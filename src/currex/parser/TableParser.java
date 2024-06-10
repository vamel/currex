package currex.parser;

import currex.lexer.Lexer;
import currex.parser.error.*;
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
        List<String> declaredRowCurrencies = new ArrayList<>();
        List<String> declaredHeaderCurrencies = new ArrayList<>();
        CurrencyRowExpression currencyRow = parseCurrencyRow();
        for (CurrencyIdentifierExpression identifier : currencyRow.currencyNames()) {
            declaredHeaderCurrencies.add(identifier.name());
        }
        ConversionRowExpression conversionRow = parseConversionRow();
        while (conversionRow.currencyName() != null) {
            String currencyName = conversionRow.currencyName().name();
            if (declaredRowCurrencies.contains(currencyName)) {
                errorHandler.handleParserError(new DuplicateCurrencyDeclarationError("CURRENCY " +
                                currencyName + " IS ALREADY DEFINED IN A TABLE!"),
                        new Position(currentToken.getPosition()));
            }
            if (!declaredHeaderCurrencies.contains(currencyName)) {
                errorHandler.handleParserError(new CurrencyNotDeclaredError("CURRENCY " +
                                currencyName + " WAS NOT DEFINED IN A TABLE HEADER!"),
                        new Position(currentToken.getPosition()));
            }
            tableRows.add(conversionRow);
            declaredRowCurrencies.add(currencyName);
            conversionRow = parseConversionRow();
        }
        if (declaredHeaderCurrencies.size() != declaredRowCurrencies.size()) {
            errorHandler.handleParserError(new InvalidCurrencyTableError("HEADER CURRENCIES COUNT OF " +
                            declaredHeaderCurrencies.size() + " DOES NOT MATCH ROW CURRENCIES COUNT OF " +
                            declaredRowCurrencies.size() + "!"),
                    new Position(currentToken.getPosition()));
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
                errorHandler.handleParserError(new DuplicateCurrencyDeclarationError("CURRENCY " +
                                identifier.name() + " IS ALREADY DEFINED IN A TABLE!"),
                        new Position(currentToken.getPosition()));
            }
            temporaryCurrencyList.add(identifier.name());
            columnCurrencies.add(identifier);
        }
        if (!consumeIf(TokenType.SEMICOLON)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING SEMICOLON AT THE END OF THE LINE!"),
                    new Position(currentToken.getPosition()));
        }
        return new CurrencyRowExpression(columnCurrencies);
    }

    // rząd_konwersji = identyfikator, {przelicznik}, ";";
    private ConversionRowExpression parseConversionRow() throws Exception {
        List<ConversionRateExpression> rowConversionRates = new ArrayList<>();
        CurrencyIdentifierExpression identifier = parseCurrencyIdentifier();
        while (currentToken.getTokenType() == TokenType.INTEGER_VALUE ||
                currentToken.getTokenType() == TokenType.FLOAT_VALUE ||
                currentToken.getTokenType() == TokenType.MINUS) {
            if (rowConversionRates.size() == CurrexConfig.MAX_CONVERSION_TABLE_SIZE) {
                errorHandler.handleParserError(new TooManyCurrencyRatesError("TOO MANY CURRENCIES IN A ROW, " +
                                "EXPECTED " + CurrexConfig.MAX_CONVERSION_TABLE_SIZE + " BUT RECEIVED" +
                                (CurrexConfig.MAX_CONVERSION_TABLE_SIZE + 1) + "!"),
                        new Position(currentToken.getPosition()));
            }
            if (currentToken.getTokenType() == TokenType.MINUS) {
                errorHandler.handleParserError(new NegationNotAllowedError("NEGATION IS NOT ALLOWED!"),
                        new Position(currentToken.getPosition()));
            }
            ConversionRateExpression conversionRate = parseConversionRate();
            rowConversionRates.add(conversionRate);
        }
        if (currentToken.getTokenType() == TokenType.IDENTIFIER) {
            errorHandler.handleParserError(new InvalidCurrencyRateError("INVALID CURRENCY RATE " + currentToken.getValue()),
                    new Position(currentToken.getPosition()));
        }
        if (!consumeIf(TokenType.SEMICOLON) && !checkTokenType(TokenType.EOF)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING SEMICOLON AT THE END OF THE LINE!"),
                    new Position(currentToken.getPosition()));
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
        errorHandler.handleParserError(new InvalidCurrencyRateError("INVALID CURRENCY RATE " + currentToken.getValue()),
                new Position(currentToken.getPosition()));
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
