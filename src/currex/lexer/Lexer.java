package currex.lexer;

import currex.lexer.error.*;
import currex.source.Source;
import currex.token.*;
import currex.utils.CurrexLimits;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;

public class Lexer {
    private final Source source;
    private Character currentChar;
    private Token token;
    private ErrorHandler errorHandler;

    public Lexer(Source source) {
        this.source = source;
        this.currentChar = null;
        this.token = null;
        this.errorHandler = new ErrorHandler();
    }

    public Token fetchToken() throws Exception {
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
        errorHandler.handleLexerError(new UnknownTokenError("UNKNOWN TOKEN FOUND!"),
                new Position(source.getPosition()));
        return new ErrorToken(new Position(source.getPosition()), TokenType.UNKNOWN);
    }

    private boolean tryBuildOperator() throws Exception {
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
        } else if ((firstSign == '&' || firstSign == '|') && firstSign != nextSign) {
            errorHandler.handleLexerError(new MissingSecondCharacterError("MISSING SECOND CHARACTER ERROR!"),
                    new Position(source.getPosition()));
            token = new ErrorToken(tokenPosition, TokenType.MISSING_SECOND_CHARACTER_ERROR);
            return true;
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

    private boolean tryBuildNumber() throws Exception {
        if (!Character.isDigit(currentChar)) {
            return false;
        }

        int integerValue = currentChar - '0';
        Position tokenPosition = new Position(source.getPosition());
        nextCharacter();
        while (Character.isDigit(currentChar)) {
            int charValue = currentChar - '0';
            if (integerValue <= (CurrexLimits.INTEGER_MAX_VALUE - charValue)/10) {
                integerValue = integerValue * 10 + charValue;
            } else {
                errorHandler.handleLexerError(new TooBigIntegerError("INTEGER IS TOO BIG!"),
                        new Position(source.getPosition()));
                token = new ErrorToken(tokenPosition, TokenType.TOO_BIG_INTEGER_ERROR);
                return true;
            }
            nextCharacter();
        }

        if (currentChar == '.') {
            nextCharacter();
            long decimalValue = 0;
            short decimalCounter = 0;
            while (Character.isDigit(currentChar)) {
                int charValue = currentChar - '0';
                if (decimalCounter <= CurrexLimits.MAXIMUM_DECIMAL_PLACES) {
                    decimalValue = decimalValue * 10 + charValue;
                    decimalCounter++;
                } else {
                    errorHandler.handleLexerError(new FloatingPointError("FLOAT NUMBER IS TOO BIG!"),
                            new Position(source.getPosition()));
                    token = new ErrorToken(tokenPosition, TokenType.FLOATING_POINT_ERROR);
                    return true;
                }
                nextCharacter();
            }
            double totalValue = integerValue + decimalValue / Math.pow(10, decimalCounter);
            BigDecimal toRound = new BigDecimal(Double.toString(totalValue));
            totalValue = toRound.setScale(10, RoundingMode.HALF_UP).doubleValue();
            token = new FloatToken(tokenPosition, totalValue);
        } else {
            token = new IntegerToken(tokenPosition, integerValue);
        }
        return true;
    }

    private boolean tryBuildIdentifier() throws Exception {
        if (!Character.isLetter(currentChar) && currentChar != '_') {
            return false;
        }

        StringBuilder identifier = new StringBuilder();
        identifier.append(currentChar);
        Position tokenPosition = new Position(source.getPosition());
        nextCharacter();

        while ((Character.isLetterOrDigit(currentChar) || currentChar == '_')
                && identifier.length() <= CurrexLimits.MAX_IDENTIFIER_LENGTH) {
            identifier.append(currentChar);
            nextCharacter();
            if (identifier.length() == CurrexLimits.MAX_IDENTIFIER_LENGTH && !source.isStreamEnd()) {
                errorHandler.handleLexerError(new IdentifierTooLongError("TOO LONG IDENTIFIER!"),
                        new Position(source.getPosition()));
                token = new ErrorToken(tokenPosition, TokenType.TOO_LONG_IDENTIFIER_ERROR);
                return true;
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

    private boolean tryBuildString() throws Exception {
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
            if (stringLiteral.toString().length() > CurrexLimits.MAX_STRING_LENGTH) {
                errorHandler.handleLexerError(new StringTooLongError("TOO LONG STRING!"),
                        new Position(source.getPosition()));
                token = new ErrorToken(tokenPosition, TokenType.TOO_LONG_STRING_ERROR);
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
        comment.append(currentChar);

        if (currentChar == '/') {
            while (currentChar != '\n' && currentChar != '\r' && source.Peek() != (char)-1) {
                nextCharacter();
                comment.append(currentChar);
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
