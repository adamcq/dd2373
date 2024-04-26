package automata.resyntax;

import automata.EpsNFA;

public interface RegExpVisitor {
    public EpsNFA visit(Closure closure);
    public EpsNFA visit(Concatenation concat);
    public EpsNFA visit(Dot dot);
    public EpsNFA visit(Litteral litteral);
    public EpsNFA visit(OneOrMore oneOrMore);
    public EpsNFA visit(Union union);
    public EpsNFA visit(ZeroOrOne zeroOrOne);
}
