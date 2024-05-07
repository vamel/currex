package currex.structure.expressions;

import currex.visitor.Visitor;

public record BasicExpression(Expression expression) implements Expression {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
