package model.re;

import model.match.MatchEpsilon;
import model.match.MatchNode;

import java.util.HashMap;
import java.util.Map;

public record Or(Op l, Op r) implements Op {
    @Override
    public MatchNode construct() {
        var terminal = new MatchNode(new HashMap<>());
        System.out.print("l: ");
        System.out.println(l.construct());
        System.out.print("r: ");
        System.out.println(r.construct());
        System.out.println();
        return new MatchNode(Map.of(
                new MatchEpsilon(), l.construct().retail(Map.of(new MatchEpsilon(), terminal)),
                new MatchEpsilon(), r.construct().retail(Map.of(new MatchEpsilon(), terminal))));
    }

    public static void main(String[] args) {
        System.out.println(new Or(new Str("a"), new Str("b")).construct());
    }
}
