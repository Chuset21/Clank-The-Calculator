package org.chuset.discord;

public class CalculatorMessageHandler implements MessageTextHandler {
    @Override
    public String handleMessage(final String message) {
        return decimalFormatter(evaluateExpression(message));
    }

    public static String decimalFormatter(final double number) {
        final long longNum = (long) number;
        if (number == longNum) {
            return String.format("%d", longNum);
        } else {
            return String.format("%s", number);
        }
    }

    public static double evaluateExpression(final String str) {
        return new Parser(str).parse();
    }
}