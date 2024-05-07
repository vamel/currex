package currex.structure.primitives;

import currex.visitor.Visitor;

import java.math.BigDecimal;

public record CurrencyPrimitive(BigDecimal value, String name) implements Primitive {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
