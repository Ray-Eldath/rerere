package model.match;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.jetbrains.annotations.Contract;

import java.util.Collection;

public class MatchNodeSet extends UnifiedSet<MatchNode> {
    private boolean terminal = false;

    public MatchNodeSet() {
        super();
    }

    public MatchNodeSet(Collection<? extends MatchNode> c) {
        super(c);
    }

    @Override
    public boolean add(MatchNode node) {
        if (node.isTerminal())
            terminal = true;
        return super.add(node);
    }

    @Contract(pure = true)
    public MatchNodeSet accept(char c) {
        var tos = new MatchNodeSet();
        for (var n : this) {
            var target = n.accept(c);
            if (target != null)
                tos.add(n.accept(c));
        }
        return tos;
    }

    @Contract(pure = true)
    public MatchNodeSet epsilonClosure() {
        var closure = new MatchNodeSet();
        for (var n : this)
            closure.addAll(n.epsilonClosure());
        return closure;
    }

    @Contract(pure = true)
    public ImmutableList<MatchNodeSet> split(char[] sigma) {
        for (var c : sigma) {
            var partitioned = this.partition(e -> {
                var accepted = e.accept(c);
                return accepted == null || this.contains(accepted);
            });
            if (partitioned.getSelected().notEmpty() && partitioned.getRejected().notEmpty())
                return Lists.immutable.of(new MatchNodeSet(partitioned.getSelected()), new MatchNodeSet(partitioned.getRejected()));
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
