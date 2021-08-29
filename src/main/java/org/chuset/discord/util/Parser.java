package org.chuset.discord.util;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CancellationException;

public class Parser {
    private static final Map<String, Double> USER_DEFINED_CONSTANTS = new MaxSizeHashMap<>(100);

    private static final RuntimeException CLOSING_BRACKET = new RuntimeException("Missing closing bracket \")\".");

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
        if (str.matches("^[a-zA-Z]+\\s*=\\s*.+$")) {
            final int startingPos = pos;
            while (Character.isLetter(ch)) {
                nextChar();
            }
            final String constantName = str.substring(startingPos, pos);
            eat('=');

            if (USER_DEFINED_CONSTANTS.containsKey(constantName)) {
                USER_DEFINED_CONSTANTS.put(constantName, parseExpression());
                throw new CancellationException("Overwrote last value of \"**%s**\" with value: **%s**.".
                        formatted(constantName,
                                Format.doubleFormatterToString(USER_DEFINED_CONSTANTS.get(constantName))));
            } else {
                USER_DEFINED_CONSTANTS.put(constantName, parseExpression());
                throw new CancellationException("Successfully declared constant \"**%s**\" with value: **%s**.".
                        formatted(constantName,
                                Format.doubleFormatterToString(USER_DEFINED_CONSTANTS.get(constantName))));
            }
        }

        final double x = parseExpression();
        if (pos < str.length()) {
            throw unexpectedCharException();
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
                throw CLOSING_BRACKET;
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
            final String funcOrConst = str.substring(startPos, pos);
            final String funcOrConstLowerCase = funcOrConst.toLowerCase(Locale.ROOT);
            switch (funcOrConstLowerCase) { // constants
                case "pi" -> x = Math.PI;
                case "e" -> x = Math.E;
                default -> {
                    if (USER_DEFINED_CONSTANTS.containsKey(funcOrConst)) {
                        x = USER_DEFINED_CONSTANTS.get(funcOrConst);
                    } else if (!eat('(')) { // functions
                        throw new RuntimeException("\"%s\" is undefined.".formatted(funcOrConst));
                    } else {

                        x = parseExpression();
                        x = switch (funcOrConstLowerCase) {
                            case "ceil" -> Math.ceil(x);
                            case "floor" -> Math.floor(x);
                            case "abs" -> Math.abs(x);
                            case "sqrt" -> Math.sqrt(x);
                            case "sin" -> Math.sin(Math.toRadians(x));
                            case "cos" -> Math.cos(Math.toRadians(x));
                            case "tan" -> Math.tan(Math.toRadians(x));
                            case "arcsin" -> Math.toDegrees(Math.asin(x));
                            case "arccos" -> Math.toDegrees(Math.acos(x));
                            case "arctan" -> Math.toDegrees(Math.atan(x));
                            case "log" -> {
                                if (!eat(',')) {
                                    yield Math.log10(x);
                                }
                                yield Log.log(x, parseExpression());
                            }
                            case "ln" -> Math.log(x);
                            case "fib" -> Fib.fibonacci(Math.round(x)); // fibonacci
                            default -> throw new RuntimeException("Unknown function: \"%s\".".formatted(funcOrConst));
                        };

                        if (!eat(')')) {
                            if (ch < 0) {
                                throw new RuntimeException(
                                        "Missing closing bracket \")\" for function \"%s\".".formatted(funcOrConst));
                            }
                            throw unexpectedCharException();
                        }
                    }
                }
            }
        } else {
            throw unexpectedCharException();
        }

        if (eat('!')) {
            x = Fac.factorial(Math.round(x)); // factorial
        }

        if (eat('^')) {
            x = Math.pow(x, parseFactor()); // exponentiation
        }

        return x;
    }

    private RuntimeException unexpectedCharException() {
        return new RuntimeException("Unexpected: \"%s\".".formatted(ch != -1 ? (char) ch : str));
    }
}
