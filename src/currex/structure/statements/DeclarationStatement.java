package currex.structure.statements;

import currex.structure.primitives.PrimitiveType;
import currex.structure.expressions.Expression;
import currex.visitor.Visitor;

public record DeclarationStatement(PrimitiveType type, String name, Expression expression) implements Statement {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
