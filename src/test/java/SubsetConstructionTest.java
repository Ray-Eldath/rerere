import model.match.MatchNode;
import model.match.Matches;
import model.re.Closure;
import model.re.Concat;
import model.re.Or;
import model.re.Str;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SubsetConstructionTest {
    @Test
    @DisplayName("epsilonClosure (b|c)*")
    void epsilonClosure() {
        assertThat(new Closure(new Or(new Str("b"), new Str("c"))).construct().epsilonClosure()).extracting(MatchNode::id)
                .containsExactly(0, 3, 6, 8, 9);
    }

    @Test
    @DisplayName("deltaEpsilonClosure a(b|c)*")
    void deltaEpsilonClosure() {
        var node = new Concat(new Str("a"), new Closure(new Or(new Str("b"), new Str("c")))).construct();
        var aClosure = node.accept('a').epsilonClosure();
        assertThat(aClosure).extracting(MatchNode::id).containsExactly(0, 3, 6, 9, 11, 12);
        assertThat(node.accept('b')).isNull();
        assertThat(node.accept('c')).isNull();

        assertThat(aClosure.accept('a').epsilonClosure()).extracting(MatchNode::id).isEmpty();
        assertThat(aClosure.accept('b').epsilonClosure()).extracting(MatchNode::id).containsExactly(3, 4, 5, 6, 9, 11);
        assertThat(aClosure.accept('c').epsilonClosure()).extracting(MatchNode::id).containsExactly(3, 4, 6, 8, 9, 11);
    }

    @Test
    @DisplayName("subset a(b|c)*")
    void subset1() {
        var graph = new Concat(new Str("a"), new Closure(new Or(new Str("b"), new Str("c")))).construct().toMatchGraph('a', 'b', 'c');
        Tests.graphviz(graph);
        assertThat(graph.subset().toString()).isEqualTo("""
                13 14 16 15
                                
                13 a ->
                	END 14 b ->
                	END 15 b ->
                	CYCLE END 15,
                END 15 c ->
                	END 16 b ->
                	CYCLE END 15,
                END 16 c ->
                	CYCLE END 16,
                END 14 c ->
                	CYCLE END 16""");
    }

    @Test
    @DisplayName("subset fee|fie")
    void subset2() {
        var graph = new Concat(new Str("f"), new Or(new Str("ee"), new Str("ie"))).construct().toMatchGraph('e', 'f', 'i');
        Tests.graphviz(graph);
        assertThat(graph.subset().toString()).isEqualTo("""
                15 16 17 18 20 19
                                
                15 f ->
                	16 e ->
                	17 e ->
                	END 19,
                16 i ->
                	18 e ->
                	END 20""");
    }

    @AfterEach
    void reset() {
        Matches.Id = 0;
    }
}
