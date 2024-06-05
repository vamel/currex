package currex.table;

import currex.lexer.LexerMock;
import currex.parser.TableParser;
import currex.structure.table.TableStatement;
import currex.token.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TableParserTest {

    @Test
    public void ParseOneCurrency() throws Exception {
        // functionDef() {}
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 4.99))
        );
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        TableStatement table = parser.parse();
        Assert.assertEquals(1, table.currencyRow().currencyNames().size());
    }
}
