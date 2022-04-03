package model.re;

import model.match.MatchChar;
import model.match.MatchEpsilon;
import model.match.MatchNode;

public record Str(String s) implements Op {
    @Override
    public MatchNode construct() {
        MatchEpsilon epsilon = null;
        var start = new MatchNode();
        var cs = s.toCharArray();
        for (int i = cs.length - 1; i >= 0; i--) {
            epsilon = new MatchEpsilon();
            start = new MatchNode(T(epsilon,
                    new MatchNode(T(new MatchChar(cs[i]), start))));
        }
        return start.accept(epsilon);
    }
}