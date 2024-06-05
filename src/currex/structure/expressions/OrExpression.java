package currex.structure.expressions;

import currex.visitor.Visitor;

public record OrExpression(Expression left, Expression right) implements Expression {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
