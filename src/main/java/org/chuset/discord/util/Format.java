package org.chuset.discord.util;

public class Format {
    public static String doubleFormatterToString(final double number) {
        final long longNum = (long) number;
        if (number == longNum) {
            return String.format("%d", longNum);
        } else {
            return String.format("%s", number).replace("E", " * 10^");
        }
    }
}
