package currex.parser;

import currex.lexer.Lexer;
import currex.parser.error.*;
import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.*;
import currex.structure.statements.*;
import currex.token.*;

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
        }
        consumeToken();
        if (!consumeIf(TokenType.LEFT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING OPENING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
        }
        List<Parameter> functionParameters = parseParameterList();
        if (!consumeIf(TokenType.RIGHT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING CLOSING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
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
        Statement statement = parseAssignment();
        if (statement != null) {
            return statement;
        }
        statement = parseIf();
        if (statement != null) {
            return statement;
        }
        statement = parseWhile();
        if (statement != null) {
            return statement;
        }
        statement = parseReturn();
        return statement;
    }

    // przypisanie = wyrażenie_dostępu, [operator_przypisu, wyrażenie];
    private Statement parseAssignment() throws Exception {
        Expression leftExpression = parseAccessExpression();
        Statement assignment = leftExpression;
        if (leftExpression == null) {
            return null;
        }
        if (consumeIf(TokenType.EQUALS)) {
            Expression rightExpression = parseExpression();
            if (rightExpression == null) {
                errorHandler.handleParserError(new MissingExpressionError("MISSING SECOND EXPRESSION!"),
                        new Position(currentToken.getPosition()));
            }
            assignment = new AssignmentStatement(leftExpression, rightExpression);
        }
        if (!consumeIf(TokenType.SEMICOLON)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING SEMICOLON AT THE END OF THE LINE!"),
                    new Position(currentToken.getPosition()));
        }
        return assignment;
    }

    // wyrażenie_while = "while", "(", wyrażenie, ")", blok;
    private WhileStatement parseWhile() throws Exception {
        if (!consumeIf(TokenType.WHILE)) {
            return null;
        }
        if (!consumeIf(TokenType.LEFT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING OPENING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
        }
        Expression expression = parseExpression();
        if (!consumeIf(TokenType.RIGHT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING CLOSING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
        }
        Block block = parseBlock();
        return new WhileStatement(expression, block);
    }

    // wyrażenie_if = "if", "(", wyrażenie, ")", blok, {"else", ["if", "(", wyrażenie, ")"], blok};
    private IfStatement parseIf() throws Exception {
        IfStatement ifStatement;
        List<ElseStatement> elseStatements = new ArrayList<>();
        if (!consumeIf(TokenType.IF)) {
            return null;
        }
        if (!consumeIf(TokenType.LEFT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING OPENING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
        }
        Expression expression = parseExpression();
        if (!consumeIf(TokenType.RIGHT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING CLOSING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
        }
        Block trueBlock = parseBlock();
        if (trueBlock.statementList().isEmpty()) {
            errorHandler.handleParserError(new EmptyIfElseBlockError("EMPTY IF BLOCK!"),
                    new Position(currentToken.getPosition()));
        }
        while (consumeIf(TokenType.ELSE)) {
            Expression elseCondition = parseElseCondition();
            Block elseBlock = parseBlock();
            if (elseBlock.statementList().isEmpty()) {
                errorHandler.handleParserError(new EmptyIfElseBlockError("EMPTY ELSE BLOCK!"),
                        new Position(currentToken.getPosition()));
            }
            ElseStatement elseStatement = new ElseStatement(elseCondition, elseBlock);
            elseStatements.add(elseStatement);
        }
        ifStatement = new IfStatement(expression, trueBlock, elseStatements);
        return ifStatement;
    }

    private Expression parseElseCondition() throws Exception {
        if (!consumeIf(TokenType.IF)) {
            return null;
        }
        if (!consumeIf(TokenType.LEFT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING OPENING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
        }
        Expression expression = parseExpression();
        if (!consumeIf(TokenType.RIGHT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING CLOSING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
        }
        return expression;
    }

    // return = "return", [wyrażenie];
    private ReturnStatement parseReturn() throws Exception {
        if (!consumeIf(TokenType.RETURN)) {
            return null;
        }
        Expression expression = parseExpression();
        if (!consumeIf(TokenType.SEMICOLON)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING SEMICOLON AT THE END OF THE LINE!"),
                    new Position(currentToken.getPosition()));
        }
        return new ReturnStatement(expression);
    }

    // wyrażenie = wyrażenie_or;
    private Expression parseExpression() throws Exception {
        return parseOrExpression();
    }

    // wyrażenie_or = wyrażenie_and, {operator_or, wyrażenie_and};
    private Expression parseOrExpression() throws Exception {
        Expression leftExpression = parseAndExpression();
        if (leftExpression == null) {
            return null;
        }
        while (consumeIf(TokenType.OR)) {
            Expression rightExpression = parseAndExpression();
            if (rightExpression == null) {
                errorHandler.handleParserError(new MissingExpressionError("MISSING SECOND EXPRESSION!"),
                        new Position(currentToken.getPosition()));
            }
            leftExpression = new OrExpression(leftExpression, rightExpression);
        }
        return leftExpression;
    }

    // wyrażenie_and = porównanie, {operator_and, porównanie};
    private Expression parseAndExpression() throws Exception {
        Expression leftExpression = parseComparisonExpression();
        if (leftExpression == null) {
            return null;
        }
        while (consumeIf(TokenType.AND)) {
            Expression rightExpression = parseComparisonExpression();
            if (rightExpression == null) {
                errorHandler.handleParserError(new MissingExpressionError("MISSING SECOND EXPRESSION!"),
                        new Position(currentToken.getPosition()));
            }
            leftExpression = new AndExpression(leftExpression, rightExpression);
        }
        return leftExpression;
    }

    // porównanie = wyrażenie_dodania, [przyrównanie, wyrażenie_dodania];
    private Expression parseComparisonExpression() throws Exception {
        Expression leftExpression = parseAdditionExpression();
        if (leftExpression == null) {
            return null;
        }
        TokenType tokenType;
        if (checkTokenType(TokenType.GREATER) || checkTokenType(TokenType.GREATER_OR_EQUAL) ||
                checkTokenType(TokenType.LESSER) || checkTokenType(TokenType.LESSER_OR_EQUAL) ||
                checkTokenType(TokenType.EQUALITY) || checkTokenType(TokenType.INEQUALITY)) {
            tokenType = currentToken.getTokenType();
            consumeToken();
            Expression rightExpression = parseAdditionExpression();
            if (rightExpression == null) {
                errorHandler.handleParserError(new MissingExpressionError("MISSING SECOND EXPRESSION!"),
                        new Position(currentToken.getPosition()));
            }
            return checkOperator(tokenType, leftExpression, rightExpression);
        }
        return leftExpression;
    }

    // wyrażenie_dodania = wyrażenie_mnożenia, {operator_addytywny, wyrażenie_mnożenia};
    private Expression parseAdditionExpression() throws Exception {
        Expression leftExpression = parseMultiplicationExpression();
        if (leftExpression == null) {
            return null;
        }
        TokenType tokenType;
        while (checkTokenType(TokenType.PLUS) || checkTokenType(TokenType.MINUS)) {
            tokenType = currentToken.getTokenType();
            consumeToken();
            Expression rightExpression = parseMultiplicationExpression();
            if (rightExpression == null) {
                errorHandler.handleParserError(new MissingExpressionError("MISSING SECOND EXPRESSION!"),
                        new Position(currentToken.getPosition()));
            }
            leftExpression = checkOperator(tokenType, leftExpression, rightExpression);
        }
        return leftExpression;
    }

    // wyrażenie_mnożenia = wyrażenie_rzutu, {multiplikacje, wyrażenie_rzutu};
    private Expression parseMultiplicationExpression() throws Exception {
        Expression leftExpression = parseCurrencyCastExpression();
        if (leftExpression == null) {
            return null;
        }
        TokenType tokenType;
        while (checkTokenType(TokenType.ASTERISK) || checkTokenType(TokenType.SLASH)) {
            tokenType = currentToken.getTokenType();
            consumeToken();
            Expression rightExpression = parseCurrencyCastExpression();
            if (rightExpression == null) {
                errorHandler.handleParserError(new MissingExpressionError("MISSING SECOND EXPRESSION!"),
                        new Position(currentToken.getPosition()));
            }
            leftExpression = checkOperator(tokenType, leftExpression, rightExpression);
        }
        return leftExpression;
    }

    // wyrażenie_rzutu = wyrażenie_unarne, {(operator_rzutu | operator_wymiany), nazwa_waluty}
    private Expression parseCurrencyCastExpression() throws Exception {
        Expression leftExpression = parseUnaryExpression();
        if (leftExpression == null) {
            return null;
        }
        TokenType tokenType;
        while (checkTokenType(TokenType.AT) || checkTokenType(TokenType.ARROW)) {
            tokenType = currentToken.getTokenType();
            consumeToken();
            Expression rightExpression = parseIdentifierOrFunctionCall();
            if (rightExpression == null) {
                errorHandler.handleParserError(new MissingExpressionError("MISSING SECOND EXPRESSION!"),
                        new Position(currentToken.getPosition()));
            }
            leftExpression = checkOperator(tokenType, leftExpression, rightExpression);
        }
        return leftExpression;
    }

    // wyrażenie_unarne = [operator_unarny], (wyrażenie_dostępu | literał);
    private Expression parseUnaryExpression() throws Exception {
        if (consumeIf(TokenType.MINUS) || consumeIf(TokenType.EXCLAMATION)) {
            Expression expression = parseAccessExpression();
            return new NegationExpression(expression);
        }
        return parseAccessExpression();
    }

    // wyrażenie_dostępu = podst_wyrażenie, {operator_dostępu, iden_lub_wywołanie};
    private Expression parseAccessExpression() throws Exception {
        Expression leftExpression = parseBasicExpression();
        if (leftExpression == null) {
            return null;
        }
        while (consumeIf(TokenType.DOT)) {
            Expression rightExpression = parseIdentifierOrFunctionCall();
            if (rightExpression == null) {
                errorHandler.handleParserError(new MissingExpressionError("MISSING SECOND EXPRESSION!"),
                        new Position(currentToken.getPosition()));
            }
            leftExpression = new AccessExpression(leftExpression, rightExpression);
        }
        return leftExpression;
    }

    // podst_wyrażenie = iden_lub_wywołanie | literał | "(", wyrażenie ")";
    private Expression parseBasicExpression() throws Exception {
        Primitive literal = parsePrimitive();
        if (literal != null) {
            return literal;
        }
        Expression expression = parseIdentifierOrFunctionCall();
        if (expression != null) {
            return expression;
        }
        if (!consumeIf(TokenType.LEFT_PARENTHESIS)) {
            return null;
        }
        expression = parseExpression();
        if (expression == null) {
            errorHandler.handleParserError(new MissingExpressionError("MISSING SECOND EXPRESSION!"),
                    new Position(currentToken.getPosition()));
        }
        if (!consumeIf(TokenType.RIGHT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING CLOSING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
        }
        return expression;
    }

    // iden_lub_wywołanie = identyfikator, ["(", [lista_argumentów], ")"];
    private Expression parseIdentifierOrFunctionCall() throws Exception {
        if (!checkTokenType(TokenType.IDENTIFIER)) {
            return null;
        }
        String name = (String) currentToken.getValue();
        consumeToken();
        List<Expression> functionArguments = parseArguments();
        if (functionArguments == null) {
            return new IdentifierExpression(name);
        }
        return new FunctionCallExpression(name, functionArguments);
    }

    // lista_argumentów = wyrażenie, {",", wyrażenie};
    private List<Expression> parseArguments() throws Exception {
        if (!consumeIf(TokenType.LEFT_PARENTHESIS)) {
            return null;
        }
        List<Expression> arguments = new ArrayList<>();
        Expression arg = parseExpression();
        if (arg == null) {
            return arguments;
        }
        arguments.add(arg);
        while (consumeIf(TokenType.COMMA)) {
            arg = parseExpression();
            if (arg == null) {
                errorHandler.handleParserError(new InvalidArgumentError("INVALID OR MISSING ARGUMENT!"),
                        new Position(currentToken.getPosition()));
            }
            arguments.add(arg);
        }
        if (!consumeIf(TokenType.RIGHT_PARENTHESIS)) {
            errorHandler.handleParserError(new MissingSemicolonError("MISSING CLOSING PARENTHESIS!"),
                    new Position(currentToken.getPosition()));
        }
        return arguments;
    }

    // literał = boolean | ((liczba | float), [nazwa_waluty]) | string;
    private Primitive parsePrimitive() throws Exception {
        if (checkTokenType(TokenType.STRING_VALUE)) {
            String value = (String) currentToken.getValue();
            consumeToken();
            return new StringPrimitive(value);
        }
        if (consumeIf(TokenType.TRUE)) {
            return new BoolPrimitive(true);
        } else if (consumeIf(TokenType.FALSE)) {
            return new BoolPrimitive(false);
        }
        return parseCurrency();
    }

    // currency_value = float, identifier;
    private Primitive parseCurrency() throws Exception {
        Integer integerValue = null;
        Double floatValue = null;
        if (checkTokenType(TokenType.INTEGER_VALUE)) {
            integerValue = (Integer) currentToken.getValue();
            consumeToken();
        }
        if (checkTokenType(TokenType.FLOAT_VALUE)) {
            floatValue = (Double) currentToken.getValue();
            consumeToken();
        }
        if (checkTokenType(TokenType.IDENTIFIER) && (integerValue != null || floatValue != null)) {
            String currency = (String) currentToken.getValue();
            consumeToken();
            Double castedValue = integerValue == null ? floatValue : Double.valueOf(integerValue);
            BigDecimal value = BigDecimal.valueOf(castedValue);
            return new CurrencyPrimitive(value, currency);
        }
        if (integerValue != null) {
            return new IntPrimitive(integerValue);
        } else if (floatValue != null) {
            return new FloatPrimitive(floatValue);
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
        return switch(tokenType) {
            case INTEGER -> PrimitiveType.INTEGER;
            case FLOAT -> PrimitiveType.FLOAT;
            case BOOL -> PrimitiveType.BOOL;
            case STRING -> PrimitiveType.STRING;
            case CURRENCY -> PrimitiveType.CURRENCY;
            default -> PrimitiveType.NONE;
        };
    }

    private Expression checkOperator(TokenType tokenType, Expression left, Expression right) {
        return switch(tokenType) {
            case AT -> new CurrencyCastExpression(left, right);
            case ARROW -> new CurrencyConversionExpression(left, right);
            case ASTERISK -> new MultiplicationExpression(left, right);
            case SLASH -> new DivisionExpression(left, right);
            case PLUS -> new AdditionExpression(left, right);
            case MINUS -> new SubtractionExpression(left, right);
            case GREATER -> new GreaterExpression(left, right);
            case GREATER_OR_EQUAL -> new GreaterOrEqualExpression(left, right);
            case LESSER -> new LesserExpression(left, right);
            case LESSER_OR_EQUAL -> new LesserOrEqualExpression(left, right);
            case EQUALITY -> new EqualExpression(left, right);
            case INEQUALITY -> new NotEqualExpression(left, right);
            default -> null;
        };
    }
}
