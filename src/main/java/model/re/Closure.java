package model.re;

import model.match.MatchNode;

public record Closure(Op m) implements Op {
    @Override
    public MatchNode construct() {
        return null;
    }
}
