package com.enchf;

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

    public String next() {
        if (firstCall) {
            firstCall = false;
            return INITIAL;
        }

        String line = nextReaderLine();

        if (line != null) {
            externalExisted = true;
            if (firstExternal) {
                firstExternal = false;
                return "\n" + line;
            }
            return line;
        } else if (externalExisted && !externalExhausted) {
            externalExhausted = true;
            return CONTINUE;
        } else {
            return ".";
        }
    }

    private String nextReaderLine() {
        try {
            return Optional.ofNullable(reader.readLine()).map(line -> line + "\n").orElse(null);
        } catch (Exception ex) {
            System.err.println("Error reading console input: " + ex.getMessage());
            return null;
        }
    }
}
