package currex.table;

import currex.lexer.Lexer;
import currex.source.Source;
import currex.token.Position;
import currex.token.Token;
import currex.token.TokenType;

import java.io.FileReader;
import java.util.List;

public class LexerTableMock extends Lexer {
    private final List<Token> tokenList;

    public LexerTableMock(List<Token> tokenList) throws Exception {
        super(new Source(new FileReader("resources/lexer/currency_table.txt")));
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
