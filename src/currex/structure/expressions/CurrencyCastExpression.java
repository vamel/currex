package currex.structure.expressions;

import currex.visitor.Visitor;

public record CurrencyCastExpression(Expression expression, String name) implements Expression {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
