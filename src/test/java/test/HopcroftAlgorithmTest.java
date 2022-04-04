package test;

import model.match.Matches;
import model.re.Closure;
import model.re.Concat;
import model.re.Or;
import model.re.Str;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HopcroftAlgorithmTest {
    @Test
    @DisplayName("fee|fie")
    void minify1() {
        var graph = new Concat(new Str("f"), new Or(new Str("ee"), new Str("ie"))).construct().toMatchGraph('e', 'f', 'i');
        Tests.graphviz(graph);
        assertThat(graph.subset().minify().toString()).isEqualTo("""
                21 22 23 24
                                
                22 f ->
                	23 e ->
                	24 e ->
                	END 21,
                23 i ->
                	CYCLE 24""");
    }

    @Test
    @DisplayName("a(b|c)*")
    void minify2() {
        var graph = new Concat(new Str("a"), new Closure(new Or(new Str("b"), new Str("c")))).construct().toMatchGraph('a', 'b', 'c');
        Tests.graphviz(graph);
        assertThat(graph.subset().minify().toString()).isEqualTo("""
                17 18
                                
                18 a ->
                	END 17 b ->
                	CYCLE END 17,
                END 17 c ->
                	CYCLE END 17""");
    }

    @AfterEach
    void reset() {
        Matches.Id = 0;
    }
}
