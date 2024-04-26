package automata.resyntax;

import automata.EpsNFA;

public class Closure extends RegExp {
    public final RegExp r;
    public Closure(RegExp r) {
        this.r = r;
    }

    public EpsNFA accept(RegExpVisitor visitor) {
        return visitor.visit(this);
    }
}
