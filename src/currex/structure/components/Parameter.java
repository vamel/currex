package currex.structure.components;

import currex.structure.primitives.PrimitiveType;
import currex.visitor.Visitable;
import currex.visitor.Visitor;

public record Parameter(PrimitiveType type, String name) implements Visitable {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
