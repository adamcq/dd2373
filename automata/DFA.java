package automata;

import java.util.*;

public class DFA extends Automaton<Set<Integer>, Character>{
    Set<Character> sigma;
    public DFA(EpsNFA epsNFA, Set<Character> sigma) {
        this.setInitialState(new HashSet<>());
        this.sigma = sigma;
        dfaToNfa(epsNFA);
    }

    public void dfaToNfa(EpsNFA epsNFA) {

        // 1. set initial state of DFA
        this.setInitialState(epsNFA.epsClosure(epsNFA.getInitialState()));

        // 2. build transition function for DFA
        List<Set<Integer>> toVisit = new ArrayList<Set<Integer>>();
        toVisit.add(this.getInitialState());
        Set<Set<Integer>> visited = new HashSet<>();

        while (!toVisit.isEmpty()) {
//            System.out.println(getStates());
            // pick unvisited dfaSrc in DFA
            Set<Integer> dfaSrc = toVisit.remove(0);
            visited.add(dfaSrc);

            // perform 1 step of BFS on dfaSrc
            for (Character x : sigma) {

                // create a set of NFA states reachable by x
                Set<Integer> dfaDstOnX = new HashSet<>();
                for (Integer nfaSrc : dfaSrc)
                    dfaDstOnX.addAll(epsNFA.getSuccessors(nfaSrc,x));

                // the next DFA state is their epsilon closure
                // find the closure and add transition
                dfaDstOnX = epsNFA.epsClosure(dfaDstOnX);
                this.addTransition(dfaSrc, dfaDstOnX, x);

                // add the e-closed set (dfa destination) to the queue
                if (!visited.contains(dfaDstOnX))
                    toVisit.add(dfaDstOnX);
            }
        }

        // 3. set accepting states for DFA
        for (Set<Integer> state : this.getStates()) {
            for (Integer nfaAccepting : epsNFA.epsClosure(epsNFA.getFirstAcceptingState())) { // probably unnecessary since the closure should be just the state
                if (state.contains(nfaAccepting))
                    this.addAcceptingState(state);
            }
        }
    }

//    public void minimize() {
//        String[][] table = new String[getStates().size()][getStates().size()];
//        HashSet<Integer>[] states = getStates().toArray(new HashSet[0]);
//        HashMap<Set<Integer>, Integer> stateToIndex = new HashMap<>();
//        for (int i = 0; i < states.length; i++) {
//            stateToIndex.put(states[i], i);
//        }
//
////        System.out.println("States: " + Arrays.toString(states));
//
//        // Step 2 mark states {p,q} with epsilon if p in F and q not in F or vice versa
//        for (int i = 0; i < states.length; i++) {
//            for (int j = i+1; j < states.length; j++) {
//                Set<Integer> p = states[i];
//                Set<Integer> q = states[j];
//                if (accepting.contains(p) ^ accepting.contains(q))
//                    table[i][j] = EpsNFA.EPSILON.toString();
//            }
//        }
//
//        // Step 3 for unmarked {p,q} & any a in sigma,
//        // such that {delta(p,a), delta(q,a)} is marked by x,
//        // mark {p,q} with ax
//        for (int i = 0; i < states.length; i++) {
//            for (int j = i+1; j < states.length; j++) {
//                if (table[i][j] == null) {
//                    Set<Integer> p = states[i];
//                    Set<Integer> q = states[j];
//                    for (Character c : sigma) {
//                        // get delta(p,c) and delta(q,c) -> resulting in 2 new states
//                        Set<Integer> deltaPC = getSuccessors(p, c).iterator().next();
//                        Set<Integer> deltaQC = getSuccessors(q, c).iterator().next();
//
//                        // get indices of delta(p,c) and delta(q,c) in the table
//                        int ii = Math.min(stateToIndex.get(deltaPC), stateToIndex.get(deltaQC));
//                        int jj = Math.max(stateToIndex.get(deltaPC), stateToIndex.get(deltaQC));
//
//                        // if table[ii][jj] is not null, update table[i][j]
//                        if (table[ii][jj] != null) {
//                            if (table[ii][jj].equals(EpsNFA.EPSILON.toString()))
//                                table[i][j] = c.toString();
//                            else
//                                table[i][j] = c.toString() + table[ii][jj];
//                        }
//                    }
//                }
//            }
//        }
//
//        // 4. create equivalence classes
//        HashSet<HashSet<Set<Integer>>> equivalenceClasses = new HashSet<>();
//
//        for (int i = 0; i < states.length; i++) {
//            for (int j = i+1; j < states.length; j++) {
//                if (table[i][j] == null) {
//                    Set<Integer> p = states[i];
//                    Set<Integer> q = states[j];
//                    boolean added = false;
//                    // if a pair was already found (3+ in the equivalence class)
//                    for (HashSet<Set<Integer>> equivalenceClass : equivalenceClasses) {
//                        if (equivalenceClass.contains(p) || equivalenceClass.contains(q)) {
//                            equivalenceClass.add(p);
//                            equivalenceClass.add(q);
//                            added = true;
//                        }
//                    }
//                    // if neither of the states have been added before (first 2)
//                    if (!added) {
//                        HashSet<Set<Integer>> newPair = new HashSet<>();
//                        newPair.add(p);
//                        newPair.add(q);
//                        equivalenceClasses.add(newPair);
//                    }
//                }
//            }
//        }
//
//        // 5. in each equivalence class, merge the equivalent states into the first one to minimize DFA
//        for (Set<Set<Integer>> equivalentStates : equivalenceClasses) {
//            Set<Integer> mergeInto = equivalentStates.iterator().next();
//            for (Set<Integer> mergeFrom : equivalentStates)
//                this.mergeStates(mergeInto, mergeFrom);
//        }
//
////        // print table
////        for (int i = 0; i < states.length; i++) {
////            for (int j = i+1; j < states.length; j++) {
////                System.out.print("(" + i + "" + j + "" + table[i][j] + ") ");
////            }
////            System.out.println();
////        }
////        // print equivalence classes
////        System.out.println(equivalenceClasses);
//    }

    // DIEGO'S MINIMIZE
    public void minimize() {

        // We use Myhill-Nerode Theorem

        // Step 1: Create pairs (a, b) of all states in DFA
        Set<List<Set<Integer>>> statePairs = new HashSet<>();
        for (Set<Integer> a : getStates()) {
            for (Set<Integer> b : getStates()) {
                if (!a.equals(b)) {
                    List<Set<Integer>> pair = Arrays.asList(a, b);
                    statePairs.add(pair);
                }
            }
        }

        // Step 2: Mark pairs where one state is final and the other is non-final
        Map<List<Set<Integer>>, Boolean> markedPairs = new HashMap<>();
        for (List<Set<Integer>> pair : statePairs) {
            markedPairs.put(pair, false);
        }
        for (List<Set<Integer>> pair : statePairs) {
            Set<Integer> a = pair.get(0);
            Set<Integer> b = pair.get(1);
            boolean marked = (getAcceptingStates().contains(a) && !getAcceptingStates().contains(b)) ||
                    (getAcceptingStates().contains(b) && !getAcceptingStates().contains(a));
            markedPairs.put(pair, marked);
        }

        // Step 3: Mark pairs based on transitions
        boolean marked;
        do {
            // System.out.print("marked pairs: ");
            // System.out.println(markedPairs);
            marked = false;
            for (List<Set<Integer>> pair : statePairs) {
                if (!markedPairs.get(pair)) {
                    Set<Integer> a = pair.get(0);
                    Set<Integer> b = pair.get(1);
                    for (char symbol : sigma) {
                        Set<Set<Integer>> nextA = getSuccessors(a, symbol);
                        Set<Set<Integer>> nextB = getSuccessors(b, symbol);
                        if (!nextA.isEmpty() && !nextB.isEmpty()) {
                            List<Set<Integer>> nextPair = Arrays.asList(nextA.iterator().next(),
                                    nextB.iterator().next());
                            // add sanity check (next pair in keys we're interested in)
                            // why? before, it could happen (a, a) was a next pair, but we don't care about
                            // repeatred pairs.
                            if (markedPairs.keySet().contains(nextPair))
                                if (markedPairs.get(nextPair)) {
                                    markedPairs.put(pair, true);
                                    marked = true;
                                    break;
                                }
                        }
                    }
                }
            }
        } while (marked);

        // Step 4: Combine all unmarked pairs and make them a single state
        for (List<Set<Integer>> pair : statePairs) {
            if (!markedPairs.get(pair)) {
                Set<Integer> newState = new HashSet<>();
                newState.addAll(pair.get(0));
                newState.addAll(pair.get(1));
                mergeStates(newState, pair.get(0));
                mergeStates(newState, pair.get(1));
            }
        }
    }

}
