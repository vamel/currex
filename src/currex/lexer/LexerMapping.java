package currex.lexer;

import currex.token.TokenType;

import java.util.Map;

import static java.util.Map.entry;

public class LexerMapping {

    public static final Map<Character, TokenType> singleCharacterOperatorsMap = Map.ofEntries(
            entry('[', TokenType.LEFT_BRACKET),
            entry(']', TokenType.RIGHT_BRACKET),
            entry('{', TokenType.LEFT_CURLY_BRACKET),
            entry('}', TokenType.RIGHT_CURLY_BRACKET),
            entry(')', TokenType.RIGHT_PARENTHESIS),
            entry('(', TokenType.LEFT_PARENTHESIS),
            entry('.', TokenType.DOT),
            entry(',', TokenType.COMMA),
            entry(';', TokenType.SEMICOLON),
            entry('<', TokenType.LESSER),
            entry('>', TokenType.GREATER),
            entry('!', TokenType.EXCLAMATION),
            entry('=', TokenType.EQUALS),
            entry('@', TokenType.AT),
            entry('+', TokenType.PLUS),
            entry('-', TokenType.MINUS),
            entry('*', TokenType.ASTERISK),
            entry('/', TokenType.SLASH)
    );

    public static final Map<String, TokenType> twoCharacterOperatorsMap = Map.ofEntries(
            entry("<=", TokenType.LESSER_OR_EQUAL),
            entry(">=", TokenType.GREATER_OR_EQUAL),
            entry("!=", TokenType.INEQUALITY),
            entry("==", TokenType.EQUALITY),
            entry("&&", TokenType.AND),
            entry("||", TokenType.OR),
            entry("->", TokenType.ARROW)
    );

    public static final Map<String, TokenType> keywordMap = Map.ofEntries(
            entry("while", TokenType.WHILE),
            entry("if", TokenType.IF),
            entry("else", TokenType.ELSE),
            entry("return", TokenType.RETURN),
            entry("int", TokenType.INTEGER),
            entry("float", TokenType.FLOAT),
            entry("string", TokenType.STRING),
            entry("bool", TokenType.BOOL),
            entry("currency", TokenType.CURRENCY),
            entry("true", TokenType.TRUE),
            entry("false", TokenType.FALSE)
    );
}
