package model.match.op;

public record MatchChar(char c) implements MatchOp {
    @Override
    public String toString() {
        return String.valueOf(c);
    }
}
