package com.enchf;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Creates a cellular automata using the specified configuration and sets the display size.
 * Configuration works as follows:
 *
 * A cellular automata is a sequence of an array of squares, either empty or full.
 * Initially, it starts with a single full square in the middle.
 * Then, a new array is generated depending on the value of each square and its contiguous squares.
 *
 * For example:
 *
 * █ █ █ = ░ : Full left, full square and full right, gives empty square.
 * █ █ ░ = ░
 * █ ░ █ = ░
 * █ ░ ░ = █
 * ░ █ █ = █
 * ░ █ ░ = █
 * ░ ░ █ = █
 * ░ ░ ░ = ░
 *
 * This configuration gives a binary number of 8 digits: 0 0 0 1 1 1 1 0, equivalent to 30,
 * so this is the configuration parameter.
 */
public class CellularAutomata {

    private final int config;
    private final char fullChar;
    private final char emptyChar;
    private final int displaySize;

    private String current = null;

    public static final int[] INTERESTING_ONES = {
        18, 22, 26, 28, 30, 45, 57, 60, 62, 69, 70, 71, 73, 75, 78, 82, 86, 89, 90,
        92, 93, 94, 99, 101, 102, 105, 107, 109, 110, 118, 124, 126, 129, 131, 133,
        135, 137, 141, 145, 146, 147, 149, 150, 153, 154, 156, 157, 161, 163, 165,
        167, 169, 181, 182, 188, 193, 195, 197, 210, 214, 218, 225
    };

    private CellularAutomata(int config, char fullChar, char emptyChar, int displaySize) {
        this.config = config;
        this.fullChar = fullChar;
        this.emptyChar = emptyChar;
        this.displaySize = displaySize;
    }

    public String next() {
        return (current = Optional.ofNullable(current).map(curr -> generate()).orElse(firstLine()));
    }

    public int getRule() {
        return config;
    }

    private String generate() {
        return IntStream.range(0, current.length())
                .map(this::defineState)
                .mapToObj(i -> i == 0 ? emptyChar : fullChar)
                .map(Objects::toString)
                .collect(Collectors.joining());
    }

    private int defineState(int i) {
        String stateString = IntStream.range(i - 1, i + 2)      // Range of previous, current and next position.
                .map(this::full)
                .boxed()
                .map(Object::toString)
                .collect(Collectors.joining(""));
        int state = Integer.valueOf(stateString, 2);

        return (config >> state) % 2;
    }

    private int full(int index) {
        return index >= 0 && index < current.length() && current.charAt(index) == fullChar ? 1 : 0;
    }

    private String firstLine() {
        int leftLen = displaySize / 2 - ((displaySize + 1) % 2);
        int rightLen = displaySize / 2;
        String format = String.format("%%%ds%s%%%ds", leftLen, fullChar, rightLen);
        return String.format(format, emptyChar, emptyChar);
    }

    public static Builder randomInteresting() {
        return builder(INTERESTING_ONES[new Random().nextInt(INTERESTING_ONES.length)]);
    }

    public static Builder builder(int config) {
        return new Builder(config);
    }

    static class Builder {
        private final int config;
        private char fullChar = '█';
        private char emptyChar = ' ';
        private int displaySize = 80;

        private Builder(int config) {
            this.config = config;
        }

        public Builder fullChar(char fullChar) {
            this.fullChar = fullChar;
            return this;
        }

        public Builder emptyChar(char emptyChar) {
            this.emptyChar = emptyChar;
            return this;
        }

        public Builder displaySize(int displaySize) {
            this.displaySize = displaySize;
            return this;
        }

        public CellularAutomata build() {
            return new CellularAutomata(config, fullChar, emptyChar, displaySize);
        }
    }
}
