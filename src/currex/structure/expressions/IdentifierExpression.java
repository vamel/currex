package currex.structure.expressions;

import currex.visitor.Visitor;

public record IdentifierExpression(String name) implements Expression {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
