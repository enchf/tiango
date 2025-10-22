package com.enchf;

public class DefaultOutput implements Output {
    private static final String INITIAL = "Tiango 🐵 is active...";
    private boolean firstCall = true;

    @Override
    public String next() {
        if (firstCall) {
            firstCall = false;
            return INITIAL;
        }
        return ".";
    }
}
