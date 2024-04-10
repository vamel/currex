package currex.token;

public class Token {
    private Position position;
    private TokenType tokenType;

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

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", " + position +
                '}';
    }
}
