package currex.token;

public class IntegerToken extends Token {

    private final Integer value;

    public IntegerToken(Position position, Integer value) {
        super(position, TokenType.INTEGER);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IntegerToken{" +
                "tokenType=" + this.getTokenType() +
                ", " + this.getPosition() +
                ", value=" + value + "}";
    }
}
