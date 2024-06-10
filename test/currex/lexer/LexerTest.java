package currex.lexer;

import currex.lexer.error.*;
import currex.source.Source;
import currex.token.Token;
import currex.token.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.StringReader;

public class LexerTest {
    @Test
    public void createLexerTest() throws Exception {
        Source reader = new Source(new FileReader("resources/lexer/string_declaration.txt"));
        Lexer lexer = new Lexer(reader);
        Token token = lexer.fetchToken();
        Assert.assertNotNull(token);
    }

    @Test
    public void readCommentTokensTest() throws Exception {
        Source reader = new Source(new FileReader("resources/lexer/comment.txt"));
        Lexer lexer = new Lexer(reader);

        Token tokenComment = lexer.fetchToken();
        Assert.assertEquals(TokenType.COMMENT, tokenComment.getTokenType());
        Assert.assertEquals("// this is a comment!", tokenComment.getValue());
    }

    @Test
    public void readLeftBracketTest() throws Exception {
        Source reader = new Source(new StringReader("["));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
    }

    @Test
    public void readRightBracketTest() throws Exception {
        Source reader = new Source(new StringReader("]"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
    }

    @Test
    public void readLeftCurlyBracketTest() throws Exception {
        Source reader = new Source(new StringReader("{"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LEFT_CURLY_BRACKET, token.getTokenType());
    }

    @Test
    public void readRightCurlyBracketTest() throws Exception {
        Source reader = new Source(new StringReader("}"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.RIGHT_CURLY_BRACKET, token.getTokenType());
    }

    @Test
    public void readLeftParenthesisTest() throws Exception {
        Source reader = new Source(new StringReader("("));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LEFT_PARENTHESIS, token.getTokenType());
    }

    @Test
    public void readRightParenthesisTest() throws Exception {
        Source reader = new Source(new StringReader(")"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.RIGHT_PARENTHESIS, token.getTokenType());
    }

    @Test
    public void readDotTest() throws Exception {
        Source reader = new Source(new StringReader("."));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.DOT, token.getTokenType());
    }

    @Test
    public void readCommaTest() throws Exception {
        Source reader = new Source(new StringReader(","));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.COMMA, token.getTokenType());
    }

    @Test
    public void readSemicolonTest() throws Exception {
        Source reader = new Source(new StringReader(";"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.SEMICOLON, token.getTokenType());
    }

    @Test
    public void readLesserTest() throws Exception {
        Source reader = new Source(new StringReader("<"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LESSER, token.getTokenType());
    }

    @Test
    public void readGreaterTest() throws Exception {
        Source reader = new Source(new StringReader(">"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.GREATER, token.getTokenType());
    }

    @Test
    public void readExclamationTest() throws Exception {
        Source reader = new Source(new StringReader("!"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.EXCLAMATION, token.getTokenType());
    }

    @Test
    public void readEqualsTest() throws Exception {
        Source reader = new Source(new StringReader("="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.EQUALS, token.getTokenType());
    }

    @Test
    public void readAtTest() throws Exception {
        Source reader = new Source(new StringReader("@"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.AT, token.getTokenType());
    }

    @Test
    public void readPlusTest() throws Exception {
        Source reader = new Source(new StringReader("+"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.PLUS, token.getTokenType());
    }

    @Test
    public void readMinusTest() throws Exception {
        Source reader = new Source(new StringReader("-"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.MINUS, token.getTokenType());
    }

    @Test
    public void readAsteriskTest() throws Exception {
        Source reader = new Source(new StringReader("*"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.ASTERISK, token.getTokenType());
    }

    @Test
    public void readSlashTest() throws Exception {
        Source reader = new Source(new StringReader("/"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.SLASH, token.getTokenType());
    }

    @Test
    public void readLesserOrEqualTest() throws Exception {
        Source reader = new Source(new StringReader("<="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.LESSER_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void readGreaterOrEqualTest() throws Exception {
        Source reader = new Source(new StringReader(">="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.GREATER_OR_EQUAL, token.getTokenType());
    }

    @Test
    public void readInequalityTest() throws Exception {
        Source reader = new Source(new StringReader("!="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.INEQUALITY, token.getTokenType());
    }

    @Test
    public void readEqualityTest() throws Exception {
        Source reader = new Source(new StringReader("=="));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.EQUALITY, token.getTokenType());
    }

    @Test
    public void readAndTest() throws Exception {
        Source reader = new Source(new StringReader("&&"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.AND, token.getTokenType());
    }

    @Test
    public void readOrTest() throws Exception {
        Source reader = new Source(new StringReader("||"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.OR, token.getTokenType());
    }

    @Test
    public void readArrowTest() throws Exception {
        Source reader = new Source(new StringReader("->"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.ARROW, token.getTokenType());
    }

    @Test
    public void readWhileTest() throws Exception {
        Source reader = new Source(new StringReader("while"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.WHILE, token.getTokenType());
    }

    @Test
    public void readIfTest() throws Exception {
        Source reader = new Source(new StringReader("if"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.IF, token.getTokenType());
    }

    @Test
    public void readElseTest() throws Exception {
        Source reader = new Source(new StringReader("else"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.ELSE, token.getTokenType());
    }

    @Test
    public void readReturnTest() throws Exception {
        Source reader = new Source(new StringReader("return"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.RETURN, token.getTokenType());
    }

    @Test
    public void readIntTest() throws Exception {
        Source reader = new Source(new StringReader("int"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.INTEGER, token.getTokenType());
    }

    @Test
    public void readFloatTest() throws Exception {
        Source reader = new Source(new StringReader("float"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT, token.getTokenType());
    }

    @Test
    public void readCurrencyTest() throws Exception {
        Source reader = new Source(new StringReader("currency"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.CURRENCY, token.getTokenType());
    }

    @Test
    public void readBoolTest() throws Exception {
        Source reader = new Source(new StringReader("bool"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.BOOL, token.getTokenType());
    }

    @Test
    public void readStringTest() throws Exception {
        Source reader = new Source(new StringReader("string"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.STRING, token.getTokenType());
    }

    @Test
    public void readTrueTest() throws Exception {
        Source reader = new Source(new StringReader("true"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.TRUE, token.getTokenType());
    }

    @Test
    public void readFalseTest() throws Exception {
        Source reader = new Source(new StringReader("false"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FALSE, token.getTokenType());
    }

    @Test
    public void readIntValueTest() throws Exception {
        Source reader = new Source(new StringReader("578"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.INTEGER_VALUE, token.getTokenType());
        Assert.assertEquals(578, token.getValue());
    }

    @Test
    public void readIntValueMoreThanOneInitialZeroTest() throws Exception {
        Source reader = new Source(new StringReader("000000578"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.INTEGER_VALUE, token.getTokenType());
        Assert.assertEquals(578, token.getValue());
    }


    @Test
    public void readFloatValueTest() throws Exception {
        Source reader = new Source(new StringReader("5.08"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT_VALUE, token.getTokenType());
        Assert.assertEquals(5.08, token.getValue());
    }

    @Test
    public void readFloatValueInitialZeroTest() throws Exception {
        Source reader = new Source(new StringReader("0.08"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT_VALUE, token.getTokenType());
        Assert.assertEquals(0.08, token.getValue());
    }

    @Test
    public void readFloatValueMoreThanOneInitialZeroTest() throws Exception {
        Source reader = new Source(new StringReader("000.08"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.FLOAT_VALUE, token.getTokenType());
        Assert.assertEquals(0.08, token.getValue());
    }

    @Test
    public void readStringValueTest() throws Exception {
        Source reader = new Source(new StringReader("\"Long String\""));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.STRING_VALUE, token.getTokenType());
        Assert.assertEquals("\"Long String\"", token.getValue());
    }

    @Test
    public void readSplitIdentifierTest() throws Exception {
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
    public void readEmptyFileTest() throws Exception {
        Source reader = new Source(new FileReader("resources/lexer/empty_file.txt"));
        Lexer lexer = new Lexer(reader);

        Token token = lexer.fetchToken();
        Assert.assertEquals(TokenType.EOF, token.getTokenType());
    }

    @Test
    public void readStringTokensTest() throws Exception {
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
    public void readBoolTokensTest() throws Exception {
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
    public void readIntegerTokensTest() throws Exception {
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
    public void readFloatTokensTest() throws Exception {
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
    public void readCurrencyTokensTest() throws Exception {
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
    public void tooLongStringValueTest() throws Exception {
        Source reader = new Source(new StringReader("\"Lorem ipsum dolor sit amet, " +
                "consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. " +
                "Sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec qu\""));
        Lexer lexer = new Lexer(reader);

        StringTooLongError e = Assert.assertThrows(StringTooLongError.class, lexer::fetchToken);
        Assert.assertTrue(e.getMessage().contains("TOO LONG STRING!"));
    }

    @Test
    public void tooLongIdentifierTest() throws Exception {
        Source reader = new Source(new StringReader("elementumelementumelementumelementumelementumelementumelementum" +
                "elementumelementumelementumelementumelementumelementumelementumelementumelementumelementumelementum"));
        Lexer lexer = new Lexer(reader);

        IdentifierTooLongError e = Assert.assertThrows(IdentifierTooLongError.class, lexer::fetchToken);
        Assert.assertTrue(e.getMessage().contains("TOO LONG IDENTIFIER"));
    }

    @Test
    public void floatingPointErrorTest() throws Exception {
        Source reader = new Source(new StringReader("0.111111111111111111"));
        Lexer lexer = new Lexer(reader);

        FloatingPointError e = Assert.assertThrows(FloatingPointError.class, lexer::fetchToken);
        Assert.assertTrue(e.getMessage().contains("FLOAT NUMBER IS TOO BIG!"));
    }

    @Test
    public void tooBigIntErrorTest() throws Exception {
        Source reader = new Source(new StringReader("111111111111111111"));
        Lexer lexer = new Lexer(reader);

        TooBigIntegerError e = Assert.assertThrows(TooBigIntegerError.class, lexer::fetchToken);
        Assert.assertTrue(e.getMessage().contains("INTEGER IS TOO BIG!"));
    }

    @Test
    public void missingSecondPipeCharTest() throws Exception {
        Source reader = new Source(new StringReader("|"));
        Lexer lexer = new Lexer(reader);

        MissingSecondCharacterError e = Assert.assertThrows(MissingSecondCharacterError.class, lexer::fetchToken);
        Assert.assertTrue(e.getMessage().contains("MISSING SECOND CHARACTER ERROR!"));
    }

    @Test
    public void missingSecondAmpersandCharTest() throws Exception {
        Source reader = new Source(new StringReader("&"));
        Lexer lexer = new Lexer(reader);

        MissingSecondCharacterError e = Assert.assertThrows(MissingSecondCharacterError.class, lexer::fetchToken);
        Assert.assertTrue(e.getMessage().contains("MISSING SECOND CHARACTER ERROR!"));
    }
}


