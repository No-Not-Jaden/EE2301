package fractions;

import java.util.Scanner;

public class FractionSolver {

    public static void main(String[] args) {
        prompt();
    }

    private static void prompt() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What base 10 number do you want to convert to binary?");
        double number = scanner.nextDouble();
        System.out.println("What base do you want to convert this number to?");
        int base = scanner.nextInt();
        System.out.println(number + " in base 10 is " + getBinary(number, 10, base) + " in base " + base);
    }

    private static String getBinary(double number, int digitsAfterDecimal, int outputBase) {
        int beforeDecimal = (int) number;
        double afterDecimal = number - beforeDecimal;
        StringBuilder builder = new StringBuilder(Integer.toString(beforeDecimal, outputBase) + ".");
        for (int i = 0; i < digitsAfterDecimal; i++) {
            afterDecimal *= outputBase;
            beforeDecimal = (int) afterDecimal;
            if (beforeDecimal > 0) {
                builder.append(Integer.toString(beforeDecimal, outputBase));
                afterDecimal -= beforeDecimal;
            } else {
                builder.append(0);
            }
        }
        return builder.toString();
    }
}
