package model.match;

import model.match.format.GraphFormatter;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.OrderedMaps;
import org.jetbrains.annotations.Contract;

import java.util.LinkedList;
import java.util.List;

public record MatchGraph(MutableList<MatchNode> nodes, MatchNode start, char[] sigma) {
    @Contract(pure = true)
    public MatchGraph subset() {
        var q0 = start.epsilonClosure();
        var WorkList = new LinkedList<>(List.of(q0));
        var start = new MatchNode();
        var Q = OrderedMaps.adapt(Maps.mutable.of(q0, start));
        while (!WorkList.isEmpty()) {
            var q = WorkList.pop();
            for (var c : sigma) {
                var t = q.accept(c).epsilonClosure();
                if (t.isEmpty()) continue;
                if (!Q.containsKey(t)) {
                    Q.put(t, new MatchNode(t));
                    WorkList.add(t);
                }
                Q.get(q).transfer(new MatchChar(c), Q.get(t));
            }
        }
        return new MatchGraph(Q.valuesView().toList(), start, sigma);
    }

    @Contract(pure = true)
    public MatchGraph minify() {
        var t = nodes.partition(MatchNode::isTerminal);
        var T = Lists.mutable.of(new MatchNodes(t.getSelected()), new MatchNodes(t.getRejected()));
        var P = Lists.mutable.<MatchNodes>empty();
        while (!P.equals(T)) {
            P = T;
            T = Lists.mutable.empty();
            for (var p : P)
                T.addAllIterable(p.split(sigma));
//            System.out.println(T);
        }

//        assert P.select(e -> e.contains(start)).notEmpty();
        var startIndex = P.detectIndex(e -> e.contains(start));
        var R = P.collect(p -> new MatchNode(p.allSatisfy(MatchNode::isTerminal)));
        for (var i = 0; i < P.size(); i++) {
            for (var c : sigma) {
                var accepted = P.get(i).getFirst().accept(c);
                if (accepted != null) {
                    var target = P.detectIndex(e -> e.contains(accepted));
                    R.get(i).transfer(new MatchChar(c), R.get(target));
                }
            }
        }
        return new MatchGraph(R, R.get(startIndex), sigma);
    }

    public String format(GraphFormatter f) {
        return f.preamble() + f.start(nodes.toImmutable()) + start.format(f) + f.epilogue();
    }

    @Override
    public String toString() {
        return nodes.collect(MatchNode::id).makeString(" ") + "\n\n" + start.toString();
    }
}
