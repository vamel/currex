package currex.lexer.error;

import currex.token.Position;

public class ErrorHandler {
    public ErrorHandler() {}

    public void handleLexerError(Exception exception, Position position) throws Exception {
        System.out.println("An error has occured in line "
                + position.getRow()
                + " in column "
                + position.getColumn() + ":\n"
                + exception.getMessage());
        throw exception;
    }
}
