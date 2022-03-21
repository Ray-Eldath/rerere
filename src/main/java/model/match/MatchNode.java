package model.match;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class MatchNode {
    private final long id;
    private final Map<MatchOp, MatchNode> t;

    public MatchNode(Map<MatchOp, MatchNode> t) {
        this.t = t;
        this.id = new Random().nextLong();
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

    public Map<MatchOp, MatchNode> t() {
        return t;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchNode that = (MatchNode) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        return t.size() == 0 ? "END " + hashCode() :
                t.entrySet().stream().map(entry -> String.format("%s ->\n\t%s", entry.getKey(), entry.getValue())).collect(Collectors.joining(",\n"));
    }
}
