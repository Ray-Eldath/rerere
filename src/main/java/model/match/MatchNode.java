package model.match;

import java.util.*;
import java.util.stream.Collectors;

public class MatchNode {
    private final int id;
    private final Map<MatchOp, MatchNode> t;

    public MatchNode(Map<MatchOp, MatchNode> t) {
        this.t = t;
        this.id = Matches.Id++;
    }

    public boolean isTerminal() {
        return t.size() == 0;
    }

    private MatchNode retailNoCycle(List<Integer> visited, Map<MatchOp, MatchNode> newT) {
        if (isTerminal()) {
            t.putAll(newT);
            return null;
        }

        for (var v : t.values())
            if (!visited.contains(v.id)) {
                visited.add(v.id);
                v.retailNoCycle(visited, newT);
            }
        return this;
    }

    public MatchNode retail(Map<MatchOp, MatchNode> newT) {
        return retailNoCycle(new ArrayList<>(), newT);
    }

    private String toStringNoCycle(Set<Integer> visited) {
        if (t.size() == 0)
            return "END " + hashCode();
        return t.entrySet().stream().sorted(Comparator.comparingInt(x -> x.getValue().id())).map(entry -> {
            var target = entry.getValue();
            if (visited.contains(target.id))
                return String.format("%d %s ->\n\tCYCLE %s", id, entry.getKey(), target.id);
            visited.add(target.id);
            return String.format("%d %s ->\n\t%s", id, entry.getKey(), target.toStringNoCycle(visited));
        }).collect(Collectors.joining(",\n"));
    }

    @Override
    public String toString() {
        return toStringNoCycle(new HashSet<>());
    }

    public int id() {
        return id;
    }

    public Map<MatchOp, MatchNode> t() {
        return t;
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
