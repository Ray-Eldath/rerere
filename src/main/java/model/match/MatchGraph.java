package model.match;

import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.OrderedMaps;
import org.jetbrains.annotations.Contract;

import java.util.LinkedList;
import java.util.List;

public record MatchGraph(MutableList<MatchNode> nodes, MatchNode start) {
    @Contract(pure = true)
    public MatchGraph subset(char... sigma) {
        var q0 = start.epsilonClosure();
        var WorkList = new LinkedList<>(List.of(q0));
        var start = new MatchNode();
        var Q = OrderedMaps.adapt(Maps.mutable.of(q0, start));
        while (!WorkList.isEmpty()) {
            var q = WorkList.pop();
            for (var c : sigma) {
                var t = q.accept(c).epsilonClosure();
                if (t.isEmpty())
                    continue;
                if (!Q.containsKey(t)) {
                    Q.put(t, new MatchNode(t));
                    WorkList.add(t);
                }
                Q.get(q).transfer(new MatchChar(c), Q.get(t));
            }
        }
        return new MatchGraph(Q.valuesView().toList(), start);
    }

    @Override
    public String toString() {
        return nodes.collect(MatchNode::id).makeString(" ") + "\n\n" + start.toString();
    }
}
