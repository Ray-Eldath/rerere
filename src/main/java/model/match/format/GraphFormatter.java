package model.match.format;

import model.match.MatchNode;
import org.eclipse.collections.api.list.ImmutableList;

public interface GraphFormatter extends Formatter {
    String start(MatchNode start, ImmutableList<MatchNode> nodes);

    default String preamble() {
        return "";
    }

    default String epilogue() {
        return "";
    }
}
