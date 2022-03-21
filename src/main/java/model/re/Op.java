package model.re;

import model.match.MatchNode;

public sealed interface Op permits Str, Closure, Or {
    MatchNode construct();
}
