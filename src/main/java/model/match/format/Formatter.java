package model.match.format;

import model.match.MatchNode;
import model.match.op.MatchOp;

public interface Formatter {
    default String formatNode(MatchNode node) {
        return "";
    }

    default String formatCycleNode(MatchNode node) {
        return "";
    }

    String formatVertex(MatchNode origin, MatchOp matcher, MatchNode target);

    String separator();
}
