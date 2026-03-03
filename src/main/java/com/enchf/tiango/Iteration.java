package com.enchf.tiango;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;

public class Iteration implements Runnable {

    private static final int[][] CIRCLES = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    private final Robot robot;
    private final Timer timer;
    private final Output output;
    private final AtomicInteger iteration;
    private final AtomicInteger externalLinesPrinted;

    private int maxLength = 0;
    
    public Iteration(int minutes, boolean debugMode) {
        this.robot = buildRobot();
        this.timer = new Timer(Duration.ofMinutes(minutes));
        this.output = new Output(debugMode);
        this.iteration = new AtomicInteger(0);
        this.externalLinesPrinted = new AtomicInteger(0);
    }
    
    @Override
    public void run() {
        try {
            if (externalLinesPrinted.get() > 0) {
                // 1. Rewind to timer position.
                System.out.print("\u001b[" + (1 + externalLinesPrinted.get()) + "A\u001b[2K\r");
                // 2. Print/update timer.
                printText(timer.next());
                // 3. Move again to the bottom of the screen
                System.out.print("\u001b[" + (externalLinesPrinted.get() + 1) + "B\r");
            } else {
                // 1. Rewind to timer position.
                if (iteration.get() != 0) {
                    System.out.print("\u001b[1A\u001b[2K\r");
                }
                
                // 2. Print/update timer.
                printText(timer.next());
            }
            
            // 4. Print external output if available.
            output.next().ifPresent(line -> {
                printText(line);
                externalLinesPrinted.incrementAndGet();
            });

            // 5. Get current mouse position.
            Point current = MouseInfo.getPointerInfo().getLocation();

            // 6. Move mouse.
            int i = iteration.get() % CIRCLES.length;
            int newX = current.x + CIRCLES[i][0];
            int newY = current.y + CIRCLES[i][1];
            robot.mouseMove(newX, newY);

            // 7. Update counters.
            iteration.incrementAndGet();
        } catch (Exception e) {
            // log and stop if something goes wrong
            System.err.println("Error: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    @SneakyThrows
    private Robot buildRobot() {
        return new Robot();
    }

    private void printText(String line) {
        maxLength = Math.max(maxLength, line.length());
        System.out.printf("%." + maxLength + "s%n", line);
    }
}
