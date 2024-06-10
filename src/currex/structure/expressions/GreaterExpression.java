package currex.structure.expressions;

import currex.visitor.Visitor;

public record GreaterExpression(Expression left, Expression right) implements Expression {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
