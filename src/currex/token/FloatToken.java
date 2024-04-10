package currex.token;

public class FloatToken extends Token {

    private final double value;

    public FloatToken(Position position, double value) {
        super(position, TokenType.FLOAT_VALUE);
        this.value = value;
    }

    public double getValue() {
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
