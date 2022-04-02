package model.re;

import model.match.MatchEpsilon;
import model.match.MatchNode;

import java.util.HashMap;
import java.util.Map;

public record Or(Op l, Op r) implements Op {
    @Override
    public MatchNode construct() {
        var terminal = new MatchNode(new HashMap<>());
        return new MatchNode(T(
                new MatchEpsilon(), l.construct().retail(T(new MatchEpsilon(), terminal)),
                new MatchEpsilon(), r.construct().retail(T(new MatchEpsilon(), terminal))));
    }

    public static void main(String[] args) {
        System.out.println(new Or(new Str("b"), new Str("c")).construct());
    }
}
