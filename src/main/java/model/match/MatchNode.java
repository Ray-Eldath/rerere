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

    public MatchNode retail(Map<MatchOp, MatchNode> newT) {
        if (isTerminal()) {
            t.putAll(newT);
            return null;
        }

        for (var v : t.values())
            v.retail(newT);
        return this;
    }

    String toStringNoCycle(List<Integer> visited) {
        if (t.size() == 0)
            return "END " + hashCode();
        return t.entrySet().stream().map(entry -> {
            var target = entry.getValue();
            if (visited.contains(target.id)) {
                return String.format("CYCLE %d %s", target.id, entry.getKey());
            }
            visited.add(target.id);
            return String.format("%d %s ->\n\t%s", id, entry.getKey(), target.toStringNoCycle(visited));
        }).collect(Collectors.joining(",\n"));
    }

    @Override
    public String toString() {
        return toStringNoCycle(new ArrayList<>());
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
