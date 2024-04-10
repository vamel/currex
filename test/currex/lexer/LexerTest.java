package currex.lexer;

import currex.source.Source;
import currex.token.Token;
import currex.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

public class LexerTest {
    @Test
    public void createLexerTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/string_declaration.txt"));
        Lexer lexer = new Lexer(reader);
        Token token = lexer.fetchToken();
        Assert.assertNotNull(token);
    }

    @Test
    public void readEmptyFileTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/empty_file.txt"));
        Lexer lexer = new Lexer(reader);

        Token tokenUnknown = lexer.fetchToken();
        Assert.assertEquals(TokenType.UNKNOWN, tokenUnknown.getTokenType());
    }

    @Test
    public void readStringTokensTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/string_declaration.txt"));
        Lexer lexer = new Lexer(reader);

        Token tokenString = lexer.fetchToken();
        Assert.assertEquals(TokenType.STRING, tokenString.getTokenType());
        Assert.assertNull(tokenString.getValue());

        Token tokenIdentifier = lexer.fetchToken();
        Assert.assertNotNull(tokenIdentifier);
        Assert.assertEquals(TokenType.IDENTIFIER, tokenIdentifier.getTokenType());
        Assert.assertEquals("nazwa", tokenIdentifier.getValue());

        Token tokenEquals = lexer.fetchToken();
        Assert.assertNotNull(tokenEquals);
        Assert.assertEquals(TokenType.EQUALS, tokenEquals.getTokenType());
        Assert.assertNull(tokenEquals.getValue());

        Token tokenStringValue = lexer.fetchToken();
        Assert.assertNotNull(tokenStringValue);
        Assert.assertEquals(TokenType.STRING_VALUE, tokenStringValue.getTokenType());
        Assert.assertEquals("\"ABCDEF\"", tokenStringValue.getValue());

        Token tokenSemicolon = lexer.fetchToken();
        Assert.assertNotNull(tokenSemicolon);
        Assert.assertEquals(TokenType.SEMICOLON, tokenSemicolon.getTokenType());
        Assert.assertNull(tokenSemicolon.getValue());
    }

    @Test
    public void readBoolTokensTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/bool_declaration.txt"));
        Lexer lexer = new Lexer(reader);

        Token tokenBoolean1 = lexer.fetchToken();
        Assert.assertEquals(TokenType.BOOLEAN, tokenBoolean1.getTokenType());

        Token tokenIdentifier1 = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenIdentifier1.getTokenType());
        Assert.assertEquals("true_val", tokenIdentifier1.getValue());

        Token tokenEquals1 = lexer.fetchToken();
        Assert.assertEquals(TokenType.EQUALS, tokenEquals1.getTokenType());

        Token tokenTrue = lexer.fetchToken();
        Assert.assertEquals(TokenType.TRUE, tokenTrue.getTokenType());

        Token tokenSemicolon1 = lexer.fetchToken();
        Assert.assertEquals(TokenType.SEMICOLON, tokenSemicolon1.getTokenType());

        Token tokenBoolean2 = lexer.fetchToken();
        Assert.assertEquals(TokenType.BOOLEAN, tokenBoolean2.getTokenType());

        Token tokenIdentifier2 = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenIdentifier2.getTokenType());
        Assert.assertEquals("false_val", tokenIdentifier2.getValue());

        Token tokenEquals2 = lexer.fetchToken();
        Assert.assertEquals(TokenType.EQUALS, tokenEquals2.getTokenType());

        Token tokenFalse = lexer.fetchToken();
        Assert.assertEquals(TokenType.FALSE, tokenFalse.getTokenType());

        Token tokenSemicolon2 = lexer.fetchToken();
        Assert.assertEquals(TokenType.SEMICOLON, tokenSemicolon2.getTokenType());
    }

    @Test
    public void readIntegerTokensTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/integer_declaration.txt"));
        Lexer lexer = new Lexer(reader);

        Token tokenInt = lexer.fetchToken();
        Assert.assertEquals(TokenType.INTEGER, tokenInt.getTokenType());

        Token tokenIdentifier = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenIdentifier.getTokenType());
        Assert.assertEquals("divide_and_conquer", tokenIdentifier.getValue());

        Token tokenEquals = lexer.fetchToken();
        Assert.assertEquals(TokenType.EQUALS, tokenEquals.getTokenType());

        Token tokenInteger1 = lexer.fetchToken();
        Assert.assertEquals(TokenType.INTEGER_VALUE, tokenInteger1.getTokenType());
        Assert.assertEquals(50, tokenInteger1.getValue());

        Token tokenSlash = lexer.fetchToken();
        Assert.assertEquals(TokenType.SLASH, tokenSlash.getTokenType());

        Token tokenInteger2 = lexer.fetchToken();
        Assert.assertEquals(TokenType.INTEGER_VALUE, tokenInteger2.getTokenType());
        Assert.assertEquals(10, tokenInteger2.getValue());

        Token tokenSemicolon = lexer.fetchToken();
        Assert.assertEquals(TokenType.SEMICOLON, tokenSemicolon.getTokenType());

    }

    @Test
    public void readFloatTokensTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/float_declaration.txt"));
        Lexer lexer = new Lexer(reader);

        Token tokenFloat = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT, tokenFloat.getTokenType());

        Token tokenIdentifier = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenIdentifier.getTokenType());
        Assert.assertEquals("small_val", tokenIdentifier.getValue());

        Token tokenEquals = lexer.fetchToken();
        Assert.assertEquals(TokenType.EQUALS, tokenEquals.getTokenType());

        Token tokenFloatValue = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT_VALUE, tokenFloatValue.getTokenType());
        Assert.assertEquals(0.45, tokenFloatValue.getValue());

        Token tokenSemicolon = lexer.fetchToken();
        Assert.assertEquals(TokenType.SEMICOLON, tokenSemicolon.getTokenType());
    }

    @Test
    public void readCurrencyTokensTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/currency_declaration.txt"));
        Lexer lexer = new Lexer(reader);

        Token tokenCurrency = lexer.fetchToken();
        Assert.assertEquals(TokenType.CURRENCY, tokenCurrency.getTokenType());

        Token tokenIdentifier1 = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenIdentifier1.getTokenType());
        Assert.assertEquals("result", tokenIdentifier1.getValue());

        Token tokenEquals = lexer.fetchToken();
        Assert.assertEquals(TokenType.EQUALS, tokenEquals.getTokenType());

        Token tokenIdentifier2 = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenIdentifier2.getTokenType());
        Assert.assertEquals("some_function", tokenIdentifier2.getValue());

        Token tokenLeftParenthesis = lexer.fetchToken();
        Assert.assertEquals(TokenType.LEFT_PARENTHESIS, tokenLeftParenthesis.getTokenType());

        Token tokenCurrencyValue1 = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT_VALUE, tokenCurrencyValue1.getTokenType());
        Assert.assertEquals(5.00, tokenCurrencyValue1.getValue());

        Token tokenCurrencyName1 = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenCurrencyName1.getTokenType());
        Assert.assertEquals("EUR", tokenCurrencyName1.getValue());

        Token tokenComma = lexer.fetchToken();
        Assert.assertEquals(TokenType.COMMA, tokenComma.getTokenType());

        Token tokenCurrencyValue2 = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT_VALUE, tokenCurrencyValue2.getTokenType());
        Assert.assertEquals(7.50, tokenCurrencyValue2.getValue());

        Token tokenCurrencyName2 = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenCurrencyName2.getTokenType());
        Assert.assertEquals("EUR", tokenCurrencyName2.getValue());

        Token tokenRightParenthesis = lexer.fetchToken();
        Assert.assertEquals(TokenType.RIGHT_PARENTHESIS, tokenRightParenthesis.getTokenType());

        Token tokenSemicolon = lexer.fetchToken();
        Assert.assertEquals(TokenType.SEMICOLON, tokenSemicolon.getTokenType());
    }

    @Test
    public void readCommentTokensTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/comment.txt"));
        Lexer lexer = new Lexer(reader);

        Token tokenComment = lexer.fetchToken();
        Assert.assertEquals(TokenType.COMMENT, tokenComment.getTokenType());
        Assert.assertEquals("// this is a comment!", tokenComment.getValue());
    }
}
