package currex.structure.primitives;

import currex.visitor.Visitor;

public record StringPrimitive(String value) implements Primitive {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return value();
    }
}
