package currex.token;

public class IntegerToken extends Token {

    private final Integer value;

    public IntegerToken(Position position, Integer value) {
        super(position, TokenType.INTEGER_VALUE);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IntegerToken{" +
                "tokenType=" + getTokenType() +
                ", " + getPosition() +
                ", value=" + value + "}";
    }
}
