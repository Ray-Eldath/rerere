package test;

import model.match.MatchGraph;
import model.re.Closure;
import model.re.Concat;
import model.re.Or;
import model.re.Str;
import org.assertj.core.api.ListAssert;
import org.eclipse.collections.impl.utility.internal.IteratorIterate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EvaluateTest {
    @Test
    @DisplayName("aaa(b|c)*")
    void evaluate1() {
        var graph = new Concat(new Str("aaa"), new Closure(new Or(new Str("b"), new Str("c")))).build('a', 'b', 'c');
        Tests.graphviz(graph);
        assertThis(graph, "a").isEmpty();
        assertThis(graph, "aaaa").containsExactly("aaa");
        assertThis(graph, "aaad").containsExactly("aaa");
        assertThis(graph, "aaaaa").containsExactly("aaa");
        assertThis(graph, "aaab").containsExactly("aaab");
        assertThis(graph, "aaaaaa").containsExactly("aaa", "aaa");
        assertThis(graph, "aaaaaab").containsExactly("aaa", "aaab");
        assertThis(graph, "aaaaaabaab").containsExactly("aaa", "aaab");
        assertThis(graph, "aaaaaabaaa").containsExactly("aaa", "aaab", "aaa");
        assertThis(graph, "aaaaaabaaac").containsExactly("aaa", "aaab", "aaac");
        assertThis(graph, "aaaaaabaaaca").containsExactly("aaa", "aaab", "aaac");
    }

    private ListAssert<String> assertThis(MatchGraph graph, String str) {
        return assertThat(drain(graph.evaluate(str)));
    }

    private <T> List<T> drain(Iterator<T> iterator) {
        return IteratorIterate.collect(iterator, id -> id, new ArrayList<>());
    }
}
