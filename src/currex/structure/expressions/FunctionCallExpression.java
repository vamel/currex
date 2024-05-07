package currex.structure.expressions;

import currex.structure.components.Parameter;
import currex.visitor.Visitor;

import java.util.List;

public record FunctionCallExpression(String name, List<Parameter> arguments) implements Expression {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
