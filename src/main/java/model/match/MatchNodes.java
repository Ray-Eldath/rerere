package model.match;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

public class MatchNodes extends HashSet<MatchNode> {
    private boolean terminal = false;

    public MatchNodes() {
        super();
    }

    public MatchNodes(Set<? extends MatchNode> c) {
        super(c);
    }

    @Override
    public boolean add(MatchNode node) {
        if (node.isTerminal())
            terminal = true;
        return super.add(node);
    }

    @Contract(pure = true)
    public MatchNodes epsilonClosure() {
        var closure = new MatchNodes();
        for (var n : this)
            closure.addAll(n.epsilonClosure());
        return closure;
    }

    @Contract(pure = true)
    public MatchNodes accept(char c) {
        var tos = new MatchNodes();
        for (var n : this) {
            var target = n.accept(c);
            if (target != null)
                tos.add(n.accept(c));
        }
        return tos;
    }

    public boolean terminal() {
        return terminal;
    }
}
