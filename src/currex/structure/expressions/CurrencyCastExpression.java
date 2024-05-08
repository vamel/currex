package currex.structure.expressions;

import currex.visitor.Visitor;

public record CurrencyCastExpression(Expression left, Expression right) implements Expression {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
