package model.re;

import model.match.MatchEpsilon;
import model.match.MatchNode;

public record Concat(Op a, Op b) implements Op {
    @Override
    public MatchNode construct() {
        return a.construct().retail(T(new MatchEpsilon(), b.construct()));
    }
}
