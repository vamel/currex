package currex.token;

public class ErrorToken extends Token {
    public ErrorToken(Position position, TokenType tokenType) {
        super(position, tokenType);
    }

    @Override
    public String toString() {
        return  "ErrorToken{" +
                "ErrorType=" + getTokenType() +
                ", " + getPosition() + "}";
    }
}
