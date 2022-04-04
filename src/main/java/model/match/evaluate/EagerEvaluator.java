package model.match.evaluate;

import model.match.MatchGraph;
import model.match.MatchNode;
import org.eclipse.collections.api.factory.Stacks;
import org.eclipse.collections.api.iterator.CharIterator;
import org.eclipse.collections.api.stack.MutableStack;

import java.util.Iterator;

public final class EagerEvaluator implements Iterator<String> {
    private final CharIterator chars;
    private final MatchNode start;
    private final MutableStack<String> terminalStack;
    private char peekChar;

    public EagerEvaluator(MatchGraph graph, CharIterator chars) {
        this.chars = chars;
        this.start = graph.start();
        this.terminalStack = Stacks.mutable.empty();
    }

    @Override
    public boolean hasNext() {
        StringBuilder str = new StringBuilder();
        MatchNode current = start;
        if (peekChar != '\u0000') {
            str.append(peekChar);
            current = current.accept(peekChar);
        }
        while (chars.hasNext()) {
            var c = (peekChar = chars.next());
            var accepted = current.accept(c);
            if (accepted == null)
                return terminalStack.notEmpty();
            str.append(c);
            if ((current = accepted).isTerminal()) {
                terminalStack.push(str.toString());
            }
        }
        return terminalStack.notEmpty();
    }

    @Override
    public String next() {
        var s = terminalStack.peek();
        terminalStack.clear();
        return s;
    }
}