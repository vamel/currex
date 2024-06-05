package currex.interpreter.builtin;

import currex.visitor.Visitor;

public record GetCurrencyMethod() implements Method {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
