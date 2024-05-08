package currex.parser;

import currex.lexer.LexerMock;
import currex.parser.error.*;
import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.*;
import currex.structure.statements.*;
import currex.token.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    @Test
    public void ParseEmptyFunctionTest() throws Exception {
        // functionDef() {}
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Assert.assertEquals(1, program.functionDefinitions().size());
        Assert.assertTrue(program.functionDefinitions().containsKey("functionDef"));
        Block block = program.functionDefinitions().get("functionDef").block();
        Assert.assertEquals(0, block.statementList().size());
    }

    @Test
    public void ParseIntegerLiteralTest() throws Exception {
        // 1000;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new IntegerToken(new Position(), 1000),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Block block = program.functionDefinitions().get("functionDef").block();
        Assert.assertEquals(1, block.statementList().size());
        Statement statement = block.statementList().get(0);
        Assert.assertEquals(IntPrimitive.class, statement.getClass());
        IntPrimitive primitive = (IntPrimitive) statement;
        Integer integer = 1000;
        Assert.assertEquals(integer, primitive.value());
    }

    @Test
    public void ParseFloatLiteralTest() throws Exception {
        // 100.001;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new FloatToken(new Position(), 100.001),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Block block = program.functionDefinitions().get("functionDef").block();
        Assert.assertEquals(1, block.statementList().size());
        Statement statement = block.statementList().get(0);
        Assert.assertEquals(FloatPrimitive.class, statement.getClass());
        FloatPrimitive primitive = (FloatPrimitive) statement;
        Double d = 100.001;
        Assert.assertEquals(d, primitive.value());
    }

    @Test
    public void ParseBooleanFalseTest() throws Exception {
        // false;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.FALSE),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Block block = program.functionDefinitions().get("functionDef").block();
        Assert.assertEquals(1, block.statementList().size());
        Statement statement = block.statementList().get(0);
        Assert.assertEquals(BoolPrimitive.class, statement.getClass());
        BoolPrimitive primitive = (BoolPrimitive) statement;
        Assert.assertEquals(false, primitive.value());
    }

    @Test
    public void ParseBooleanTrueTest() throws Exception {
        // true;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.TRUE),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Block block = program.functionDefinitions().get("functionDef").block();
        Assert.assertEquals(1, block.statementList().size());
        Statement statement = block.statementList().get(0);
        Assert.assertEquals(BoolPrimitive.class, statement.getClass());
        BoolPrimitive primitive = (BoolPrimitive) statement;
        Assert.assertEquals(true, primitive.value());
    }

    @Test
    public void ParseStringLiteralTest() throws Exception {
        // "test";
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "\"test\"", TokenType.STRING_VALUE),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Block block = program.functionDefinitions().get("functionDef").block();
        Assert.assertEquals(1, block.statementList().size());
        Statement statement = block.statementList().get(0);
        Assert.assertEquals(StringPrimitive.class, statement.getClass());
        StringPrimitive primitive = (StringPrimitive) statement;
        Assert.assertEquals("\"test\"", primitive.value());
    }

    @Test
    public void ParseCurrencyLiteralTest() throws Exception {
        // 1.50 XYZ;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new FloatToken(new Position(), 1.50),
                        new StringToken(new Position(), "XYZ" ,TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Block block = program.functionDefinitions().get("functionDef").block();
        Assert.assertEquals(1, block.statementList().size());
        Statement statement = block.statementList().get(0);
        Assert.assertEquals(CurrencyPrimitive.class, statement.getClass());
        CurrencyPrimitive primitive = (CurrencyPrimitive) statement;
        Assert.assertEquals(BigDecimal.valueOf(1.50), primitive.value());
        Assert.assertEquals("XYZ", primitive.name());
    }

    @Test
    public void parseFunctionDefinitionSingleParameterTest() throws Exception {
        /* functionDef(currency XYZ) {
          "test";
        } */
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.CURRENCY),
                        new StringToken(new Position(), "XYZ" ,TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "\"test\"", TokenType.STRING_VALUE),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        FunctionDefinition function = program.functionDefinitions().get("functionDef");
        Assert.assertEquals(1, function.parameters().size());
        Parameter param = function.parameters().get(0);
        Assert.assertEquals(PrimitiveType.CURRENCY, param.type());
        Assert.assertEquals("XYZ", param.name());
    }

    @Test
    public void parseFunctionDefinitionTwoParametersTest() throws Exception {
        /* functionDef(currency XYZ, integer ABC) {
          "test";
        } */
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.CURRENCY),
                        new StringToken(new Position(), "XYZ" ,TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.COMMA),
                        new Token(new Position(), TokenType.INTEGER),
                        new StringToken(new Position(), "ABC" ,TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "\"test\"", TokenType.STRING_VALUE),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        FunctionDefinition function = program.functionDefinitions().get("functionDef");
        Assert.assertEquals(2, function.parameters().size());
        Parameter param1 = function.parameters().get(0);
        Assert.assertEquals(PrimitiveType.CURRENCY, param1.type());
        Assert.assertEquals("XYZ", param1.name());
        Parameter param2 = function.parameters().get(1);
        Assert.assertEquals(PrimitiveType.INTEGER, param2.type());
        Assert.assertEquals("ABC", param2.name());
    }

    @Test
    public void parseFunctionDefinitionThreeParametersTest() throws Exception {
        /* functionDef(currency XYZ, integer ABC, float Parameter) {
          "test";
        } */
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.CURRENCY),
                        new StringToken(new Position(), "XYZ" ,TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.COMMA),
                        new Token(new Position(), TokenType.INTEGER),
                        new StringToken(new Position(), "ABC" ,TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.COMMA),
                        new Token(new Position(), TokenType.FLOAT),
                        new StringToken(new Position(), "Parameter" ,TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "\"test\"", TokenType.STRING_VALUE),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        FunctionDefinition function = program.functionDefinitions().get("functionDef");
        Assert.assertEquals(3, function.parameters().size());
        Parameter param1 = function.parameters().get(0);
        Assert.assertEquals(PrimitiveType.CURRENCY, param1.type());
        Assert.assertEquals("XYZ", param1.name());
        Parameter param2 = function.parameters().get(1);
        Assert.assertEquals(PrimitiveType.INTEGER, param2.type());
        Assert.assertEquals("ABC", param2.name());
        Parameter param3 = function.parameters().get(2);
        Assert.assertEquals(PrimitiveType.FLOAT, param3.type());
        Assert.assertEquals("Parameter", param3.name());
    }

    @Test
    public void parseIdentifierTest() throws Exception {
        // test;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "test", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(IdentifierExpression.class, statement.getClass());
        IdentifierExpression identifier = (IdentifierExpression) statement;
        Assert.assertEquals("test", identifier.name());
    }

    @Test
    public void parseFunctionCallNoArgumentsTest() throws Exception {
        // functionCall();
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(FunctionCallExpression.class, statement.getClass());
        FunctionCallExpression functionCall = (FunctionCallExpression) statement;
        Assert.assertEquals("functionCall", functionCall.name());
        Assert.assertEquals(0, functionCall.arguments().size());
    }

    @Test
    public void parseFunctionCallOneArgumentTest() throws Exception {
        // functionCall(10 ABC);
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new IntegerToken(new Position(), 10),
                        new StringToken(new Position(), "ABC", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(FunctionCallExpression.class, statement.getClass());
        FunctionCallExpression functionCall = (FunctionCallExpression) statement;
        Assert.assertEquals("functionCall", functionCall.name());
        Assert.assertEquals(1, functionCall.arguments().size());
        Expression argument = functionCall.arguments().get(0);
        Assert.assertEquals(CurrencyPrimitive.class, argument.getClass());
        CurrencyPrimitive currency = (CurrencyPrimitive) argument;
        Assert.assertEquals(BigDecimal.valueOf(10.0), currency.value());
        Assert.assertEquals("ABC", currency.name());
    }

    @Test
    public void parseFunctionCallMultipleArgumentsTest() throws Exception {
        // functionCall("arg", 4.99);
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new StringToken(new Position(), "\"arg\"", TokenType.STRING_VALUE),
                        new Token(new Position(), TokenType.COMMA),
                        new FloatToken(new Position(), 4.99),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(FunctionCallExpression.class, statement.getClass());
        FunctionCallExpression functionCall = (FunctionCallExpression) statement;
        Assert.assertEquals("functionCall", functionCall.name());
        Assert.assertEquals(2, functionCall.arguments().size());
        Expression stringArg = functionCall.arguments().get(0);
        Expression floatArg = functionCall.arguments().get(1);
        Assert.assertEquals(StringPrimitive.class, stringArg.getClass());
        Assert.assertEquals(FloatPrimitive.class, floatArg.getClass());
        StringPrimitive str = (StringPrimitive) stringArg;
        FloatPrimitive floatVal = (FloatPrimitive) floatArg;
        Assert.assertEquals("\"arg\"", str.value());
        Assert.assertEquals(Double.valueOf(4.99), floatVal.value());
    }

    @Test
    public void parseAccessExpressionFunctionCallTest() throws Exception {
        // identifier.functionCall();
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "identifier", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(AccessExpression.class, statement.getClass());
        AccessExpression accessExpression = (AccessExpression) statement;
        Expression right = accessExpression.right();
        Assert.assertEquals(FunctionCallExpression.class, right.getClass());
    }

    @Test
    public void parseAccessExpressionFunctionCallWithArgumentTest() throws Exception {
        // identifier.functionCall("arg");
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "identifier", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new StringToken(new Position(), "\"arg\"", TokenType.STRING_VALUE),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(AccessExpression.class, statement.getClass());
        AccessExpression accessExpression = (AccessExpression) statement;
        Expression right = accessExpression.right();
        Assert.assertEquals(FunctionCallExpression.class, right.getClass());
    }

    @Test
    public void parseAccessExpressionTest() throws Exception {
        // identifier.access;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "identifier", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "access", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(AccessExpression.class, statement.getClass());
        AccessExpression accessExpression = (AccessExpression) statement;
        Expression right = accessExpression.right();
        Assert.assertEquals(IdentifierExpression.class, right.getClass());
    }

    @Test
    public void parseMultipleAccessExpressionTest() throws Exception {
        // identifier.access.get();
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "identifier", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "access", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "get", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        AccessExpression accessExpression = (AccessExpression) statement;
        Expression left = accessExpression.left();
        Expression right = accessExpression.right();
        Assert.assertEquals(AccessExpression.class, left.getClass());
        Assert.assertEquals(FunctionCallExpression.class, right.getClass());
        AccessExpression access = (AccessExpression) left;
        Expression accessLeft = access.left();
        Expression accessRight = access.right();
        Assert.assertEquals(IdentifierExpression.class, accessLeft.getClass());
        Assert.assertEquals(IdentifierExpression.class, accessRight.getClass());
    }

    @Test
    public void parseUnaryExclamationTest() throws Exception {
        // value = !identifier.functionCall();
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.INTEGER),
                        new StringToken(new Position(), "value", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new Token(new Position(), TokenType.EXCLAMATION),
                        new StringToken(new Position(), "identifier", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        DeclarationStatement statement = (DeclarationStatement) program.functionDefinitions()
                .get("functionDef").block().statementList().get(0);
        Expression negation = statement.expression();
        Assert.assertEquals(NegationExpression.class, negation.getClass());
    }

    @Test
    public void parseUnaryExpressionMinusTest() throws Exception {
        // integer value = -identifier.functionCall();
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.INTEGER),
                        new StringToken(new Position(), "value", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new Token(new Position(), TokenType.MINUS),
                        new StringToken(new Position(), "identifier", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        DeclarationStatement statement = (DeclarationStatement) program.functionDefinitions()
                .get("functionDef").block().statementList().get(0);
        Expression negation = statement.expression();
        Assert.assertEquals(NegationExpression.class, negation.getClass());
    }

    @Test
    public void parseCurrencyCastExpressionTest() throws Exception {
        // result = curr @ ABC;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "curr", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.AT),
                        new StringToken(new Position(), "ABC", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(CurrencyCastExpression.class, right.getClass());
    }

    @Test
    public void parseCurrencyConversionExpressionTest() throws Exception {
        // result = curr -> ABC;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "curr", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.ARROW),
                        new StringToken(new Position(), "ABC", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(CurrencyConversionExpression.class, right.getClass());
    }

    @Test
    public void parseMultiplicationExpressionTest() throws Exception {
        // result = val1 * val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.ASTERISK),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(MultiplicationExpression.class, right.getClass());
    }

    @Test
    public void parseDivisionExpressionTest() throws Exception {
        // result = val1 / val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SLASH),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(DivisionExpression.class, right.getClass());
    }

    @Test
    public void parseAdditionExpressionTest() throws Exception {
        // result = val1 + val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.PLUS),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(AdditionExpression.class, right.getClass());
    }

    @Test
    public void parseSubtractionExpressionTest() throws Exception {
        // result = val1 - val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.MINUS),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(SubtractionExpression.class, right.getClass());
    }

    @Test
    public void parseComparisonGreaterExpressionTest() throws Exception {
        // result = val1 > val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.GREATER),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(GreaterExpression.class, right.getClass());
    }

    @Test
    public void parseComparisonGreaterOrEqualExpressionTest() throws Exception {
        // result = val1 >= val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.GREATER_OR_EQUAL),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(GreaterOrEqualExpression.class, right.getClass());
    }

    @Test
    public void parseComparisonLesserExpressionTest() throws Exception {
        // result = val1 < val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LESSER),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(LesserExpression.class, right.getClass());
    }

    @Test
    public void parseComparisonLesserOrEqualExpressionTest() throws Exception {
        // result = val1 <= val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LESSER_OR_EQUAL),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(LesserOrEqualExpression.class, right.getClass());
    }

    @Test
    public void parseComparisonEqualExpressionTest() throws Exception {
        // result = val1 == val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALITY),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(EqualExpression.class, right.getClass());
    }

    @Test
    public void parseComparisonNotEqualExpressionTest() throws Exception {
        // result = val1 != val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.INEQUALITY),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(NotEqualExpression.class, right.getClass());
    }

    @Test
    public void parseAndExpressionTest() throws Exception {
        // result = val1 && val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.AND),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(AndExpression.class, right.getClass());
    }

    @Test
    public void parseOrExpressionTest() throws Exception {
        // result = val1 || val2;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "val1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.OR),
                        new StringToken(new Position(), "val2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        AssignmentStatement statement = (AssignmentStatement) program.functionDefinitions().get("functionDef")
                .block().statementList().get(0);
        Expression left = statement.left();
        Expression right = statement.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(OrExpression.class, right.getClass());
    }

    @Test
    public void parseReturnStatementTest() throws Exception {
        // return value;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RETURN),
                        new StringToken(new Position(), "value", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(ReturnStatement.class, statement.getClass());
        ReturnStatement returnStatement = (ReturnStatement) statement;
        Expression expression = returnStatement.expression();
        Assert.assertEquals(IdentifierExpression.class, expression.getClass());
    }

    @Test
    public void parseDeclarationStatementTest() throws Exception {
        // int value = identifier.functionCall();
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.INTEGER),
                        new StringToken(new Position(), "value", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "identifier", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(DeclarationStatement.class, statement.getClass());
    }

    @Test
    public void parseAssignmentStatementTest() throws Exception {
        // result = foo.getValue();
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "foo", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "getValue", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(AssignmentStatement.class, statement.getClass());
    }

    @Test
    public void parseIfStatementTest() throws Exception {
        /*
            if(value) {
                1000;
            }
        */
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.IF),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new StringToken(new Position(), "value", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new IntegerToken(new Position(), 1000),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(IfStatement.class, statement.getClass());
    }

    @Test
    public void parseIfElseStatementTest() throws Exception {
        /*
            if(value) {
                1000;
            } else {
                100.0;
            }
        */
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.IF),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new StringToken(new Position(), "value", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new IntegerToken(new Position(), 1000),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.ELSE),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new FloatToken(new Position(), 100.0),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(IfStatement.class, statement.getClass());
        IfStatement ifStatement = (IfStatement) statement;
        Assert.assertEquals(1, ifStatement.elseStatements().size());
        Statement elseStatement = ifStatement.elseStatements().get(0);
        Assert.assertEquals(ElseStatement.class, elseStatement.getClass());
    }

    @Test
    public void parseIfElseIfStatementTest() throws Exception {
        /*
            if(value) {
                1000;
            } else if (result) {
                -40.0;
            } else {
                100.0;
            }
        */
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.IF),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new StringToken(new Position(), "value", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new IntegerToken(new Position(), 1000),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.ELSE),
                        new Token(new Position(), TokenType.IF),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new FloatToken(new Position(), -40.0),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.ELSE),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new FloatToken(new Position(), 100.0),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(IfStatement.class, statement.getClass());
        IfStatement ifStatement = (IfStatement) statement;
        Assert.assertEquals(2, ifStatement.elseStatements().size());
        Statement elseStatement1 = ifStatement.elseStatements().get(0);
        Assert.assertEquals(ElseStatement.class, elseStatement1.getClass());
        Statement elseStatement2 = ifStatement.elseStatements().get(1);
        Assert.assertEquals(ElseStatement.class, elseStatement2.getClass());
    }

    @Test
    public void parseWhileStatementTest() throws Exception {
        /*
        while(identifier.functionCall()) {
            1000;
        }
        */
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.WHILE),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.EXCLAMATION),
                        new StringToken(new Position(), "identifier", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new IntegerToken(new Position(), 1000),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(WhileStatement.class, statement.getClass());
    }

    @Test
    public void parseExpressionWithParenthesesTest() throws Exception {
        // result = value1 * (value2 + value3);
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "result", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "value1", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.ASTERISK),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new StringToken(new Position(), "value2", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.PLUS),
                        new StringToken(new Position(), "value3", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        Program program = parser.parse();
        Statement statement = program.functionDefinitions().get("functionDef").block().statementList().get(0);
        Assert.assertEquals(AssignmentStatement.class, statement.getClass());
        AssignmentStatement assignment = (AssignmentStatement) statement;
        Expression left = assignment.left();
        Expression right = assignment.right();
        Assert.assertEquals(IdentifierExpression.class, left.getClass());
        Assert.assertEquals(MultiplicationExpression.class, right.getClass());
        MultiplicationExpression multiplication = (MultiplicationExpression) right;
        Expression factor1 = multiplication.left();
        Expression factor2 = multiplication.right();
        Assert.assertEquals(IdentifierExpression.class, factor1.getClass());
        Assert.assertEquals(AdditionExpression.class, factor2.getClass());
    }

    @Test
    public void duplicateFunctionDeclarationErrorTest() throws Exception {
        /*
          functionDef() {}
          functionDef() {}
        */
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        DuplicateDeclaration e = Assert.assertThrows(DuplicateDeclaration.class, parser::parse);
    }

    @Test
    public void duplicateParameterDeclarationErrorTest() throws Exception {
        // functionDef(string str, string str)
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.STRING),
                        new StringToken(new Position(), "str", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.COMMA),
                        new Token(new Position(), TokenType.STRING),
                        new StringToken(new Position(), "str", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        DuplicateDeclaration e = Assert.assertThrows(DuplicateDeclaration.class, parser::parse);
    }

    @Test
    public void emptyIfBlockErrorTest() throws Exception {
         // if(value) {}
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.IF),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new StringToken(new Position(), "value", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        EmptyIfElseBlockError e = Assert.assertThrows(EmptyIfElseBlockError.class, parser::parse);
    }

    @Test
    public void emptyIfElseBlockErrorTest() throws Exception {
        /*
            if(value) {
                1000;
            } else {}
        */
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.IF),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new StringToken(new Position(), "value", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new IntegerToken(new Position(), 1000),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.ELSE),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        EmptyIfElseBlockError e = Assert.assertThrows(EmptyIfElseBlockError.class, parser::parse);
    }

    @Test
    public void emptyWhileBlockErrorTest() throws Exception {
        // while(identifier.functionCall()) {}
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.WHILE),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.EXCLAMATION),
                        new StringToken(new Position(), "identifier", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.DOT),
                        new StringToken(new Position(), "functionCall", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        EmptyWhileBlockError e = Assert.assertThrows(EmptyWhileBlockError.class, parser::parse);
    }

    @Test
    public void invalidIdentifierErrorTest() throws Exception {
        /* functionDef() {
           func(2,);
        }*/
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new Token(new Position(), TokenType.INTEGER),
                        new StringToken(new Position(), "func", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new IntegerToken(new Position(), 2),
                        new Token(new Position(), TokenType.COMMA),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        InvalidIdentifierError e = Assert.assertThrows(InvalidIdentifierError.class, parser::parse);
    }

    @Test
    public void missingBracketErrorTest() throws Exception {
        // func() {
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        MissingBracketError e = Assert.assertThrows(MissingBracketError.class, parser::parse);
    }

    @Test
    public void missingExpressionErrorTest() throws Exception {
        // val = 2 +;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "val", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new IntegerToken(new Position(), 2),
                        new Token(new Position(), TokenType.PLUS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        MissingExpressionError e = Assert.assertThrows(MissingExpressionError.class, parser::parse);
    }

    @Test
    public void missingParenthesisErrorTest() throws Exception {
        // val = foo(;
        List<Token> tokenList = new ArrayList<>(
                List.of(new StringToken(new Position(), "functionDef", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.RIGHT_PARENTHESIS),
                        new Token(new Position(), TokenType.LEFT_CURLY_BRACKET),
                        new StringToken(new Position(), "val", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.EQUALS),
                        new StringToken(new Position(), "foo", TokenType.IDENTIFIER),
                        new Token(new Position(), TokenType.LEFT_PARENTHESIS),
                        new Token(new Position(), TokenType.SEMICOLON),
                        new Token(new Position(), TokenType.RIGHT_CURLY_BRACKET))
        );
        LexerMock lexer = new LexerMock(tokenList);
        Parser parser = new Parser(lexer);
        MissingParenthesisError e = Assert.assertThrows(MissingParenthesisError.class, parser::parse);
    }
}
