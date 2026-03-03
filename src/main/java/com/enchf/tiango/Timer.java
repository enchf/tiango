package com.enchf.tiango;

import java.time.Duration;
import java.util.Iterator;

public class Timer implements Iterator<String> {

    private final Duration initial;
    private Duration current;

    public Timer(Duration initial) {
        this.initial = initial;
        this.current = this.initial;
    }
    
    @Override
    public boolean hasNext() {
        return !this.current.isZero() && !this.current.isNegative();
    }
    
    @Override
    public String next() {
        String nextLine = String.format(
            "Tiango 🐵 remaining time: %d:%02d", 
            this.current.toMinutes(), 
            this.current.getSeconds() % 60
        );
        this.current = this.current.minus(Duration.ofSeconds(1));
        return nextLine;
    }
}
