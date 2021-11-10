package ru.ev3nmorn.task2;

public class NumParser {

    public static int toInt(String sample) throws IllegalArgumentException {
        if (sample == null || sample.length() == 0) {
            throw new IllegalArgumentException();
        }

        int result = 0;
        int order = 1;
        final int factor = 10;
        char[] chars = sample.toCharArray();

        for (int i = chars.length - 1; i >= 0; --i) {
            if (chars[i] < '0' || chars[i] > '9') {
                throw new IllegalArgumentException();
            }

            result += order * (chars[i] - '0');
            order *= factor;
        }

        return result;
    }

    public static double toDouble(String sample) throws IllegalArgumentException {
        if (sample == null || sample.length() == 0) {
            throw new IllegalArgumentException();
        }

        if (sample
                .chars()
                .filter(c -> c == '.')
                .count() > 1) {
            throw new IllegalArgumentException();
        }

        String[] doubleParts = sample.split("\\.");

        int intPart = toInt(doubleParts[0]),
                fractPart = doubleParts.length == 2 ? toInt(doubleParts[1]) : 0,
                scale = doubleParts.length == 2 ? doubleParts[1].length() : 0;

        return intPart + Math.pow(10, -1 * scale) * fractPart;
    }
}
