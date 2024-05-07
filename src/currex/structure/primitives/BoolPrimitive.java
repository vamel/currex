package currex.structure.primitives;

import currex.visitor.Visitor;

public record BoolPrimitive(Boolean value) implements Primitive {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
