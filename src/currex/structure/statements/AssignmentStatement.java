package currex.structure.statements;

import currex.structure.expressions.Expression;
import currex.visitor.Visitor;

public record AssignmentStatement(String name, Expression expression) implements Statement {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
