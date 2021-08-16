package org.chuset.discord;

import org.chuset.discord.util.Parser;

public class CalculatorMessageHandler implements MessageTextHandler {
    @Override
    public String handleMessage(final String message) {
        return doubleFormatterToString(evaluateExpression(message));
    }

    public static String doubleFormatterToString(final double number) {
        final long longNum = (long) number;
        if (number == longNum) {
            return String.format("%d", longNum);
        } else {
            return String.format("%s", number).replace("E", " * 10^");
        }
    }

    public static double evaluateExpression(final String str) {
        return new Parser(str).parse();
    }
}