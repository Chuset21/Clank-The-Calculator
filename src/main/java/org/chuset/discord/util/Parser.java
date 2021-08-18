package org.chuset.discord.util;

import java.util.Locale;

public class Parser {
    private final String str;
    private int pos;
    private int ch;

    public Parser(final String str) {
        if (str.isBlank()) {
            throw new RuntimeException("Empty string given.");
        }

        this.str = str;
        pos = -1;
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean eat(final char charToEat) {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    public double parse() {
        nextChar();
        final double x = parseExpression();
        if (pos < str.length()) {
            throw new RuntimeException("Unexpected: \"%s\".".formatted(ch != -1 ? (char) ch : str));
        }
        return x;
    }

    private double parseExpression() {
        double x = parseTerm();
        while (true) {
            if (eat('+')) {
                x += parseTerm(); // addition
            } else if (eat('-')) {
                x -= parseTerm(); // subtraction
            } else {
                return x;
            }
        }
    }

    private double parseTerm() {
        double x = parseFactor();
        while (true) {
            if (eat('*')) {
                x *= parseFactor(); // multiplication
            } else if (eat('/')) {
                x /= parseFactor(); // division
            } else {
                return x;
            }
        }
    }

    private double parseFactor() {
        if (eat('+')) {
            return parseFactor(); // unary plus
        }

        if (eat('-')) {
            return -parseFactor(); // unary minus
        }

        double x;
        final int startPos = pos;
        if (eat('(')) { // parentheses
            x = parseExpression();
            if (!eat(')')) {
                throw new RuntimeException("Missing closing bracket \")\".");
            }
        } else if (Character.isDigit(ch) || ch == '.') { // numbers
            while (Character.isDigit(ch) || ch == '.') {
                nextChar();
            }
            x = Double.parseDouble(str.substring(startPos, pos));
        } else if (Character.isLetter(ch)) {
            while (Character.isLetter(ch)) {
                nextChar();
            }
            final String funcOrConst = str.substring(startPos, pos).toLowerCase(Locale.ROOT);
            switch (funcOrConst) { // constants
                case "pi" -> x = Math.PI;
                case "e" -> x = Math.E;
                default -> { // functions
                    x = parseFactor();
                    x = switch (funcOrConst) {
                        case "sqrt" -> Math.sqrt(x);
                        case "sin" -> Math.sin(Math.toRadians(x));
                        case "cos" -> Math.cos(Math.toRadians(x));
                        case "tan" -> Math.tan(Math.toRadians(x));
                        case "arcsin" -> Math.toDegrees(Math.asin(x));
                        case "arccos" -> Math.toDegrees(Math.acos(x));
                        case "arctan" -> Math.toDegrees(Math.atan(x));
                        case "log" -> Math.log(x);
                        case "fib" -> Fib.fibonacci(Math.round(x)); // fibonacci
                        default -> throw new RuntimeException("Unknown function: \"%s\".".formatted(funcOrConst));
                    };
                }
            }
        } else {
            throw new RuntimeException("Unexpected: \"%s\".".formatted(ch != -1 ? (char) ch : str));
        }

        if (eat('!')) {
            x = Fac.factorial(Math.round(x)); // factorial
        }

        if (eat('^')) {
            x = Math.pow(x, parseFactor()); // exponentiation
        }

        return x;
    }
}
