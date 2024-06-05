package currex.structure.expressions;

import currex.visitor.Visitor;

public record NegationExpression(Expression expression) implements Expression {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
