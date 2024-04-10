package currex.lexer;

import currex.source.Source;
import currex.token.Token;
import currex.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class LexerTest {
    @Test
    public void createLexerTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/string_declaration.txt"));
        Lexer lexer = new Lexer(reader);
        Token token = lexer.fetchToken();
        Assert.assertNotNull(token);
    }

    @Test
    public void readCommentTokensTest() throws IOException {
        Source reader = new Source(new FileReader("resources/lexer/comment.txt"));
        Lexer lexer = new Lexer(reader);

        Token tokenComment = lexer.fetchToken();
        Assert.assertEquals(TokenType.COMMENT, tokenComment.getTokenType());
        Assert.assertEquals("// this is a comment!", tokenComment.getValue());
    }

    @Test
    public void readLeftBracketTest() throws IOException {
        Source reader = new Source(new StringReader("["));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void readRightBracketTest() throws IOException {
        Source reader = new Source(new StringReader("]"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
    }

    @Test
    public void readLeftCurlyBracketTest() throws IOException {
        Source reader = new Source(new StringReader("{"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LEFT_CURLY_BRACKET, token.getTokenType());
    }

    @Test
    public void readRightCurlyBracketTest() throws IOException {
        Source reader = new Source(new StringReader("}"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.RIGHT_CURLY_BRACKET, token.getTokenType());
    }

    @Test
    public void readLeftParenthesisTest() throws IOException {
        Source reader = new Source(new StringReader("("));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LEFT_PARENTHESIS, token.getTokenType());
    }

    @Test
    public void readRightParenthesisTest() throws IOException {
        Source reader = new Source(new StringReader(")"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.RIGHT_PARENTHESIS, token.getTokenType());
    }

    @Test
    public void readDotTest() throws IOException {
        Source reader = new Source(new StringReader("."));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.DOT, token.getTokenType());
    }

    @Test
    public void readCommaTest() throws IOException {
        Source reader = new Source(new StringReader(","));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void readSemicolonTest() throws IOException {
        Source reader = new Source(new StringReader(";"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.SEMICOLON, token.getTokenType());
    }

    @Test
    public void readLesserTest() throws IOException {
        Source reader = new Source(new StringReader("<"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LESSER, token.getTokenType());
    }

    @Test
    public void readGreaterTest() throws IOException {
        Source reader = new Source(new StringReader(">"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.GREATER, token.getTokenType());
    }

    @Test
    public void readExclamationTest() throws IOException {
        Source reader = new Source(new StringReader("!"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.EXCLAMATION, token.getTokenType());
    }

    @Test
    public void readEqualsTest() throws IOException {
        Source reader = new Source(new StringReader("="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void readAtTest() throws IOException {
        Source reader = new Source(new StringReader("@"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.AT, token.getTokenType());
    }

    @Test
    public void readPlusTest() throws IOException {
        Source reader = new Source(new StringReader("+"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void readMinusTest() throws IOException {
        Source reader = new Source(new StringReader("-"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.MINUS, token.getTokenType());
    }

    @Test
    public void readAsteriskTest() throws IOException {
        Source reader = new Source(new StringReader("*"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.ASTERISK, token.getTokenType());
    }

    @Test
    public void readSlashTest() throws IOException {
        Source reader = new Source(new StringReader("/"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void readLesserOrEqualTest() throws IOException {
        Source reader = new Source(new StringReader("<="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LESSER_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void readGreaterOrEqualTest() throws IOException {
        Source reader = new Source(new StringReader(">="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.GREATER_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void readInequalityTest() throws IOException {
        Source reader = new Source(new StringReader("!="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.INEQUALITY, token.getTokenType());
    }

    @Test
    public void readEqualityTest() throws IOException {
        Source reader = new Source(new StringReader("=="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.EQUALITY, token.getTokenType());
    }

    @Test
    public void readAndTest() throws IOException {
        Source reader = new Source(new StringReader("&&"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void readOrTest() throws IOException {
        Source reader = new Source(new StringReader("||"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.OR, token.getTokenType());
    }

    @Test
    public void readArrowTest() throws IOException {
        Source reader = new Source(new StringReader("->"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.ARROW, token.getTokenType());
    }

    @Test
    public void readWhileTest() throws IOException {
        Source reader = new Source(new StringReader("while"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.WHILE, token.getTokenType());
    }

    @Test
    public void readIfTest() throws IOException {
        Source reader = new Source(new StringReader("if"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.IF, token.getTokenType());
    }

    @Test
    public void readElseTest() throws IOException {
        Source reader = new Source(new StringReader("else"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.ELSE, token.getTokenType());
    }

    @Test
    public void readReturnTest() throws IOException {
        Source reader = new Source(new StringReader("return"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.RETURN, token.getTokenType());
    }

    @Test
    public void readIntTest() throws IOException {
        Source reader = new Source(new StringReader("int"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.INTEGER, token.getTokenType());
    }

    @Test
    public void readFloatTest() throws IOException {
        Source reader = new Source(new StringReader("float"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT, token.getTokenType());
    }

    @Test
    public void readCurrencyTest() throws IOException {
        Source reader = new Source(new StringReader("currency"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.CURRENCY, token.getTokenType());
    }

    @Test
    public void readBoolTest() throws IOException {
        Source reader = new Source(new StringReader("bool"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.BOOL, token.getTokenType());
    }

    @Test
    public void readStringTest() throws IOException {
        Source reader = new Source(new StringReader("string"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.STRING, token.getTokenType());
    }

    @Test
    public void readTrueTest() throws IOException {
        Source reader = new Source(new StringReader("true"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.TRUE, token.getTokenType());
    }

    @Test
    public void readFalseTest() throws IOException {
        Source reader = new Source(new StringReader("false"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FALSE, token.getTokenType());
    }

    @Test
    public void readIntValueTest() throws IOException {
        Source reader = new Source(new StringReader("578"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.INTEGER_VALUE, token.getTokenType());
        Assert.assertEquals(578, token.getValue());
    }

    @Test
    public void readIntValueMoreThanOneInitialZeroTest() throws IOException {
        Source reader = new Source(new StringReader("000000578"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.INTEGER_VALUE, token.getTokenType());
        Assert.assertEquals(578, token.getValue());
    }


    @Test
    public void readFloatValueTest() throws IOException {
        Source reader = new Source(new StringReader("5.08"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT_VALUE, token.getTokenType());
        Assert.assertEquals(5.08, token.getValue());
    }

    @Test
    public void readFloatValueInitialZeroTest() throws IOException {
        Source reader = new Source(new StringReader("0.08"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT_VALUE, token.getTokenType());
        Assert.assertEquals(0.08, token.getValue());
    }

    @Test
    public void readFloatValueMoreThanOneInitialZeroTest() throws IOException {
        Source reader = new Source(new StringReader("000.08"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT_VALUE, token.getTokenType());
        Assert.assertEquals(0.08, token.getValue());
    }

    @Test
    public void readStringValueTest() throws IOException {
        Source reader = new Source(new StringReader("\"Long String\""));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.STRING_VALUE, token.getTokenType());
        Assert.assertEquals("\"Long String\"", token.getValue());
    }

    @Test
    public void readSplitIdentifierTest() throws IOException {
        Source reader = new Source(new StringReader("iden-tifier"));
        Lexer lexer = new Lexer(reader);

        Token tokenIdentifier1 = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenIdentifier1.getTokenType());
        Assert.assertEquals("iden", tokenIdentifier1.getValue());

        Token tokenMinus = lexer.fetchToken();
        Assert.assertEquals(TokenType.MINUS, tokenMinus.getTokenType());

        Token tokenIdentifier2 = lexer.fetchToken();
        Assert.assertEquals(TokenType.IDENTIFIER, tokenIdentifier2.getTokenType());
        Assert.assertEquals("tifier", tokenIdentifier2.getValue());
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
        Assert.assertEquals(TokenType.BOOL, tokenBoolean1.getTokenType());

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
        Assert.assertEquals(TokenType.BOOL, tokenBoolean2.getTokenType());

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
    public void tooLongStringValueTest() throws IOException {
        Source reader = new Source(new StringReader("\"Lorem ipsum dolor sit amet, " +
                "consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. " +
                "Sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec qu\""));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.TOO_LONG_STRING_ERROR, token.getTokenType());
    }

    @Test
    public void tooLongIdentifierTest() throws IOException {
        Source reader = new Source(new StringReader("elementumelementumelementumelementumelementumelementumelementum" +
                "elementumelementumelementumelementumelementumelementumelementumelementumelementumelementumelementum"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.TOO_LONG_IDENTIFIER_ERROR, token.getTokenType());
    }

    @Test
    public void floatingPointErrorTest() throws IOException {
        Source reader = new Source(new StringReader("0.111111111111111111"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOATING_POINT_ERROR, token.getTokenType());
    }

    @Test
    public void tooBigIntErrorTest() throws IOException {
        Source reader = new Source(new StringReader("111111111111111111"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.TOO_BIG_INTEGER_ERROR, token.getTokenType());
    }

    @Test
    public void missingSecondPipeCharTest() throws IOException {
        Source reader = new Source(new StringReader("|"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.MISSING_SECOND_CHARACTER_ERROR, token.getTokenType());
    }

    @Test
    public void missingSecondAmpersandCharTest() throws IOException {
        Source reader = new Source(new StringReader("&"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.MISSING_SECOND_CHARACTER_ERROR, token.getTokenType());
    }
}


