package automata.resyntax;

import automata.EpsNFA;

public abstract class RegExp {
    public abstract EpsNFA accept(RegExpVisitor visitor); // input: RegExpVisitor, output: EpsNFA
}
