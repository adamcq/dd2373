package automata.resyntax;

import automata.EpsNFA;

public class ZeroOrOne extends RegExp {
    public final RegExp r;
    public ZeroOrOne(RegExp r) {
        this.r = r;
    }
    
    public EpsNFA accept(RegExpVisitor visitor) {
        return visitor.visit(this);
    }
}
