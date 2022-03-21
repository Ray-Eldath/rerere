package model.match;

public record MatchChar(char c) implements MatchOp {
    @Override
    public String toString() {
        return String.valueOf(c);
    }
}
