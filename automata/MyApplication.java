package automata;

import java.util.*;
import java.util.stream.Collectors;

/* TODO: Implement a grep-like tool. */

public class MyApplication {
    Set<Character> sigma = new HashSet<>();

    // Your own regex search function here:
    // Takes a regex and a text as input
    // Returns true if the regex matches any substring of the text; otherwise returns false
    public static boolean mySearch(String regex, String text) {
        // build sigma
        HashSet<Character> sigma = new HashSet<>();
        Set<Character> operators = Set.of('(', ')', '+', '*', '|', '?', '.');
        for (Character c : text.toCharArray())
            if (!operators.contains(c))
                sigma.add(c);
        for (Character c : regex.toCharArray())
            if (!operators.contains(c))
                sigma.add(c);
//        System.out.println("sigma " + sigma);

        // adjust regex
        regex = ".*(" + regex + ").*";
        try {

            // construct minimal DFA from regex
            EpsNFA.STATE_COUNTER = 0;
            EpsNFA epsNFA = REParser.parse(regex).accept(new BuilderNFA(sigma));
            DFA dfa = new DFA(epsNFA, sigma);
            dfa.minimize();

            // test if DFA accepts the string
            Set<Set<Integer>> currentStates = Set.of(dfa.getInitialState());
            for (Character c : text.toCharArray()) {
                Set<Set<Integer>> nextStates = new HashSet<>();
                for (Set<Integer> q : currentStates) {
                    nextStates.addAll(dfa.getSuccessors(q, c));
                }
                currentStates = nextStates;
            }
            for (Set<Integer> acceptingState: dfa.getAcceptingStates()) {
                if (currentStates.contains(acceptingState)) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void process(String regex, String line) {
        if (mySearch(regex, line))
            System.out.println(line);
    }

    public static void main(String[] args) throws Exception {
        java.util.Scanner s = new java.util.Scanner(System.in);
        Set<Character> sigma = s.nextLine().chars()
                .mapToObj(e->(char)e).collect(Collectors.toSet());
        String regex = s.nextLine();
        while (s.hasNextLine()) {
            String nextLine = s.nextLine();
            process(regex, nextLine);
        }
        System.exit(0);

    }

}
