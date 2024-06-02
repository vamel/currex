package currex.interpreter.builtin;

import java.math.BigDecimal;

public class Currency {
    private BigDecimal value;
    private String name;

    public Currency(BigDecimal value, String name) {
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
    public String toString() {
        return value.toString() + " " + name;
    }
}
