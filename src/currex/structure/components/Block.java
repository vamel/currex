package currex.structure.components;

import currex.structure.statements.Statement;
import currex.visitor.Visitable;
import currex.visitor.Visitor;

import java.util.List;

public record Block(List<Statement> statementList) implements Visitable {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
