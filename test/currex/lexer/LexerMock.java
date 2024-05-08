package currex.lexer;

import currex.source.Source;
import currex.token.Position;
import currex.token.Token;
import currex.token.TokenType;

import java.io.FileReader;
import java.util.List;

public class LexerMock extends Lexer {
    private final List<Token> tokenList;

    public LexerMock(List<Token> tokenList) throws Exception {
        super(new Source(new FileReader("resources/lexer/string_declaration.txt")));
        this.tokenList = tokenList;
    }

    public Token fetchToken() {
        if (!tokenList.isEmpty()) {
            Token token = tokenList.get(0);
            tokenList.remove(0);
            return token;
        }
        return new Token(new Position(), TokenType.EOF);
    }
}
