package com.enchf.tiango;

import java.awt.*;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.enchf.tiango.utils.Version;

import lombok.SneakyThrows;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "tiango", 
    description = "Tiango 🐵 - A monkey that keeps your screen on",
    mixinStandardHelpOptions = true
)
public class Tiango implements Callable<Integer> {

    private static final int[][] CIRCLES = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
    private static final long PAUSE = Duration.ofSeconds(5).toMillis();
    private static final int DEFAULT_DURATION = 60;

    private final Robot robot = buildRobot();

    @Option(names = {"-t", "--time"}, description = "Time in minutes to execute the monkey")
    private Integer minutes = DEFAULT_DURATION;

    @Option(names = {"-d", "--debug"}, description = "Enable debug mode")
    private boolean debug = false;

    @Override
    public Integer call() throws Exception {
        banner();

        long durationInMs = Duration.ofMinutes(minutes).toMillis();;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable iteration = buildIterableRunnable();

        scheduler.scheduleAtFixedRate(iteration, 0, PAUSE, TimeUnit.MILLISECONDS);
        scheduler.schedule(scheduler::shutdownNow, durationInMs, TimeUnit.MILLISECONDS);

        boolean finished = scheduler.awaitTermination(durationInMs + PAUSE, TimeUnit.MILLISECONDS);
        System.out.println("\nTiango 🐵 terminated " + (finished ? "successfully." : "with timeout."));

        return 0;
    }

    @SneakyThrows
    private Robot buildRobot() {
        return new Robot();
    }

    private void banner() {
        System.out.printf("Starting Tiango 🐵 version '%s' with parameters: %s%n", Version.current(), printParameters());
    }

    private String printParameters() {
        return String.format("{ minutes: %d, debug: %b }", minutes, debug);
    }

    private Runnable buildIterableRunnable() {
        AtomicInteger idx = new AtomicInteger(0);
        Output output = new Output(debug);

        return () -> {
            try {
                Point current = MouseInfo.getPointerInfo().getLocation();
                int i = idx.getAndUpdate(x -> (x + 1) % CIRCLES.length);
                int newX = current.x + CIRCLES[i][0];
                int newY = current.y + CIRCLES[i][1];

                if (debug) {
                    System.out.printf("Moving mouse from (%d, %d) to (%d, %d)%n", current.x, current.y, newX, newY);
                }

                robot.mouseMove(newX, newY);
                System.out.print(output.next());    
            } catch (Exception e) {
                // log and stop if something goes wrong
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
        };
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new Tiango()).execute(args);
        System.exit(exitCode);
    }
}
