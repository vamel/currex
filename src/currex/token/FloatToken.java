package currex.token;

public class FloatToken extends Token {

    private final Double value;

    public FloatToken(Position position, Double value) {
        super(position, TokenType.FLOAT_VALUE);
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FloatToken{" +
                "tokenType=" + getTokenType() +
                ", " + getPosition() +
                ", value=" + value + "}";
    }
}
