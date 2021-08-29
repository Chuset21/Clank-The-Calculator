package org.chuset.discord;

import org.chuset.discord.util.Format;
import org.chuset.discord.util.Parser;

public class CalculatorMessageHandler implements MessageTextHandler {
    @Override
    public String handleMessage(final String message) {
        return Format.doubleFormatterToString(evaluateExpression(message));
    }

    public static double evaluateExpression(final String str) {
        return new Parser(str).parse();
    }
}