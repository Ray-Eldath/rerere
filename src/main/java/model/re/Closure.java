package model.re;

import model.match.MatchEpsilon;
import model.match.MatchNode;

public record Closure(Op m) implements Op {
    @Override
    public MatchNode construct() {
        var q = terminal();
        var ij = m.construct();
        ij.retail(T(new MatchEpsilon(), q,
                new MatchEpsilon(), ij));
        return new MatchNode(T(new MatchEpsilon(), ij,
                new MatchEpsilon(), q));
    }
}
