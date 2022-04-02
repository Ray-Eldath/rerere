package model.re;

import model.match.MatchNode;
import model.match.MatchOp;

import java.util.HashMap;
import java.util.Map;

public sealed interface Op permits Closure, Concat, Or, Str {
    MatchNode construct();

    default String constructString() {
        return construct().toString();
    }

    default MatchNode terminal() {
        return new MatchNode(new HashMap<>());
    }

    default Map<MatchOp, MatchNode> T(MatchOp k1, MatchNode v1) {
        return new HashMap<>(Map.of(k1, v1));
    }

    default Map<MatchOp, MatchNode> T(MatchOp k1, MatchNode v1, MatchOp k2, MatchNode v2) {
        return new HashMap<>(Map.of(k1, v1, k2, v2));
    }
}
