package currex.structure.primitives;

import currex.visitor.Visitor;

public record FloatPrimitive(Float value) implements Primitive {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
