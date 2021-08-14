package org.chuset.discord.util;

import java.util.HashMap;
import java.util.Map;

public class Fac {
    private static final Map<Long, Long> CACHE = new HashMap<>();

    public static long factorial(final long n) {
        if (n <= 1) {
            return 1;
        }
        CACHE.putIfAbsent(n, factorial(n - 1));
        return CACHE.get(n);
    }
}
