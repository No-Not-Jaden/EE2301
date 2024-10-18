package midterm_1;

import java.util.Scanner;

public class TwosCompliment {

    public static void main(String[] args) {

        prompt();
    }

    private static void prompt() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What type of calculation do you want to do? Enter 1-2");
        System.out.println("(1) Compliment of a number.");
        System.out.println("(2) Compliment subtraction.");
        int result = scanner.nextInt();
        if (result == 1) {
            promptCompliment();
        } else {
            promptSubtraction();
        }
    }

    private static void promptSubtraction() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Compliment subtraction: number1 - number2");
        System.out.println("(number1) What is your first number?");
        String number1 = scanner.nextLine();
        System.out.println("(number2) What is the number being subtracted from the first? Enter a positive value.");
        String number2 = scanner.nextLine();
        System.out.println("What base should the operation be performed in?");
        int base = scanner.nextInt();
        System.out.println("What is the maximum number of digits that your result can have? Ex: the number of bits you have.");
        int maxDigits = scanner.nextInt();
        subtractionWalkthrough(number1, number2, base, maxDigits);
    }

    private static void promptCompliment() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What number do you want to find the compliment for?");
        String number = scanner.nextLine();
        System.out.println("What base is the number in?");
        int base = scanner.nextInt();
        System.out.println("What is the maximum number of digits that your result can have? Ex: the number of bits you have.");
        int maxDigits = scanner.nextInt();
        if (number.length() < maxDigits) number = "0".repeat(maxDigits - number.length()) + number;
        System.out.println("The compliment of " + number + " is " + getCompliment(number, base) + " in base " + base);
    }

    private static void subtractionWalkthrough(String number1, String number2, int base, int maxDigits) {
        // make the numbers maxDigits long
        if (number1.length() < maxDigits) number1 = "0".repeat(maxDigits - number1.length()) + number1;
        if (number2.length() < maxDigits) number2 = "0".repeat(maxDigits - number2.length()) + number2;
        System.out.println("First, find the compliment of " + number2);
        String compliment = getCompliment(number2, base);
        System.out.println("Next, add " + number1 + " to " + compliment);
        String result = add(number1, compliment, base, maxDigits);
        System.out.println("Finally, the result of " + number1 + " - " + number2 + " = " + result);
        System.out.println("In base 10, " + Integer.valueOf(number1, base) + " - " + Integer.valueOf(number2, base) + " = " + Integer.valueOf(result, base));
    }


    private static String getCompliment(String number, int base) {
        char[] newNumber = new char[number.length()];
        System.out.println(String.valueOf(base-1).repeat(number.length()));
        System.out.println(number);
        System.out.println("-".repeat(number.length()));
        for (int i = 0; i < number.length(); i++) {
            int numericValue = Character.digit(number.charAt(i), base);
            int compliment = base - numericValue - 1;
            newNumber[i] = Character.forDigit(compliment, base);
        }
        String result = new String(newNumber);
        System.out.println(result);
        System.out.println("Add 1 to get: ");
        return add(result, "1", base, -1);
    }

    /**
     * Adds 2 numbers together with annotations printed to console.
     * @param number1 First number to add.
     * @param number2 Second number to add.
     * @param base Base that is used for the addition.
     * @param maxDigits Maximum number of digits in the output. -1 will cut off no digits
     * @return The sum of the 2 numbers.
     */
    private static String add(String number1, String number2, int base, int maxDigits) {
        // append 0's to make the numbers the same length
        if (number1.length() > number2.length()) {
            number2 = "0".repeat(number1.length() - number2.length()) + number2;
        } else if (number2.length() > number1.length()) {
            number1 = "0".repeat(number2.length() - number1.length()) + number1;
        }

        char[] result = new char[number1.length()];
        int carryIn = 0;
        char[] carries = new char[number1.length()];

        for (int i = number1.length() - 1; i >= 0; i--) {
            int char1Val = Character.digit(number1.charAt(i), base);
            int char2Val = Character.digit(number2.charAt(i), base);
            int resultVal = char1Val + char2Val + carryIn;
            if (carryIn == 1)
                carries[i] = '1';
            else
                carries[i] = ' ';
            if (resultVal >= base) {
                carryIn = 1;
                resultVal -= base;
            } else {
                carryIn = 0;
            }
            result[i] = Character.forDigit(resultVal, base);
        }

        char carryChar = carryIn == 1 ? '1' : ' ';
        String formattedResult = carryChar + new String(result);
        String carryString = carryChar + new String(carries);
        System.out.println(" " + carryString + "   Carry");
        System.out.println("  " + number1 + "   Number 1");
        System.out.println("+ " + number2 + "   Number 2");
        System.out.println("-".repeat(2 + number1.length()));
        System.out.println(" " + formattedResult);

        String output;
        if (carryIn == 1) output = formattedResult;
        else output = new String(result);
        if (maxDigits != -1 && output.length() > maxDigits) {
            output = output.substring(output.length()-maxDigits);
            System.out.println("Cut off the digits that overflow -> " + output);
        }
        return output;
    }

}
