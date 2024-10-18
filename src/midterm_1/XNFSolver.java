package midterm_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class XNFSolver {
    public static void main(String[] args) {
        promptXNF();
    }

    public static void promptXNF() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How would you like to input your truth table? Enter 1-2");
        System.out.println("(1) Intuitive way.");
        System.out.println("(2) Quick way.");
        int input = scanner.nextInt();
        while (input > 2 || input < 1) {
            System.out.println("Invalid input.");
            input = scanner.nextInt();
        }
        int[] orderedResults;
        if (input == 1) {
            orderedResults = promptResults();
        } else {
            orderedResults = promptFastResults();
        }
        System.out.println("\n\n\n\n\n");
        printTruthTable(Arrays.stream(orderedResults).boxed().toList(), 0);
        System.out.println("f = " + termsToString(computeXNF(orderedResults)));
    }

    private static int[] promptFastResults() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter in the outputs to the truth tabel all on one line with nothing separating them.");
        System.out.println("Ex: 00101x0101");
        String input = scanner.nextLine();
        int[] results = new int[input.length()];
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '0')
                results[i] = 0;
            else if (input.charAt(i) == '1')
                results[i] = 1;
            else
                results[i] = 2;
        }
        return results;
    }

    private static int[] promptResults() {

        Scanner scanner = new Scanner(System.in);
        List<Integer> inputs = new ArrayList<>();
        System.out.println("Complete the truth table:");
        System.out.println("x1     f");
        System.out.print(" 0   | ");
        String userInput = scanner.nextLine();
        try {
            inputs.add(Integer.parseInt(userInput));
        } catch (NumberFormatException e) {
            // don't care about the first input
            inputs.add(2);
        }

        while (!userInput.equalsIgnoreCase("c")) {
            int startingTableWidth = Integer.toBinaryString(inputs.size()).length();
            System.out.println("Enter '0', '1', or 'x' into the truth table.");
            System.out.println("Enter 'c' to confirm your table.");
            printTruthTable(inputs, 1);
            while (Integer.toBinaryString(inputs.size()).length() == startingTableWidth) {
                printTruthLine(inputs.size(), startingTableWidth);
                userInput = scanner.nextLine();
                if (!userInput.equalsIgnoreCase("c")) {
                    try {
                        inputs.add(Integer.parseInt(userInput));
                    } catch (NumberFormatException e) {
                        inputs.add(2);
                    }
                } else {
                    break;
                }
            }
        }

        return inputs.stream().mapToInt(input -> input).toArray();
    }

    private static void printTruthTable(List<Integer> orderedOutputs, int extraRows) {
        String maxNum = Integer.toBinaryString(orderedOutputs.size() + (extraRows - 1));
        for (int i = 0; i < maxNum.length(); i++) {
            System.out.print("x" + (i+1) + " ");
            if (i+1 < 10)
                System.out.print(" ");
        }
        System.out.println("   f");
        for (int i = 0; i < orderedOutputs.size(); i++) {
            printTruthLine(i, maxNum.length());
            if (orderedOutputs.get(i) < 2)
                System.out.println(orderedOutputs.get(i));
            else
                System.out.println("#");
        }
    }

    private static void printTruthLine(int num, int size) {
        String inputs = integerStringRepresentation(num, size);
        for (int j = 0; j < inputs.length(); j++) {
            System.out.print(" " + inputs.charAt(j) + "  ");
        }
        System.out.print(" | ");
    }

    private static List<int[]> computeXNF(int[] orderedResults) {
        int numInputs = (int) Math.ceil(Math.log(orderedResults.length) / Math.log(2));
        List<int[]> terms = new ArrayList<>();
        for (int i = 0; i < orderedResults.length; i++) {
            if (orderedResults[i] < 2) { // 2 represents a "don't care"
                boolean expectedResult = orderedResults[i] == 1;
                if (getResult(i, terms, numInputs) != expectedResult) {
                    // add to expression
                    terms.add(numToTerm(i, numInputs));
                }
            }
        }
        return terms;
    }

    private static int[] numToTerm(int num, int numInputs) {
        String binary = integerStringRepresentation(num, numInputs);
        int[] term = new int[binary.replace("0", "").length()];
        if (term.length == 0)
            return new int[]{0};
        int termIndex = 0;
        for (int i = 0; i < binary.length(); i++) {
            // 'i' represents the input variable number - 1
            if (binary.charAt(i) == '1') {
                term[termIndex] = i+1;
                termIndex++;
            }
        }
        return term;
    }

    private static String integerStringRepresentation(int num, int length) {
        String binary = Integer.toBinaryString(num);
        return "0".repeat(length - binary.length()) + binary;
    }

    /**
     * Get a boolean result from a boolean XNF expression
     * @param num Input number ranging from 0-([2^numInputs]-1)
     * @param terms A list of AND expressions to be XOR'd together
     * @param numInputs Number of input bits.
     * @return True if the expression results in a 1
     */
    private static boolean getResult(int num, List<int[]> terms, int numInputs) {
        String binary = integerStringRepresentation(num, numInputs);
        int numTrue = (int) terms.stream().filter(term -> getTermResult(term, binary)).count();
        // return if the number of true results is odd
        return numTrue % 2 == 1;
    }

    /**
     * Get the result of the term with the given binary inputs.
     * @param term Variables to put in an AND gate.
     *            The containing integers are the bits found at their index -1 in the binary string.
     *            These can be negative, meaning they should be inverted.
     * @param binary A string of binary to represent the input.
     * @return True if all the terms point to 1
     */
    private static boolean getTermResult(int[] term, String binary) {
        if (term[0] != 0) {
            for (int variable : term) {
                // variable represents the index of the binary input + 1
                // a negative variable means it is inverted
                boolean result = binary.charAt(Math.abs(variable) - 1) == '1';
                if (variable < 0)
                    result = !result;
                if (!result) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get a string representation of XOR terms
     * @param terms A list of AND terms.
     * @return A string representation of the expression.
     */
    private static String termsToString(List<int[]> terms) {
        StringBuilder builder = new StringBuilder();
        for (int[] term : terms) {
            if (!builder.isEmpty())
                builder.append(" ⊕ ");
            for (int variable : term) {
                if (variable == 0) {
                    builder.append(1);
                } else {
                    if (variable > 0)
                        builder.append('x');
                    else
                        builder.append("x̄");
                    builder.append(Math.abs(variable));
                }
            }
        }
        return builder.toString();
    }

}
