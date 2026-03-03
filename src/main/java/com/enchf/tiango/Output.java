package com.enchf.tiango;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

public class Output {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private final boolean debug;

    public Output() {
        this.debug = false;
    }

    public Output(boolean debug) {
        this.debug = debug;
    }

    public Optional<String> next() {
        Optional<String> nextLine = nextReaderLine();

        if (debug) {
            System.out.printf("Next line to be printed: %s%n", nextLine.orElse("null"));
        }
        
        return nextLine;
    }

    private Optional<String> nextReaderLine() {
        try {
            if (debug) {
                System.out.println("Reading next line from console...");
            }
            // Check if input is available before trying to read
            if (reader.ready()) {
                return Optional.ofNullable(reader.readLine());
            } else {
                return Optional.empty(); // No input available, don't block
            }
        } catch (Exception ex) {
            System.err.println("Error reading console input: " + ex.getMessage());
            return Optional.empty();
        }
    }
}
