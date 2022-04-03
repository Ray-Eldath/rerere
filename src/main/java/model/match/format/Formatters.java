package model.match.format;

import model.match.MatchNode;
import model.match.MatchOp;
import org.eclipse.collections.api.list.ImmutableList;

public class Formatters {
    public static final Formatter DEFAULT = new Formatter() {
        @Override
        public String formatNode(MatchNode node) {
            return (node.isTerminal() ? "END " : "") + node.id();
        }

        @Override
        public String formatCycleNode(MatchNode node) {
            return "CYCLE " + formatNode(node);
        }

        @Override
        public String formatVertex(MatchNode origin, MatchOp matcher, MatchNode target) {
            return String.format(" %s ->\n\t", matcher);
        }

        @Override
        public String separator() {
            return ",\n";
        }
    };

    public static final GraphFormatter GRAPHVIZ = new GraphFormatter() {
        @Override
        public String formatVertex(MatchNode origin, MatchOp matcher, MatchNode target) {
            return String.format("""
                      %d -> %d [label="%s"]
                    """, origin.id(), target.id(), matcher);
        }

        @Override
        public String start(ImmutableList<MatchNode> nodes) {
            return nodes.select(MatchNode::isTerminal).collect(n -> String.format("""
                      %d [shape="doublecircle"]
                    """, n.id())).makeString("", "", "\n");
        }

        @Override
        public String preamble() {
            return """
                    digraph G {
                      node [shape=circle]
                      rankdir="LR"
                    """;
        }

        @Override
        public String epilogue() {
            return "}";
        }

        @Override
        public String separator() {
            return "\n";
        }
    };

}
