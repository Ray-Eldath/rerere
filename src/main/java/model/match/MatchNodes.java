package model.match;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.jetbrains.annotations.Contract;

import java.util.Collection;

public class MatchNodes extends UnifiedSet<MatchNode> {
    private boolean terminal = false;

    public MatchNodes() {
        super();
    }

    public MatchNodes(Collection<? extends MatchNode> c) {
        super(c);
    }

    @Override
    public boolean add(MatchNode node) {
        if (node.isTerminal())
            terminal = true;
        return super.add(node);
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

    @Contract(pure = true)
    public MatchNodes epsilonClosure() {
        var closure = new MatchNodes();
        for (var n : this)
            closure.addAll(n.epsilonClosure());
        return closure;
    }

    @Contract(pure = true)
    public ImmutableList<MatchNodes> split(char[] sigma) {
        for (var c : sigma) {
            var partitioned = this.partition(e -> {
                var accepted = e.accept(c);
                return accepted == null || this.contains(accepted);
            });
            if (partitioned.getSelected().notEmpty() && partitioned.getRejected().notEmpty())
                return Lists.immutable.of(new MatchNodes(partitioned.getSelected()), new MatchNodes(partitioned.getRejected()));
        }
        return Lists.immutable.of(this);
    }

    public boolean terminal() {
        return terminal;
    }

    @Override
    public String toString() {
        return this.collect(MatchNode::id).makeString("{", ", ", "}");
    }
}
