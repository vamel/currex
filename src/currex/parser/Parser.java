package currex.parser;

import currex.lexer.Lexer;
import currex.parser.error.*;
import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.CurrencyPrimitive;
import currex.structure.primitives.PrimitiveType;
import currex.structure.statements.AssignmentStatement;
import currex.structure.statements.ReturnStatement;
import currex.structure.statements.Statement;
import currex.token.Position;
import currex.token.Token;
import currex.token.TokenType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private ParserErrorHandler errorHandler;

    public Parser(final Lexer lexer) throws Exception {
        this.lexer = lexer;
        this.errorHandler = new ParserErrorHandler();
        consumeToken();
    }

    // program = {definicja_funkcji};
    public Program parse() throws Exception {
        Map<String, FunctionDefinition> functionDefinitions = new HashMap<>();
        FunctionDefinition newFunction = parseFunctionDefinition(functionDefinitions);
        while (newFunction != null) {
            functionDefinitions.put(newFunction.name(), newFunction);
            newFunction = parseFunctionDefinition(functionDefinitions);
        }
        if (!checkTokenType(TokenType.EOF)) {
            errorHandler.handleParserError(new NotEndOfStreamException("EXPECTED END OF STREAM!"),
                    new Position(currentToken.getPosition()));
            return null;
        }
        return new Program(functionDefinitions);
    }

    // definicja_funkcji  = [typ], nazwa_funkcji, "(", [lista_parametrów], ")", blok;
    private FunctionDefinition parseFunctionDefinition(Map<String, FunctionDefinition> functionDefinitions) throws Exception {
        PrimitiveType functionReturnType = checkPrimitiveType(currentToken.getTokenType());
        if (functionReturnType != PrimitiveType.NONE) {
            consumeToken();
        }
        if (checkTokenType(TokenType.EOF)) {
            return null;
        }
        if (!checkTokenType(TokenType.IDENTIFIER)) {
            errorHandler.handleParserError(new InvalidIdentifierError("INVALID FUNCTION NAME!"),
                    new Position(currentToken.getPosition()));
        }
        String name = (String) currentToken.getValue();
        if (functionDefinitions.containsKey(name)) {
            errorHandler.handleParserError(new DuplicateDeclaration("FUNCTION WITH THIS NAME WAS ALREADY DEFINED!"),
                    new Position(currentToken.getPosition()));
            return null;
        }
        consumeToken();
        if (!consumeIf(TokenType.LEFT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING OPENING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
            return null;
        }
        List<Parameter> functionParameters = parseParameterList();
        if (!consumeIf(TokenType.RIGHT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING CLOSING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
            return null;
        }
        Block block = parseBlock();
        return new FunctionDefinition(functionReturnType, name, functionParameters, block);
    }

    // lista_parametrów = parametr_funkcji, {",", parametr_funkcji};
    private List<Parameter> parseParameterList() throws Exception {
        List<Parameter> parameterList = new ArrayList<>();
        List<String> parameterNames = new ArrayList<>();
        Parameter firstParameter = parseParameter();
        if (firstParameter == null) {
            return parameterList;
        }
        parameterList.add(firstParameter);
        parameterNames.add(firstParameter.name());
        while (consumeIf(TokenType.COMMA)) {
            Parameter parameter = parseParameter();
            if (parameter == null) {
                errorHandler.handleParserError(new InvalidIdentifierError("INVALID PARAMETER NAME!"),
                        new Position(currentToken.getPosition()));
            } else if (parameterNames.contains(parameter.name())) {
                errorHandler.handleParserError(new DuplicateDeclaration("PARAMETER WITH THIS NAME WAS ALREADY DEFINED!"),
                        new Position(currentToken.getPosition()));
            } else {
                parameterList.add(parameter);
                parameterNames.add(parameter.name());
            }
        }
        return parameterList;
    }

    // parametr_funkcji = typ, identyfikator;
    private Parameter parseParameter() throws Exception {
        if (checkTokenType(TokenType.RIGHT_PARENTHESIS)) {
            return null;
        }
        PrimitiveType type = checkPrimitiveType(currentToken.getTokenType());
        consumeToken();
        if (checkTokenType(TokenType.IDENTIFIER)) {
            String parameterName = (String) currentToken.getValue();
            consumeToken();
            return new Parameter(type, parameterName);
        }
        errorHandler.handleParserError(new InvalidIdentifierError("INVALID PARAMETER NAME!"),
                new Position(currentToken.getPosition()));
        return null;
    }

    // blok = "{", {instrukcja}, "}";
    private Block parseBlock() throws Exception {
        if (!consumeIf(TokenType.LEFT_CURLY_BRACKET)) {
            errorHandler.handleParserError(new MissingBracketError("MISSING OPENING BRACKET!"),
                    new Position(currentToken.getPosition()));
        }
        List<Statement> statements = new ArrayList<>();
        Statement currentStatement = parseStatement();
        while (currentStatement != null) {
            statements.add(currentStatement);
            currentStatement = parseStatement();
        }
        if (!consumeIf(TokenType.RIGHT_CURLY_BRACKET)) {
            errorHandler.handleParserError(new MissingBracketError("MISSING CLOSING BRACKET!"),
                    new Position(currentToken.getPosition()));
        }
        return new Block(statements);
    }

    // instrukcja = (przypisanie | return), ";" | wyrażenie_if | wyrażenie_while;
    private Statement parseStatement() throws Exception {
        return parseReturn();
    }

    // przypisanie = wyrażenie_dostępu, [operator_przypisu, wyrażenie];
    private AssignmentStatement parseAssignment() {
        return null;
    }

    // return = "return", [wyrażenie];
    private ReturnStatement parseReturn() throws Exception {
        if (!consumeIf(TokenType.RETURN)) {
            return null;
        }
        Expression expression = parseOrExpression();
        if (!consumeIf(TokenType.SEMICOLON)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING SEMICOLON AT THE END OF THE LINE!"),
                    new Position(currentToken.getPosition()));
        }
        return new ReturnStatement(expression);
    }

    // wyrażenie_or = wyrażenie_and, {operator_or, wyrażenie_and};
    private OrExpression parseOrExpression() {
        return null;
    }

    // wyrażenie_and = porównanie, {operator_and, porównanie};
    private AndExpression parseAndExpression() {
        return null;
    }

    // porównanie = wyrażenie_dodania, [przyrównanie, wyrażenie_dodania];
    private EqualExpression parseEqualExpression() {
        return null;
    }

    private NotEqualExpression parseNotEqualExpression() {
        return null;
    }

    // wyrażenie_dodania = wyrażenie_mnożenia, {operator_addytywny, wyrażenie_mnożenia};
    private AdditionExpression parseAdditionExpression() {
        return null;
    }

    private SubtractionExpression parseSubtractionExpression() {
        return null;
    }

    // wyrażenie_mnożenia = wyrażenie_rzutu, {multiplikacje, wyrażenie_rzutu};
    private MultiplicationExpression parseMultiplicationExpression() {
        return null;
    }

    private DivisionExpression parseDivisionExpression() {
        return null;
    }

    // wyrażenie_rzutu = wyrażenie_unarne, {(operator_rzutu | operator_wymiany), nazwa_waluty}
    private CurrencyCastExpression parseCurrencyCastExpression() {
        return null;
    }

    // wyrażenie_unarne = [operator_unarny], (wyrażenie_dostępu | literał);
    private NegationExpression parseUnaryExpression() {
        return null;
    }

    // wyrażenie_dostępu = podst_wyrażenie, {operator_dostępu, iden_lub_wywołanie};
    private AccessExpression parseAccessExpression() {
        return null;
    }

    // currency_value = float, identifier;
    private CurrencyPrimitive parseCurrency() throws Exception {
        if (consumeIf(TokenType.CURRENCY)) {
            if (!checkTokenType(TokenType.FLOAT_VALUE)) {
                return null;
            }
            BigDecimal value = BigDecimal.valueOf((Double) currentToken.getValue());
            consumeToken();
            if (!checkTokenType(TokenType.IDENTIFIER)) {
                errorHandler.handleParserError(new InvalidIdentifierError("MISSING CURRENCY NAME!"),
                        new Position(currentToken.getPosition()));
                return null;
            }
            String currency = (String) currentToken.getValue();
            return new CurrencyPrimitive(value, currency);
        }
        return null;
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

    private PrimitiveType checkPrimitiveType(TokenType tokenType) {
        PrimitiveType type = switch(tokenType) {
            case INTEGER -> PrimitiveType.INTEGER;
            case FLOAT -> PrimitiveType.FLOAT;
            case BOOL -> PrimitiveType.BOOL;
            case STRING -> PrimitiveType.STRING;
            case CURRENCY -> PrimitiveType.CURRENCY;
            default -> PrimitiveType.NONE;
        };
        return type;
    }
}
