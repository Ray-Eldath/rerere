package model.re;

import model.match.MatchChar;
import model.match.MatchEpsilon;
import model.match.MatchNode;

import java.util.HashMap;
import java.util.Map;

public record Str(String s) implements Op {
    @Override
    public MatchNode construct() {
        MatchEpsilon epsilon = null;
        var start = new MatchNode(new HashMap<>());
        var cs = s.toCharArray();
        for (int i = cs.length - 1; i >= 0; i--) {
            epsilon = new MatchEpsilon();
            start = new MatchNode(Map.of(epsilon,
                    new MatchNode(Map.of(new MatchChar(cs[i]), start))));
        }
        return start.t().get(epsilon);
    }
}