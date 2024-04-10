package currex.token;

import java.math.BigDecimal;

public class CurrencyToken extends Token {
    private final BigDecimal value;
    private final String currencyType;

    public CurrencyToken(Position position, Double value, String currencyType) {
        super(position, TokenType.CURRENCY);
        this.value = BigDecimal.valueOf(value);
        this.currencyType = currencyType;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public String getCurrencyType() {
        return this.currencyType;
    }

    @Override
    public String toString() {
        return super.toString() + "CurrencyToken{" +
                "value=" + value +
                ", currencyType='" + currencyType + '\'' +
                '}';
    }
}
