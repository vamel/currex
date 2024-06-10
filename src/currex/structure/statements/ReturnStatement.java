package currex.structure.statements;

import currex.structure.expressions.Expression;
import currex.visitor.Visitor;

public record ReturnStatement(Expression expression) implements Statement {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
