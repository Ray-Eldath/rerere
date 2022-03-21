package model.match;

import java.util.Objects;
import java.util.Random;

public final class MatchEpsilon implements MatchOp {
    private final long id;

    public MatchEpsilon() {
        id = new Random().nextLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchEpsilon that = (MatchEpsilon) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ε";
    }
}
