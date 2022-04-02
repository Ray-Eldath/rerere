package model.re;

import model.match.MatchEpsilon;
import model.match.MatchNode;

public record Closure(Op m) implements Op {
    @Override
    public MatchNode construct() {
        var q = terminal();
        var ij = m.construct();
        System.out.println("ij:\n" + ij);
        ij.retail(T(new MatchEpsilon(), q,
                new MatchEpsilon(), ij));
        return new MatchNode(T(new MatchEpsilon(), ij,
                new MatchEpsilon(), q));
    }

    public static void main(String[] args) {
        System.out.println(new Closure(new Or(new Str("b"), new Str("c"))).construct());
    }
}
