package com.enchf;

import java.awt.*;

public class Tiango {

    private static final int[][] CIRCLES = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

    public static void main(String...args) throws Exception {
        Robot robot = new Robot();

        CellularAutomata.Builder builder = args.length > 0 ?
                CellularAutomata.builder(Integer.parseInt(args[0])) :
                CellularAutomata.randomInteresting();
        CellularAutomata automata = builder.displaySize(120).build();

        int times = args.length > 1 ? Integer.parseInt(args[1]) : 1000;

        System.out.printf("Automata rule: %d\n", automata.getRule());

        int i;
        Point current;

        while (times-- > 0) {
            i = times % CIRCLES.length;
            current = MouseInfo.getPointerInfo().getLocation();

            robot.mouseMove(current.x + CIRCLES[i][0], current.y + CIRCLES[i][1]);

            System.out.printf("%s\n", automata.next());
            // System.out.printf("%d %d\n", mouseLocation.x, mouseLocation.y);
            Thread.sleep(5000);
        }
    }
}
