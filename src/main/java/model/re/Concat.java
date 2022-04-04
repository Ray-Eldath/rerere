package model.re;

import model.match.MatchNode;
import model.match.op.MatchEpsilon;

public record Concat(Op a, Op b) implements Op {
    @Override
    public MatchNode construct() {
        return a.construct().retail(T(new MatchEpsilon(), b.construct()));
    }
}
