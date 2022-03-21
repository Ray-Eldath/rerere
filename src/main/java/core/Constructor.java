package core;

import model.re.*;

public class Constructor {
    public void construct(Node n) {

    }

    public static void main(String[] args) {
        var constructor = new Constructor();
        constructor.construct(
                new Node(new Str("a"),
                        new Node(new Closure(
                                new Or(new Str("b"),
                                        new Str("c"))), null)));
    }
}
