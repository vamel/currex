package currex.structure.expressions;

import currex.visitor.Visitor;

public record NotEqualExpression(Expression left, Expression right) implements Expression {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
