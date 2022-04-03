import model.match.Matches;
import model.re.Closure;
import model.re.Concat;
import model.re.Or;
import model.re.Str;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ThompsonConstructionTest {
    @Test
    void Str() {
        assertThat(new Str("ab").constructString()).isEqualTo("""
                3 a ->
                	2 ε ->
                	1 b ->
                	END 0""");
    }

    @Test
    void Concat() {
        assertThat(new Concat(new Str("a"), new Str("b")).constructString()).isEqualTo("""
                1 a ->
                	0 ε ->
                	4 b ->
                	END 3""");
    }

    @Test
    void Or() {
        assertThat(new Or(new Str("b"), new Str("c")).constructString()).isEqualTo("""
                7 ε ->
                	2 b ->
                	1 ε ->
                	END 0,
                7 ε ->
                	5 c ->
                	4 ε ->
                	CYCLE END 0""");
    }

    @Test
    void Closure() {
        assertThat(new Closure(new Str("a")).constructString()).isEqualTo("""
                4 ε ->
                	END 0,
                4 ε ->
                	2 a ->
                	1 ε ->
                	CYCLE END 0,
                1 ε ->
                	CYCLE 2""");
    }

    @Test
    void OrClosure() {
        assertThat(new Closure(new Or(new Str("b"), new Str("c"))).constructString()).isEqualTo("""
                9 ε ->
                	END 0,
                9 ε ->
                	8 ε ->
                	3 b ->
                	2 ε ->
                	1 ε ->
                	CYCLE END 0,
                1 ε ->
                	CYCLE 8,
                8 ε ->
                	6 c ->
                	5 ε ->
                	CYCLE 1""");
    }

    @Test
    void StrOrClosure() {
        assertThat(new Concat(new Str("a"), new Closure(new Or(new Str("b"), new Str("c")))).constructString()).isEqualTo("""
                1 a ->
                	0 ε ->
                	12 ε ->
                	END 3,
                12 ε ->
                	11 ε ->
                	6 b ->
                	5 ε ->
                	4 ε ->
                	CYCLE END 3,
                4 ε ->
                	CYCLE 11,
                11 ε ->
                	9 c ->
                	8 ε ->
                	CYCLE 4""");
    }

    @AfterEach
    void reset() {
        Matches.Id = 0;
    }
}
