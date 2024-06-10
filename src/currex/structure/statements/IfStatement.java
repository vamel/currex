package currex.structure.statements;

import currex.visitor.Visitor;

import java.util.List;

public record IfStatement(List<ElseStatement> conditionalStatements) implements Statement {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
