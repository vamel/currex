package currex.structure.components;

import currex.visitor.Visitable;
import currex.visitor.Visitor;

import java.util.Map;

public record Program(Map<String, FunctionDefinition> functionDefinitions) implements Visitable {

    @Override
    public void accept(Visitor visitor) throws Exception {
        visitor.visit(this);
    }
}
