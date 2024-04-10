package currex.lexer;

import currex.sourcereader.SourceReader;
import currex.token.*;

import java.util.Set;
import java.util.stream.Collectors;

public class Lexer {
    private final int MAX_IDENTIFIER_LENGTH = 100;
    private final int MAX_STRING_LENGTH = 100;
    private final int INTEGER_MAX_VALUE = Integer.MAX_VALUE;
    private final SourceReader source;
    private Character currentChar;
    private Token token;

    public Lexer(SourceReader source) {
        this.source = source;
        this.currentChar = null;
        this.token = null;
    }

    public Token fetchToken() {
        if (currentChar == null) {
            nextCharacter();
        }

        while (Character.isWhitespace(currentChar)) {
            nextCharacter();
        }

        if (tryBuildIdentifier() ||
            tryBuildNumber() ||
            tryBuildString() ||
            tryBuildComment() ||
            tryBuildOperator()) {
            return token;
        }
        nextCharacter();
        return new Token(new Position(source.getPosition()), TokenType.UNKNOWN);
    }

    private boolean tryBuildOperator() {
        if (!LexerMapping.singleCharacterOperatorsMap.containsKey(currentChar)
                && currentChar != '&' && currentChar != '|') {
            return false;
        }

        Set<Character> possibleFirstSigns = LexerMapping.twoCharacterOperatorsMap.keySet()
                .stream().map(operator -> operator.charAt(0))
                .collect(Collectors.toSet());
        Set<Character> possibleLastSigns = LexerMapping.twoCharacterOperatorsMap.keySet()
            .stream().map(operator -> operator.charAt(1))
            .collect(Collectors.toSet());

        char firstSign = currentChar;
        Position tokenPosition = new Position(source.getPosition());
        Character nextSign = source.Peek();

        if (possibleFirstSigns.contains(firstSign) && possibleLastSigns.contains(nextSign)) {
            return buildDoubleSignedOperator(firstSign, nextSign, tokenPosition);
        } else {
            return buildSingleSignedOperator(firstSign, tokenPosition);
        }
    }

    private boolean buildDoubleSignedOperator(char first, char second, Position position) {
        StringBuilder operator = new StringBuilder();
        operator.append(first).append(second);
        nextCharacter();
        if (LexerMapping.twoCharacterOperatorsMap.containsKey(operator.toString())) {
            nextCharacter();
            token = new Token(position, LexerMapping.twoCharacterOperatorsMap.get(operator.toString()));
            return true;
        }
        return false;
    }

    private boolean buildSingleSignedOperator(char character, Position position) {
        nextCharacter();
        token = new Token(position, LexerMapping.singleCharacterOperatorsMap.get(character));
        return true;
    }

    private boolean tryBuildNumber() {
        if (!Character.isDigit(currentChar)) {
            return false;
        }

        int integerValue = currentChar - '0';
        Position tokenPosition = new Position(source.getPosition());
        nextCharacter();
        while (Character.isDigit(currentChar)) {
            int charValue = currentChar - '0';
            if (integerValue <= (INTEGER_MAX_VALUE - charValue)/10) {
                integerValue = integerValue * 10 + charValue;
            } else {
                System.out.println("INTEGER NUMBER IS TOO BIG!");
            }
            nextCharacter();
        }

        if (currentChar == '.') {
            nextCharacter();
            long decimalValue = 0;
            short decimalCounter = 0;
            while (Character.isDigit(currentChar)) {
                int charValue = currentChar - '0';
                if (decimalCounter <= 10) {
                    decimalValue = decimalValue * 10 + charValue;
                    decimalCounter++;
                } else {
                    System.out.println("FLOAT NUMBER IS TOO BIG!");
                }
                nextCharacter();
            }
            double totalValue = integerValue + decimalValue / Math.pow(10, decimalCounter);
            token = new FloatToken(tokenPosition, totalValue);
        } else {
            token = new IntegerToken(tokenPosition, integerValue);
        }
        return true;
    }

    private boolean tryBuildIdentifier() {
        if (!Character.isLetter(currentChar) && currentChar != '_') {
            return false;
        }

        StringBuilder identifier = new StringBuilder();
        identifier.append(currentChar);
        Position tokenPosition = new Position(source.getPosition());
        nextCharacter();

        while ((Character.isLetterOrDigit(currentChar) || currentChar == '_')
                && identifier.length() <= MAX_IDENTIFIER_LENGTH) {
            identifier.append(currentChar);
            nextCharacter();
            if (identifier.length() == MAX_IDENTIFIER_LENGTH && !source.isStreamEnd()) {
                System.out.println("IDENTIFIER TOO LONG");
            }
        }

        String tokenValue = identifier.toString();
        if (LexerMapping.keywordMap.containsKey(tokenValue)) {
            token = new Token(tokenPosition, LexerMapping.keywordMap.get(tokenValue));
        } else {
            token = new StringToken(tokenPosition, tokenValue, TokenType.IDENTIFIER);
        }
        return true;
    }

    private boolean tryBuildString() {
        if (currentChar != '\"') {
            return false;
        }

        StringBuilder stringLiteral = new StringBuilder();
        stringLiteral.append(currentChar);
        Position tokenPosition = new Position(source.getPosition());
        nextCharacter();
        boolean isFinished = false;

        while (!isFinished) {
            stringLiteral.append(currentChar);
            if (stringLiteral.toString().length() > MAX_STRING_LENGTH) {
                System.out.println("TOO LONG STRING!");
                isFinished = true;
            } else if (currentChar == '\"' && stringLiteral.charAt(stringLiteral.length()-2) != '\\') {
                token = new StringToken(tokenPosition, stringLiteral.toString(), TokenType.STRING_VALUE);
                isFinished = true;
            } else if (currentChar == 'n' && stringLiteral.charAt(stringLiteral.length()-2) == '\\') {
                stringLiteral.deleteCharAt(stringLiteral.length()-1);
                stringLiteral.deleteCharAt(stringLiteral.length()-1);
                stringLiteral.append('\n');
            } else if (currentChar == '\"' && stringLiteral.charAt(stringLiteral.length()-2) == '\\') {
                stringLiteral.deleteCharAt(stringLiteral.length()-2);
            }
            nextCharacter();
        }
        return true;
    }

    private boolean tryBuildComment() {
        if (currentChar != '/') {
            return false;
        }

        Position tokenPosition = new Position(source.getPosition());
        StringBuilder comment = new StringBuilder();
        comment.append(currentChar);
        nextCharacter();

        if (currentChar == '/') {
            while (currentChar != '\n' && currentChar != '\r') {
                comment.append(currentChar);
                nextCharacter();
            }
                token = new StringToken(tokenPosition, comment.toString(), TokenType.COMMENT);
            } else {
                token = new Token(tokenPosition, TokenType.SLASH);
            }
        return true;
    }

    private void nextCharacter() {
        source.readChar();
        currentChar = source.getCurrentChar();
    }
}
