package org.chuset.discord.util;

import java.util.HashMap;
import java.util.Map;

public class Fib {
    private static final Map<Long, Long> CACHE = new HashMap<>();

    public static long fibonacci(final long n) {
        return fibonacciHelper(n, 1, 0);
    }

    private static long fibonacciHelper(final long n, final long val, final long prev) { // O(n)
        if (CACHE.containsKey(n)) {
            return CACHE.get(n);
        } else if (n == 0) {
            return prev;
        } else if (n == 1) {
            return val;
        }
        CACHE.put(n, fibonacciHelper(n - 1, val + prev, val));
        return CACHE.get(n);
    }
}
