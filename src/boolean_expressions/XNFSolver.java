import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XNFSolver {
    public static void main(String[] args) {
        List<TruthLine> truthTable = Arrays.asList(
                new TruthLine(1, 0,0,0),
                new TruthLine(0, 0,0,1),
                new TruthLine(0, 0,1,0),
                new TruthLine(1, 0,1,1),
                new TruthLine(0, 1,0,0),
                new TruthLine(1, 1,0,1),
                new TruthLine(0, 1,1,0),
                new TruthLine('x', 1,1,1));

        System.out.println(getResult(1, Arrays.asList(new int[]{1}, new int[]{2}), 2));
        System.out.println((int) Math.ceil(Math.log(1 + 1)));
        System.out.println((int) Math.ceil(Math.log(2 + 1)));
        System.out.println((int) Math.ceil(Math.log(3 + 1)));
    }

    public static String computeXNF(int[] orderedResults) {
        int numInputs = (int) Math.ceil(Math.log(orderedResults.length + 1));
        List<int[]> terms = new ArrayList<>();
        return "";
    }

    /**
     * Get a boolean result from a boolean XNF expression
     * @param num Input number ranging from 0-([2^numInputs]-1)
     * @param terms A list of AND expressions to be XOR'd together
     * @param numInputs Number of input bits.
     * @return
     */
    private static boolean getResult(int num, List<int[]> terms, int numInputs) {
        String binary = Integer.toBinaryString(num);
        binary = "0".repeat(numInputs - binary.length()) + numInputs;
        int numTrue = 0;
        for (int[] term : terms) {
            boolean state = true;
            for (int variable : term) {
                // variable represents the index of the binary input + 1
                // a negative variable means it is inverted
                boolean result = binary.charAt(Math.abs(variable) - 1) == '1';
                if (variable < 0)
                    result = !result;
                if (!result) {
                    state = false;
                    break;
                }
            }
            if (state) {
                numTrue++;
            }
        }
        // return if the number of true results is odd
        return numTrue % 2 == 1;
    }

    record TruthLine(int[] inputs, int output){
        public TruthLine(int output, int... input) {
            this(Arrays.stream(input).toArray(), output);
        }
        public TruthLine(char output, int... input) {
            this(Arrays.stream(input).toArray(), intRepresentation(output));
        }

        private static int intRepresentation(char c) {
            if (c == 'x')
                return 2;
            else
                return c - '0';
        }
    }
}
