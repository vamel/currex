package currex.token;

public class FloatToken extends Token {

    private final Double value;

    public FloatToken(Position position, Double value) {
        super(position, TokenType.FLOAT);
        this.value = value;
    }

    public Double getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "FloatToken{" +
                "tokenType=" + this.getTokenType() +
                ", " + this.getPosition() +
                ", value=" + value + "}";
    }
}
