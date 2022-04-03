package model.match;

import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.SortedMaps;
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

    public MatchNode(MatchNodes nodes) {
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
    public MatchNode subset(char... sigma) {
        var q0 = epsilonClosure();
        var WorkList = new LinkedList<>(List.of(q0));
        var start = new MatchNode();
        var Q = new LinkedHashMap<>(Map.of(q0, start));
        while (!WorkList.isEmpty()) {
            var q = WorkList.pop();
            for (var c : sigma) {
                var t = q.accept(c).epsilonClosure();
                if (!Q.containsKey(t)) {
                    Q.put(t, new MatchNode(t));
                    WorkList.add(t);
                }
                if (!t.isEmpty()) Q.get(q).transitions.put(new MatchChar(c), Q.get(t));
            }
        }
        return start;
    }

    @Contract(pure = true)
    public MatchNode accept(char c) {
        return transitions.get(new MatchChar(c));
    }

    @Contract(pure = true)
    public MatchNode accept(MatchOp op) {
        return transitions.get(op);
    }

    @Contract(pure = true)
    public MatchNodes epsilonClosure() {
        var closure = new MatchNodes(Set.of(this));
        transitions.keyValuesView().asLazy().select(p -> p.getOne() instanceof MatchEpsilon).collect(Pair::getTwo).forEach(node -> {
            closure.add(node);
            closure.addAll(node.epsilonClosure());
        });
        return closure;
    }

    private MatchNode retailNoCycle(List<Integer> visited, Map<MatchOp, MatchNode> newT) {
        if (isTerminal()) {
            transitions.putAll(newT);
            return null;
        }

        transitions.valuesView().reject(t -> visited.contains(t.id)).forEach(t -> {
            visited.add(t.id);
            t.retailNoCycle(visited, newT);
        });
        return this;
    }

    public MatchNode retail(Map<MatchOp, MatchNode> newT) {
        return retailNoCycle(new ArrayList<>(), newT);
    }

    private String toStringNoCycle(Set<Integer> visited) {
        if (transitions.size() == 0) return "END " + hashCode();
        return transitions.keyValuesView().toSortedList(Comparator.comparingInt(e -> e.getTwo().id)).collect(entry -> {
            var matcher = entry.getOne();
            var target = entry.getTwo();
            if (visited.contains(target.id))
                return String.format("%d %s ->\n\tCYCLE %s%s", id, matcher, target.isTerminalString(), target.id);
            visited.add(target.id);
            return String.format("%d %s%s ->\n\t%s", id, isTerminalString(), matcher, target.toStringNoCycle(visited));
        }).makeString(",\n");
    }

    private String isTerminalString() {
        return isTerminal() ? "END " : "";
    }

    public boolean isTerminal() {
        return transitions.size() == 0 || terminal;
    }

    public int id() {
        return id;
    }

    @Override
    public String toString() {
        return toStringNoCycle(new HashSet<>());
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
