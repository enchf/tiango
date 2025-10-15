package com.enchf;

import java.awt.*;

public class Tiango {

    private static final int[][] CIRCLES = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    public static void main(String...args) throws Exception {
        Robot robot = new Robot();
        int times = args.length > 0 ? Integer.parseInt(args[0]) : 1000;

        int i;
        Point current;

        while (times-- > 0) {
            i = times % CIRCLES.length;
            current = MouseInfo.getPointerInfo().getLocation();

            robot.mouseMove(current.x + CIRCLES[i][0], current.y + CIRCLES[i][1]);

            System.out.print(".");
            Thread.sleep(5000);
        }
    }
}
