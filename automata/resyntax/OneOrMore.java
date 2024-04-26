package automata.resyntax;

import automata.EpsNFA;

public class OneOrMore extends RegExp {
    public final RegExp r;
    public OneOrMore(RegExp r) {
        this.r = r;
    }

    public EpsNFA accept(RegExpVisitor visitor) {
        return visitor.visit(this);
    }
}
