package currex.interpreter.builtin;

import currex.structure.statements.Statement;
import currex.visitor.Visitor;

public record PrintFunction() implements Statement {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
