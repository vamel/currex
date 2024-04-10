package currex.token;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void tokenGettersTest() {
        Position position = new Position();
        Token token = new Token(position, TokenType.IF);
        Assert.assertEquals(token.getTokenType(), TokenType.IF);
        Assert.assertEquals(token.getPosition().getRow(), 1);
        Assert.assertEquals(token.getPosition().getColumn(), 0);
    }

    @Test
    public void setPositionTest() {
        Position position = new Position();
        position.moveToNextRow();
        position.moveToNextColumn();
        Position position2 = new Position();
        Token token = new Token(position2, TokenType.ELSE);
        token.setPosition(position);
        Assert.assertEquals(token.getPosition().getRow(), 2);
        Assert.assertEquals(token.getPosition().getColumn(), 1);
    }

    @Test
    public void tokenToStringTest() {
        Position position = new Position();
        position.moveToNextRow();
        position.moveToNextColumn();
        Token token = new Token(position, TokenType.EXCLAMATION);
        Assert.assertEquals(token.toString(), "Token{tokenType=EXCLAMATION, row: 2, column: 1}");
    }

    @Test
    public void stringTokenTest() {
        StringToken token = new StringToken(new Position(), "value", TokenType.STRING_VALUE);
        Assert.assertEquals(token.getPosition().getRow(), 1);
        Assert.assertEquals(token.getPosition().getColumn(), 0);
        Assert.assertEquals(token.getValue(), "value");
        Assert.assertEquals(token.getTokenType(), TokenType.STRING_VALUE);
    }

    @Test
    public void stringTokenIdentifierTest() {
        StringToken token = new StringToken(new Position(), "id", TokenType.IDENTIFIER);
        Assert.assertEquals(token.getPosition().getRow(), 1);
        Assert.assertEquals(token.getPosition().getColumn(), 0);
        Assert.assertEquals(token.getValue(), "id");
        Assert.assertEquals(token.getTokenType(), TokenType.IDENTIFIER);
    }

    @Test
    public void stringTokenToStringTest() {
        StringToken token = new StringToken(new Position(), "value", TokenType.STRING_VALUE);
        Assert.assertEquals(token.toString(), "StringToken{tokenType=STRING_VALUE, row: 1, column: 0, value=value}");
    }

    @Test
    public void integerTokenTest() {
        IntegerToken token = new IntegerToken(new Position(), 10000);
        Assert.assertEquals(token.getTokenType(), TokenType.INTEGER_VALUE);
        Assert.assertEquals(token.getValue(), 10000);
    }

    @Test
    public void integerTokenToStringTest() {
        IntegerToken token = new IntegerToken(new Position(), 10000);
        Assert.assertEquals(token.toString(), "IntegerToken{tokenType=INTEGER_VALUE, row: 1, column: 0, value=10000}");
    }

    @Test
    public void floatTokenTest() {
        FloatToken token = new FloatToken(new Position(), 0.0067);
        Assert.assertEquals(token.getTokenType(), TokenType.FLOAT_VALUE);
        Assert.assertEquals(token.getValue(), 0.0067, 0.000001);
    }

    @Test
    public void floatTokenToStringTest() {
        FloatToken token = new FloatToken(new Position(), 0.0067);
        Assert.assertEquals(token.toString(), "FloatToken{tokenType=FLOAT_VALUE, row: 1, column: 0, value=0.0067}");
    }

    @Test
    public void currencyTokenTest() {
        CurrencyToken token = new CurrencyToken(new Position(), 0.0067, "ABC");
        Assert.assertEquals(token.getTokenType(), TokenType.CURRENCY_VALUE);
        Assert.assertEquals(token.getCurrencyType(), "ABC");
        Assert.assertEquals(token.getValue(), 0.0067, 0.000001);
    }

    @Test
    public void currencyTokenToStringTest() {
        CurrencyToken token = new CurrencyToken(new Position(), 0.0067, "ABC");
        Assert.assertEquals(token.toString(), "CurrencyToken{tokenType=CURRENCY_VALUE, row: 1, column: 0, value=" +
                "0.0067, currencyType='ABC'}");
    }
}
