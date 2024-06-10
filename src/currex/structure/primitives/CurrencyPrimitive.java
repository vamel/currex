package currex.structure.primitives;

import currex.visitor.Visitor;

import java.math.BigDecimal;

public class CurrencyPrimitive implements Primitive {
    private BigDecimal value;
    private String name;

    public CurrencyPrimitive(BigDecimal value, String name) {
        this.value = value;
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return value + " " + name;
    }
}
