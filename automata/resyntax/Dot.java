package automata.resyntax;

import automata.EpsNFA;

public class Dot extends RegExp {

    public EpsNFA accept(RegExpVisitor visitor) {
        return visitor.visit(this);
    }
    
}
