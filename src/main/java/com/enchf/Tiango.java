package com.enchf;

import java.awt.*;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Tiango {

    private static final int[][] CIRCLES = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
    private static final long PAUSE = Duration.ofSeconds(5).toMillis();

    public static void main(String...args) throws Exception {
        Robot robot = new Robot();
        long totalMs = determineDuration(args);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable iteration = getIteration(robot);

        scheduler.scheduleAtFixedRate(iteration, 0, PAUSE, TimeUnit.MILLISECONDS);
        scheduler.schedule(scheduler::shutdownNow, totalMs, TimeUnit.MILLISECONDS);

        boolean finished = scheduler.awaitTermination(totalMs + PAUSE, TimeUnit.MILLISECONDS);
        System.out.println("Tiango terminated " + (finished ? "successfully." : "with timeout."));
    }

    private static Runnable getIteration(Robot robot) {
        AtomicInteger idx = new AtomicInteger(0);
        Output output = Objects.isNull(System.console()) ? new DefaultOutput() : new ConsoleOutput();
        return () -> {
            try {
                Point current = MouseInfo.getPointerInfo().getLocation();
                int i = idx.getAndUpdate(x -> (x + 1) % CIRCLES.length);
                robot.mouseMove(current.x + CIRCLES[i][0], current.y + CIRCLES[i][1]);
                System.out.print(output.next());
            } catch (Exception e) {
                // log and stop if something goes wrong
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
        };
    }

    private static long determineDuration(String[] args) {
        long defaultTimeMs = Duration.ofHours(1).toMillis();
        if (args.length == 1) {
            try {
                int minutes = Integer.parseInt(args[0]);
                System.out.println("Setting duration to " + minutes + " minutes.");
                return Duration.ofMinutes(minutes).toMillis();
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format for minutes: " + args[0]);
                return defaultTimeMs;
            }
        }

        System.out.println("No valid duration provided. Defaulting to 1 hour.");
        return defaultTimeMs;
    }
}
