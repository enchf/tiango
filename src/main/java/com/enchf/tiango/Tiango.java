package com.enchf.tiango;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.enchf.tiango.utils.Version;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "tiango", 
    description = "Tiango 🐵 - A monkey that keeps your screen on",
    mixinStandardHelpOptions = true
)
public class Tiango implements Callable<Integer> {

    // Update every second
    private static final long PAUSE = Duration.ofSeconds(1).toMillis();
    private static final int DEFAULT_DURATION = 60;

    @Option(names = {"-t", "--time"}, description = "Time in minutes to execute the monkey")
    private Integer minutes = DEFAULT_DURATION;

    @Option(names = {"-d", "--debug"}, description = "Enable debug mode")
    private boolean debug = false;

    @Override
    public Integer call() throws Exception {
        // Welcome text
        System.out.printf(
            "Starting Tiango 🐵 version '%s' with parameters: { minutes: %d, debug: %b }%n", 
            Version.current(),  minutes, debug);

        long durationInMs = Duration.ofMinutes(minutes).toMillis();;
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Iteration(minutes, debug), 0, PAUSE, TimeUnit.MILLISECONDS);
        scheduler.schedule(scheduler::shutdownNow, durationInMs, TimeUnit.MILLISECONDS);

        boolean finished = scheduler.awaitTermination(durationInMs + PAUSE, TimeUnit.MILLISECONDS);
        System.out.println("\nTiango 🐵 terminated " + (finished ? "successfully." : "with timeout."));

        return 0;
    }    
    
    public static void main(String... args) {
        int exitCode = new CommandLine(new Tiango()).execute(args);
        System.exit(exitCode);
    }
}
