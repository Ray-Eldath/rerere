package model.match;

import model.match.format.Formatter;
import model.match.format.Formatters;
import model.match.op.MatchChar;
import model.match.op.MatchEpsilon;
import model.match.op.MatchOp;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.SortedMaps;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.tuple.Pair;
import org.jetbrains.annotations.Contract;

import java.util.*;

public class MatchNode {
    private final int id;
    private final MutableMap<MatchOp, MatchNode> transitions;
    private boolean terminal = false;

    public MatchNode() {
        this(SortedMaps.mutable.empty());
    }

    public MatchNode(MatchNodeSet nodes) {
        this(nodes.terminal());
    }

    public MatchNode(Map<MatchOp, MatchNode> transitions) {
        this.transitions = Maps.mutable.ofMap(transitions);
        this.id = Matches.Id++;
    }

    public MatchNode(boolean terminal) {
        this(SortedMaps.mutable.empty());
        this.terminal = terminal;
    }

    @Contract(pure = true)
    public MatchNode accept(char c) {
        return transitions.get(new MatchChar(c));
    }

    @Contract(pure = true)
    public MatchNode accept(MatchOp op) {
        return transitions.get(op);
    }

    public void transfer(MatchOp op, MatchNode node) {
        transitions.put(op, node);
    }

    @Contract(pure = true)
    public MatchNodeSet epsilonClosure() {
        var closure = new MatchNodeSet(Set.of(this));
        transitions.keyValuesView().asLazy().select(p -> p.getOne() instanceof MatchEpsilon).collect(Pair::getTwo).forEach(node -> {
            closure.add(node);
            closure.addAll(node.epsilonClosure());
        });
        return closure;
    }

    private MatchNode retailRec(List<Integer> visited, Map<MatchOp, MatchNode> newTransitions) {
        if (isTerminal()) {
            transitions.putAll(newTransitions);
            return null;
        }

        transitions.valuesView().reject(t -> visited.contains(t.id)).forEach(t -> {
            visited.add(t.id);
            t.retailRec(visited, newTransitions);
        });
        return this;
    }

    public MatchNode retail(Map<MatchOp, MatchNode> newTransitions) {
        return retailRec(new ArrayList<>(), newTransitions);
    }

    private void nodesRec(List<MatchNode> nodes) {
        nodes.add(this);
        for (var v : transitions.values()) {
            if (!nodes.contains(v))
                v.nodesRec(nodes);
        }
    }

    public MutableList<MatchNode> nodes() {
        var nodes = Lists.mutable.<MatchNode>empty();
        nodesRec(nodes);
        return nodes;
    }

    public MatchGraph toMatchGraph(char... sigma) {
        return new MatchGraph(nodes(), this, sigma);
    }

    private String formatRec(Formatter f, Set<Integer> visited) {
        if (transitions.size() == 0) return f.formatNode(this);
        return transitions.keyValuesView().toSortedList(Comparator.comparingInt(e -> e.getTwo().id)).collect(entry -> {
            var matcher = entry.getOne();
            var target = entry.getTwo();
            if (visited.contains(target.id))
                return f.formatNode(this) + f.formatVertex(this, matcher, target) + f.formatCycleNode(target);
            visited.add(target.id);
            return f.formatNode(this) + f.formatVertex(this, matcher, target) + target.formatRec(f, visited);
        }).makeString(f.separator());
    }

    public String format(Formatter f) {
        return formatRec(f, new HashSet<>());
    }

    @Override
    public String toString() {
        return formatRec(Formatters.DEFAULT, new HashSet<>());
    }

    public boolean isTerminal() {
        return transitions.size() == 0 || terminal;
    }

    public int id() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchNode that = (MatchNode) o;
        return id == that.id;
    }
}
