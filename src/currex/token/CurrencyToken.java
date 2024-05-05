package currex.token;

import java.math.BigDecimal;

public class CurrencyToken extends Token {
    private final BigDecimal value;
    private final String currencyType;

    public CurrencyToken(Position position, Double value, String currencyType) {
        super(position, TokenType.CURRENCY_VALUE);
        this.value = BigDecimal.valueOf(value);
        this.currencyType = currencyType;
    }

    @Override
    public Double getValue() {
        return this.value.doubleValue();
    }

    public String getCurrencyType() {
        return this.currencyType;
    }

    @Override
    public String toString() {
        return "CurrencyToken{" +
                "tokenType=" + getTokenType() +
                ", " + getPosition() +
                ", value=" + getValue() +
                ", currencyType='" + getCurrencyType() + '\'' +
                '}';
    }
}
