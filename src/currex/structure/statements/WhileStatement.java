package currex.structure.statements;

import currex.structure.components.Block;
import currex.structure.expressions.Expression;
import currex.visitor.Visitor;

public record WhileStatement(Expression expression, Block block) implements Statement {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
