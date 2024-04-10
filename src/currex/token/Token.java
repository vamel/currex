package currex.token;

public class Token {
    private Position position;
    private TokenType tokenType;
    private Object value;

    public Token(Position position, TokenType tokenType) {
        this.position = position;
        this.tokenType = tokenType;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Object getValue() {
        return null;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", " + position +
                '}';
    }
}
