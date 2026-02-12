package com.enchf.tiango;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

public class Output {
    private static final String INITIAL = "Tiango 🐵 is active...";
    private static final String CONTINUE = "Exhausted external output... Tiango 🐵 returns to default output...";
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private boolean firstCall = true;
    private boolean firstExternal = true;
    private boolean externalExhausted = false;
    private boolean externalExisted = false;
    private final boolean debug;

    public Output() {
        this.debug = false;
    }

    public Output(boolean debug) {
        this.debug = debug;
    }

    public String next() {
        if (firstCall) {
            firstCall = false;
            return INITIAL;
        }

        String line = nextReaderLine();
        String nextLine;

        if (line != null) {
            externalExisted = true;
            if (firstExternal) {
                firstExternal = false;
                nextLine = "\n" + line;
            }
            nextLine = line;
        } else if (externalExisted && !externalExhausted) {
            externalExhausted = true;
            nextLine = CONTINUE;
        } else {
            nextLine = ".";
        }
        
        if (debug) {
            System.out.printf("Next line printed: %s%n", nextLine);
        }
        
        return nextLine;
    }

    private String nextReaderLine() {
        try {
            if (debug) {
                System.out.println("Reading next line from console...");
            }
            // Check if input is available before trying to read
            if (reader.ready()) {
                return Optional.ofNullable(reader.readLine()).map(line -> line + "\n").orElse(null);
            } else {
                return null; // No input available, don't block
            }
        } catch (Exception ex) {
            System.err.println("Error reading console input: " + ex.getMessage());
            return null;
        }
    }
}
