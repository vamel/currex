package currex.table;

import currex.interpreter.builtin.ConversionTable;
import currex.lexer.LexerMock;
import currex.parser.TableParser;
import currex.parser.error.*;
import currex.structure.table.TableStatement;
import currex.token.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TableParserTest {

    @Test
    public void ParseOneCurrency() throws Exception {
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
        Assert.assertEquals(1, table.conversionRows().size());
    }

    @Test
    public void ParseThreeCurrencies() throws Exception {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 2.01),
                        new FloatToken(new Position(), 4.99),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.30),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 9.1),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.0),
                        new FloatToken(new Position(), 8.01),
                        new FloatToken(new Position(), 1.0),
                        new Token(new Position(), TokenType.SEMICOLON)
                        ));
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        TableStatement table = parser.parse();
        Assert.assertEquals(3, table.currencyRow().currencyNames().size());
        Assert.assertEquals(3, table.conversionRows().size());
    }

    @Test
    public void ThrowCurrencyAlreadyDeclaredException() throws Exception {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 2.01),
                        new FloatToken(new Position(), 4.99),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.30),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 9.1),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.0),
                        new FloatToken(new Position(), 8.01),
                        new FloatToken(new Position(), 1.0),
                        new Token(new Position(), TokenType.SEMICOLON)
                ));
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        DuplicateCurrencyDeclarationError e = Assert.assertThrows(DuplicateCurrencyDeclarationError.class, parser::parse);
    }

    @Test
    public void ThrowCurrencyNotDeclaredException() throws Exception {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 2.01),
                        new FloatToken(new Position(), 4.99),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.30),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 9.1),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "JPY", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.0),
                        new FloatToken(new Position(), 8.01),
                        new FloatToken(new Position(), 1.0),
                        new Token(new Position(), TokenType.SEMICOLON)
                ));
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        CurrencyNotDeclaredError e = Assert.assertThrows(CurrencyNotDeclaredError.class, parser::parse);
    }

    @Test
    public void ThrowInvalidCurrencyTableError() throws Exception {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "JPY", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 2.01),
                        new FloatToken(new Position(), 4.99),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.30),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 9.1),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.0),
                        new FloatToken(new Position(), 8.01),
                        new FloatToken(new Position(), 1.0),
                        new Token(new Position(), TokenType.SEMICOLON)
                ));
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        InvalidCurrencyTableError e = Assert.assertThrows(InvalidCurrencyTableError.class, parser::parse);
    }

    @Test
    public void ThrowInvalidCurrencyRateException() throws Exception {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 2.01),
                        new StringToken(new Position(), "a", TokenType.STRING_VALUE),
                        new FloatToken(new Position(), 4.99),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.30),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 9.1),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.0),
                        new FloatToken(new Position(), 8.01),
                        new FloatToken(new Position(), 1.0),
                        new Token(new Position(), TokenType.SEMICOLON)
                ));
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        InvalidCurrencyRateError e = Assert.assertThrows(InvalidCurrencyRateError.class, parser::parse);
    }

    @Test
    public void ThrowNegationNotAllowedError() throws Exception {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 2.01),
                        new Token(new Position(), TokenType.MINUS),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 4.99),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.30),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 9.1),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.0),
                        new FloatToken(new Position(), 8.01),
                        new FloatToken(new Position(), 1.0),
                        new Token(new Position(), TokenType.SEMICOLON)
                ));
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        NegationNotAllowedError e = Assert.assertThrows(NegationNotAllowedError.class, parser::parse);
    }

    @Test
    public void ThrowTooManyCurrencyRatesError() throws Exception {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "JPY", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "CAD", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 2.01),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 4.99),
                        new FloatToken(new Position(), 0.01),
                        new FloatToken(new Position(), 10.14),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.30),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 9.1),
                        new FloatToken(new Position(), 45.4),
                        new FloatToken(new Position(), 11.0),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "USD", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.0),
                        new FloatToken(new Position(), 8.01),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 9.0),
                        new FloatToken(new Position(), 4.0),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "JPY", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.0),
                        new FloatToken(new Position(), 8.01),
                        new FloatToken(new Position(), 5.0),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 4.0),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "CAD", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.0),
                        new FloatToken(new Position(), 8.01),
                        new FloatToken(new Position(), 5.0),
                        new FloatToken(new Position(), 5.51),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 81.0),
                        new Token(new Position(), TokenType.SEMICOLON)
                ));
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        TooManyCurrencyRatesError e = Assert.assertThrows(TooManyCurrencyRatesError.class, parser::parse);
    }

    @Test
    public void ThrowMissingSemicolonError() throws Exception {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 1.0),
                        new FloatToken(new Position(), 2.01),
                        new Token(new Position(), TokenType.COMMA),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.30),
                        new FloatToken(new Position(), 1.0),
                        new Token(new Position(), TokenType.SEMICOLON)
                ));
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        MissingSemicolonError e = Assert.assertThrows(MissingSemicolonError.class, parser::parse);
    }

    @Test
    public void ConversionRateNotEqualToOneError() throws Exception {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "EUR", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 1.1),
                        new FloatToken(new Position(), 2.01),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new StringToken(new Position(), "PLN", TokenType.IDENTIFIER),
                        new FloatToken(new Position(), 3.30),
                        new FloatToken(new Position(), 1.0),
                        new Token(new Position(), TokenType.SEMICOLON)
                ));
        LexerMock lexer = new LexerMock(tokenList);
        TableParser parser = new TableParser(lexer);
        TableStatement tableStatement = parser.parse();
        InvalidCurrencyRateError e = Assert.assertThrows(InvalidCurrencyRateError.class,
                () -> new ConversionTable(tableStatement));
    }
}
