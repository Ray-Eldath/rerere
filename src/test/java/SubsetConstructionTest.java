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
    void subset() {
        assertThat(new Concat(new Str("a"), new Closure(new Or(new Str("b"), new Str("c")))).construct().subset('a', 'b', 'c').toString()).isEqualTo("""
                13 a ->
                	14 END b ->
                	16 b ->
                	CYCLE END 16,
                16 END c ->
                	17 b ->
                	CYCLE END 16,
                17 c ->
                	CYCLE END 17,
                14 c ->
                	CYCLE END 17""");
    }

    @AfterEach
    void reset() {
        Matches.Id = 0;
    }
}
