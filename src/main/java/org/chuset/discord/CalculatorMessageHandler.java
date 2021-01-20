package org.chuset.discord;

import java.util.ArrayList;
import java.util.List;

public class CalculatorMessageHandler implements MessageTextHandler {
    @Override
    public String handleMessage(String message) throws Exception {
        List<Double> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();
        int count = 0;
        for (int index = 0; index < message.length(); index++) {
            if (message.charAt(index) == '+' || message.charAt(index) == '-'
                    || message.charAt(index) == '*' || message.charAt(index) == '/') {
                operators.add(message.charAt(index));
                numbers.add(Double.parseDouble(message.substring(count, index)));
                count = index + 1;
            }
        }
        numbers.add(Double.parseDouble(message.substring(count)));
        double sum = numbers.get(0);
        for (int index = 0; index < operators.size(); index++) {
            int numberIndex = index + 1;
            switch (operators.get(index)) {
                case '+' -> sum += numbers.get(numberIndex);
                case '-' -> sum -= numbers.get(numberIndex);
                case '*' -> sum *= numbers.get(numberIndex);
                case '/' -> sum /= numbers.get(numberIndex);
            }
        }
        return decimalFormatter(sum);
    }

    public static String decimalFormatter(double number) {
        if (number == (long)number)
            return String.format("%d",(long)number);
        else
            return String.format("%s",number);
    }
}
