package org.chuset.discord.util;

public class Parser {
    private final String str;
    private int pos;
    private int ch;

    public Parser(final String str) {
        this.str = str;
        pos = -1;
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean eat(final char charToEat) {
        while (ch == ' ') {
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
            throw new RuntimeException("Unexpected: " + (char) ch);
        }
        return x;
    }

    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor
    // factor = `+` factor | `-` factor | `(` expression `)`
    //        | number | functionName factor | factor `^` factor

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
            if (eat('!')) {
                x = Fac.factorial(Math.round(x)); // factorial
            } else if (eat('*')) {
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
            eat(')');
        } else if (Character.isDigit(ch) || ch == '.') { // numbers
            while (Character.isDigit(ch) || ch == '.') {
                nextChar();
            }
            x = Double.parseDouble(str.substring(startPos, pos));
        } else if (Character.isLetter(ch)) { // functions
            while (Character.isLetter(ch)) {
                nextChar();
            }
            final String func = str.substring(startPos, pos);
            x = parseFactor();
            x = switch (func) {
                case "sqrt" -> Math.sqrt(x);
                case "sin" -> Math.sin(Math.toRadians(x));
                case "cos" -> Math.cos(Math.toRadians(x));
                case "tan" -> Math.tan(Math.toRadians(x));
                case "fib" -> Fib.fibonacci(Math.round(x)); // fibonacci
                default -> throw new RuntimeException("Unknown function: " + func);
            };
        } else {
            throw new RuntimeException("Unexpected: " + (char) ch);
        }

        if (eat('^')) {
            x = Math.pow(x, parseFactor()); // exponentiation
        }

        return x;
    }
}
