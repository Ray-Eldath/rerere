package model.re;

import model.match.MatchNode;
import model.match.MatchOp;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;

public sealed interface Op permits Closure, Concat, Or, Str {
    MatchNode construct();

    default String constructString() {
        return construct().toString();
    }

    default MatchNode terminal() {
        return new MatchNode(true);
    }

    default MutableMap<MatchOp, MatchNode> T(MatchOp k1, MatchNode v1) {
        return Maps.mutable.of(k1, v1);
    }

    default MutableMap<MatchOp, MatchNode> T(MatchOp k1, MatchNode v1, MatchOp k2, MatchNode v2) {
        return Maps.mutable.of(k1, v1, k2, v2);
    }
}
