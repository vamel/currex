package currex.structure.statements;

import currex.structure.components.Block;
import currex.structure.expressions.Expression;
import currex.visitor.Visitor;

public record IfStatement(Expression expression, Block trueBlock, Block falseBlock) implements Statement {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
