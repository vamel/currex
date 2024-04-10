package currex.token;

public class StringToken extends Token {

    private final String value;

    public StringToken(Position position, String value, TokenType tokenType) {
        super(position, tokenType);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return  "StringToken{" +
                "tokenType=" + getTokenType() +
                ", " + getPosition() +
                ", value=" + value + "}";
    }
}
