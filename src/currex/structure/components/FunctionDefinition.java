package currex.structure.components;

import currex.structure.primitives.PrimitiveType;
import currex.visitor.Visitable;
import currex.visitor.Visitor;

import java.util.List;

public record FunctionDefinition(PrimitiveType returnType, String name,
                                 List<Parameter> parameters, Block block) implements Visitable {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
