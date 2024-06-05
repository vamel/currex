package currex.interpreter.builtin;

import currex.visitor.Visitor;

public record GetBalanceMethod() implements Method {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
