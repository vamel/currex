package currex.structure.expressions;

import currex.visitor.Visitor;

import java.util.List;

public record FunctionCallExpression(String name, List<Expression> arguments) implements Expression {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
