package org.chuset.discord.util;

import java.util.HashMap;
import java.util.Map;

public class Fib {
    private static final Map<Long, Long> CACHE = new HashMap<>();

    public static long fibonacci(final long n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else if (CACHE.containsKey(n)) {
            return CACHE.get(n);
        }
        CACHE.putIfAbsent(n, fibonacci(n - 1) + fibonacci(n - 2));
        return CACHE.get(n);
    }
}
