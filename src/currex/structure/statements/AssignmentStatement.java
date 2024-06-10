package currex.structure.statements;

import currex.structure.expressions.Expression;
import currex.visitor.Visitor;

public record AssignmentStatement(Expression left, Expression right) implements Statement {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
