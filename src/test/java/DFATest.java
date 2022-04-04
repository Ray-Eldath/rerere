import model.match.MatchGraph;
import model.match.Matches;
import model.re.Closure;
import model.re.Concat;
import model.re.Or;
import model.re.Str;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.Tests;

public class DFATest {
    @Test
    @DisplayName("fee|fie")
    void all1() {
        graphvizAll(new Concat(new Str("f"), new Or(new Str("ee"), new Str("ie"))).construct().toMatchGraph('e', 'f', 'i'));
    }

    @Test
    @DisplayName("a(b|c)*")
    void all2() {
        graphvizAll(new Concat(new Str("a"), new Closure(new Or(new Str("b"), new Str("c")))).construct().toMatchGraph('a', 'b', 'c'));
    }

    @Test
    @DisplayName("aaa(b|c)*")
    void all3() {
        graphvizAll(new Concat(new Str("aaa"), new Closure(new Or(new Str("b"), new Str("c")))).construct().toMatchGraph('a', 'b', 'c'));
    }

    @Test
    @DisplayName("(ab)*|a(b|c)*")
    void all4() {
        var graph =
                new Or(new Closure(new Str("ab")),
                        new Concat(new Str("a"),
                                new Closure(new Or(new Str("b"), new Str("c"))))).construct().toMatchGraph('a', 'b', 'c');
        graphvizAll(graph);
    }

    private void graphvizAll(MatchGraph graph) {
        Matches.Id = 0;
        System.out.print("constructed: ");
        Tests.graphviz(graph);
        System.out.print("dfa: ");
        var subset = graph.subset();
        Matches.Id = 0;
        Tests.graphviz(subset);
        System.out.print("minimized:");
        Tests.graphviz(subset.minify());
        Matches.Id = 0;
    }
}
