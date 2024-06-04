package currex.parser;

import currex.lexer.Lexer;
import currex.parser.error.NotEndOfStreamException;
import currex.parser.error.ParserErrorHandler;
import currex.structure.table.*;
import currex.token.Position;
import currex.token.Token;
import currex.token.TokenType;
import currex.utils.CurrexConfig;

import java.util.ArrayList;
import java.util.List;

public class TableParser {
    private final Lexer lexer;
    private Token currentToken;
    private final ParserErrorHandler errorHandler;

    public TableParser(final Lexer lexer) throws Exception {
        this.lexer = lexer;
        this.errorHandler = new ParserErrorHandler();
        consumeToken();
    }

    public TableStatement parse() throws Exception {
        return parseTableStatement();
    }

    // tabela = {waluty, rząd_konwersji};
    private TableStatement parseTableStatement() throws Exception {
        List<ConversionRowExpression> tableRows = new ArrayList<>();
        CurrencyRowExpression currencyRow = parseCurrencyRow();
        ConversionRowExpression conversionRow = parseConversionRow();
        while (conversionRow.currencyName() != null) {
            tableRows.add(conversionRow);
            conversionRow = parseConversionRow();
        }
        if (!checkTokenType(TokenType.EOF)) {
            errorHandler.handleParserError(new NotEndOfStreamException("EXPECTED END OF STREAM!"),
                    new Position(currentToken.getPosition()));
            return null;
        }
        return new TableStatement(currencyRow, tableRows);
    }

    // waluty = {identyfikator}, ";";
    private CurrencyRowExpression parseCurrencyRow() throws Exception {
        List<CurrencyIdentifierExpression> columnCurrencies = new ArrayList<>();
        List<String> temporaryCurrencyList = new ArrayList<>();
        while (currentToken.getTokenType() == TokenType.IDENTIFIER) {
            CurrencyIdentifierExpression identifier = parseCurrencyIdentifier();
            if (temporaryCurrencyList.contains(identifier.name())) {
                System.out.println("CURRENCY ALREADY DEFINED IN A TABLE");
            }
            temporaryCurrencyList.add(identifier.name());
            columnCurrencies.add(identifier);
        }
        if (!consumeIf(TokenType.SEMICOLON)) {
            System.out.println("MISSING SEMICOLON AT THE END");
        }
        return new CurrencyRowExpression(columnCurrencies);
    }

    // rząd_konwersji = identyfikator, {przelicznik}, ";";
    private ConversionRowExpression parseConversionRow() throws Exception {
        List<ConversionRateExpression> rowConversionRates = new ArrayList<>();
        CurrencyIdentifierExpression identifier = parseCurrencyIdentifier();
        while (currentToken.getTokenType() == TokenType.INTEGER_VALUE ||
                currentToken.getTokenType() == TokenType.FLOAT_VALUE) {
            if (rowConversionRates.size() == CurrexConfig.MAX_CONVERSION_TABLE_SIZE) {
                System.out.println("TOO MANY CURRENCIES IN A ROW");
                break;
            }
            ConversionRateExpression conversionRate = parseConversionRate();
            rowConversionRates.add(conversionRate);
        }
        if (!consumeIf(TokenType.SEMICOLON) && !checkTokenType(TokenType.EOF)) {
            System.out.println("MISSING SEMICOLON AT THE END");
        }
        return new ConversionRowExpression(identifier, rowConversionRates);
    }

    // przelicznik = float;
    private ConversionRateExpression parseConversionRate() throws Exception {
        if (checkTokenType(TokenType.FLOAT_VALUE)) {
            Double floatValue = (Double) currentToken.getValue();
            consumeToken();
            return new ConversionRateExpression(floatValue);
        }
        else if (checkTokenType(TokenType.INTEGER_VALUE)) {
            Integer floatValue = (Integer) currentToken.getValue();
            consumeToken();
            return new ConversionRateExpression(Double.valueOf(floatValue));
        }
        System.out.println("INVALID CURRENCY RATE");
        return null;
    }

    private CurrencyIdentifierExpression parseCurrencyIdentifier() throws Exception {
        if (!checkTokenType(TokenType.IDENTIFIER)) {
            return null;
        }
        String name = (String) currentToken.getValue();
        consumeToken();
        return new CurrencyIdentifierExpression(name);
    }

    private void consumeToken() throws Exception {
        currentToken = lexer.fetchToken();
        while (currentToken.getTokenType() == TokenType.COMMENT) {
            currentToken = lexer.fetchToken();
        }
    }

    private boolean consumeIf(TokenType tokenType) throws Exception {
        if (checkTokenType(tokenType)) {
            consumeToken();
            return true;
        }
        return false;
    }

    private boolean checkTokenType(TokenType tokenType) {
        return currentToken.getTokenType() == tokenType;
    }
}
