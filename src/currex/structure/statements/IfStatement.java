package currex.structure.statements;

import currex.structure.components.Block;
import currex.structure.expressions.Expression;
import currex.visitor.Visitor;

import java.util.List;

public record IfStatement(Expression expression, Block trueBlock, List<ElseStatement> elseStatements) implements Statement {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
