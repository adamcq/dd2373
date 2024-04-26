package automata;

import automata.resyntax.*;

import java.util.HashSet;

public class BuilderNFA implements RegExpVisitor {

    public HashSet<Character> sigma;
    public BuilderNFA(HashSet<Character> sigma) {
        this.sigma = sigma;
    }

    private void copyTransitionsNFA(EpsNFA from, EpsNFA to) {
        for (Integer src : from.trans.keySet())
            for (Integer dst : from.trans.get(src).keySet())
                to.addTransitions(src, dst, from.trans.get(src).get(dst));
//                for (Character c : from.trans.get(src).get(dst))
//                    to.addTransition(src, dst, c);
    }

    public EpsNFA visit(Closure closure) {
//        System.out.print("(");
        Integer initialState = EpsNFA.STATE_COUNTER++;
        EpsNFA epsNFA = closure.r.accept(this);
        Integer acceptingState = EpsNFA.STATE_COUNTER++;

        epsNFA.addTransition(epsNFA.getFirstAcceptingState(), epsNFA.getInitialState(), EpsNFA.EPSILON);
        epsNFA.addTransition(initialState, epsNFA.getInitialState(), EpsNFA.EPSILON);
        epsNFA.addTransition(epsNFA.getFirstAcceptingState(), acceptingState, EpsNFA.EPSILON);
        epsNFA.addTransition(initialState, acceptingState, EpsNFA.EPSILON);

        epsNFA.setInitialState(initialState);
        epsNFA.accepting = new HashSet<>();
        epsNFA.accepting.add(acceptingState);

//        System.out.print("*)");
        return epsNFA;
    }
    
    public EpsNFA visit(Concatenation concat) {
//        System.out.print("(");
        EpsNFA first = concat.r1.accept(this);
        EpsNFA second = concat.r2.accept(this);
//        System.out.print(")");

        // connect the last state of r1 automaton into first state of r2 automaton
        first.addTransition(first.getFirstAcceptingState(), second.getInitialState(), EpsNFA.EPSILON);
        copyTransitionsNFA(second, first);
        first.accepting = second.accepting;
        return first;
    }

    public EpsNFA visit(Dot dot) {
//        System.out.print(".");
        EpsNFA epsNFA = new EpsNFA();
        Integer acceptingState = EpsNFA.STATE_COUNTER++;

        // iterate over alphabet and add transition for each character
        for (Character c: sigma){
            epsNFA.addTransition(epsNFA.getInitialState(), acceptingState, c);
        }
        epsNFA.addAcceptingState(acceptingState);
        return epsNFA;
    }

    public EpsNFA visit(Litteral litteral) {
//        System.out.print(litteral.c);
        EpsNFA epsNFA = new EpsNFA();
        epsNFA.addTransition(epsNFA.getInitialState(), EpsNFA.STATE_COUNTER++, litteral.c);
        epsNFA.addAcceptingState(epsNFA.getMaxState());
        return epsNFA;
    }

    public EpsNFA visit(OneOrMore oneOrMore) {
//        System.out.print("(");

        Integer initialState = EpsNFA.STATE_COUNTER++;
        EpsNFA epsNFA = oneOrMore.r.accept(this);
        Integer acceptingState = EpsNFA.STATE_COUNTER++;

        epsNFA.addTransition(initialState, epsNFA.getInitialState(), EpsNFA.EPSILON);
        epsNFA.addTransition(epsNFA.getFirstAcceptingState(), epsNFA.getInitialState(), EpsNFA.EPSILON);
        epsNFA.addTransition(epsNFA.getFirstAcceptingState(), acceptingState, EpsNFA.EPSILON);
        epsNFA.setInitialState(initialState);
        epsNFA.accepting = new HashSet<>();
        epsNFA.accepting.add(acceptingState);

//        System.out.print("+)");
        return epsNFA;
    }

    public EpsNFA visit(Union union) {
//        System.out.print("(");
        EpsNFA epsNFA = new EpsNFA();
        EpsNFA first = union.r1.accept(this);
//        System.out.print("|");
        EpsNFA second = union.r2.accept(this);
        Integer acceptingState = EpsNFA.STATE_COUNTER++;

        epsNFA.addTransition(epsNFA.getInitialState(), first.getInitialState(), EpsNFA.EPSILON);
        epsNFA.addTransition(epsNFA.getInitialState(), second.getInitialState(), EpsNFA.EPSILON);

        epsNFA.addAcceptingState(acceptingState);
        epsNFA.addTransition(first.getFirstAcceptingState(), epsNFA.getFirstAcceptingState(), EpsNFA.EPSILON);
        epsNFA.addTransition(second.getFirstAcceptingState(), epsNFA.getFirstAcceptingState(), EpsNFA.EPSILON);

        copyTransitionsNFA(first, epsNFA);
        copyTransitionsNFA(second, epsNFA);

//        System.out.print(")");
        return epsNFA;
    }

    public EpsNFA visit(ZeroOrOne zeroOrOne) {
//        System.out.print("(");
        EpsNFA epsNFA = zeroOrOne.r.accept(this);

        epsNFA.addTransition(epsNFA.getInitialState(), epsNFA.getFirstAcceptingState(), EpsNFA.EPSILON);

//        System.out.print("?)");
        return epsNFA;
    }
    
}   
