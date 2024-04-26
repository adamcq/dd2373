package automata;

import automata.resyntax.*;

public class PrettyPrinter implements RegExpVisitor {
    
    public EpsNFA visit(Closure closure) {
        System.out.print("(");
        closure.r.accept(this);
        System.out.print("*)");
        return null;
    }
    
    public EpsNFA visit(Concatenation concat) {
        System.out.print("(");
        concat.r1.accept(this);
        concat.r2.accept(this);
        System.out.print(")");
        return null;
    }

    public EpsNFA visit(Dot dot) {
        System.out.print(".");
        return null;
    }

    public EpsNFA visit(Litteral litteral) {
        System.out.print(litteral.c);
        return null;
    }

    public EpsNFA visit(OneOrMore oneOrMore) {
        System.out.print("(");
        oneOrMore.r.accept(this);
        System.out.print("+)");
        return null;
    }

    public EpsNFA visit(Union union) {
        System.out.print("(");
        union.r1.accept(this);
        System.out.print("|");
        union.r2.accept(this);
        System.out.print(")");
        return null;
    }

    public EpsNFA visit(ZeroOrOne zeroOrOne) {
        System.out.print("(");
        zeroOrOne.r.accept(this);
        System.out.print("?)");
        return null;
    }
    
}   
