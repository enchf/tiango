package com.enchf;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleOutput implements Output {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String next() {
        String line;
        return (line = nextReaderLine()) != null ? line : ".";
    }

    private String nextReaderLine() {
        try {
            return reader.readLine();
        } catch (Exception ex) {
            System.err.println("Error reading console input: " + ex.getMessage());
            return null;
        }
    }
}
