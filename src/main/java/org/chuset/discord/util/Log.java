package org.chuset.discord.util;

public class Log {
    public static double log(final double a, final double b) {
        if (b == 10) {
            return Math.log10(a);
        }
        return Math.log(a) / Math.log(b);
    }
}
