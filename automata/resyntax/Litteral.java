package automata.resyntax;

import automata.EpsNFA;

public class Litteral extends RegExp {
    public final Character c;
    public Litteral(Character c) {
        this.c = c;
    }

    public EpsNFA accept(RegExpVisitor visitor) {
        return visitor.visit(this);
    }
}
