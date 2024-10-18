package midterm_1;

import java.util.*;
import java.util.stream.IntStream;

record Expression(double[] numbers, char[] operators) {
    /**
     * Perform an expression from left to right.
     * @return The result of the expression.
     */
    public double performExpression() {
        if (numbers.length == 0)
            return 0;
        if (numbers.length == 1)
            return numbers[0];
        return performOperation('^').
                performOperation('*').
                performOperation('/').
                performOperation('+').
                performOperation('-').
                numbers[0];
    }

    private Expression performOperation(char operator) {

        List<Double> variableNumbers = new ArrayList<>(Arrays.stream(numbers).boxed().toList());
        List<Character> variableOperators = new ArrayList<>(IntStream.range(0, operators.length).mapToObj(i -> operators[i]).toList());
        for (int i = 0; i < variableOperators.size(); i++) {
            if (variableOperators.get(i) == operator) {
                variableNumbers.set(i, performOperation(variableNumbers.get(i), variableNumbers.get(i+1), operator));
                variableNumbers.remove(i+1);
                variableOperators.remove(i);
                i--;
            }
        }
        double[] newNumbers = variableNumbers.stream().mapToDouble(variableNumber -> variableNumber).toArray();
        char[] newOperators = new char[variableOperators.size()];
        IntStream.range(0, variableOperators.size()).forEach(i -> newOperators[i] = variableOperators.get(i));

        return new Expression(newNumbers, newOperators);
    }

    private static double performOperation(double firstNumber, double secondNumber, char operator) {
        return switch (operator) {
            case '-' -> firstNumber - secondNumber;
            case '*' -> firstNumber * secondNumber;
            case '/' -> firstNumber / secondNumber;
            case '^' -> Math.pow(firstNumber, secondNumber);
            default -> firstNumber + secondNumber;
        };
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < operators.length; i++) {
            builder.append(numbers[i]).append(operators[i]);
        }
        builder.append(numbers[operators.length]);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expression(double[] numbers1, char[] operators1))) return false;
        return Objects.deepEquals(numbers, numbers1) && Objects.deepEquals(operators, operators1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(numbers), Arrays.hashCode(operators));
    }
}

public class BaseSolver {

    public static void main(String[] args) {
        System.out.println(promptBase());
    }

    private static double promptBase() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please type in an expression with one solution on the right. Separate operators with spaces. Ex: 123 + 123 = 246");
        String input = scanner.nextLine();
        List<String> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();
        while (input.contains(" ")) {
            String currentChunk = input.substring(0, input.indexOf(" "));
            input = input.substring(currentChunk.length() + 1);
            if (currentChunk.equals("+") || currentChunk.equals("-")
                    || currentChunk.equals("*") || currentChunk.equals("/")
                    || currentChunk.equals("^")) {
                operators.add(currentChunk.charAt(0));
            } else if (!currentChunk.equals("=")) {
                numbers.add(currentChunk);
            }
        }
        String[] newNumbers = numbers.toArray(new String[0]);
        char[] newOperators = new char[operators.size()];
        IntStream.range(0, operators.size()).forEach(i -> newOperators[i] = operators.get(i));
        // input is the solution
        double base = calculateBase(newNumbers, newOperators, input);
        if (base == 0.0)
            System.out.println("There is no common base between terms, or the base is above " + MAX_BASE);
        return base;
    }

    private static final double MAX_BASE = 100;

    /**
     * Calculate the lowest base that can be used in an expression.
     * @param numbers The numbers in the expression.
     * @param operators The operators between the numbers. This length should be one less than the length of numbers.
     * @param solution The solution to the expression.
     * @return The lowest base that can be used.
     */
    private static double calculateBase(String[] numbers, char[] operators, String solution) {
        String[] allNumbers = new String[numbers.length + 1];
        System.arraycopy(numbers, 0, allNumbers, 0, numbers.length);
        allNumbers[numbers.length] = solution;
        for (double base = getLowestBase(allNumbers); base < MAX_BASE + 1; base++) {
            double base10Solution = getBase10Value(solution, base);
            double[] parsedNumbers = new double[numbers.length];
            for (int i = 0; i < parsedNumbers.length; i++) {
                parsedNumbers[i] = getBase10Value(numbers[i], base);
            }
            Expression expression = new Expression(parsedNumbers, operators);
            if (expression.performExpression() == base10Solution) {
                return base;
            }
        }
        return 0.0;
    }



    private static double getBase10Value(String number, double base) {
        double total = 0;
        int decimalIndex = number.indexOf('.');
        for (int i = 0; i < number.length(); i++) {
            // iterating from right to left in the string
            if (i == decimalIndex)
                continue;
            double numericalValue = Character.getNumericValue(number.charAt(number.length()-1-i));
            total += (numericalValue * Math.pow(base, i - decimalIndex - 1.0));
        }
        return total;
    }

    private static double getLowestBase(String[] numbers) {
        if (numbers.length == 0)
            return 0;
        // the lowest base is the highest value in the string
        double lowestBase = getLowestBase(numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            double base = getLowestBase(numbers[i]);
            if (base > lowestBase)
                lowestBase = base;
        }
        return lowestBase;
    }

    private static double getLowestBase(String number) {
        double highestValue = 0;
        for (int i = 0; i < number.length(); i++) {
            double numericalValue = Character.getNumericValue(number.charAt(number.length()-1));
            if (numericalValue > highestValue)
                highestValue = numericalValue;
        }
        return highestValue + 1;
    }
}
