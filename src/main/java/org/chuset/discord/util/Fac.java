package org.chuset.discord.util;

import java.util.HashMap;
import java.util.Map;

public class Fac {
    private static final Map<Long, Double> CACHE = new HashMap<>();

    public static double factorial(final long n) {
        if (n <= 1) {
            return 1;
        }
        CACHE.putIfAbsent(n, n * factorial(n - 1));
        return CACHE.get(n);
    }
}
