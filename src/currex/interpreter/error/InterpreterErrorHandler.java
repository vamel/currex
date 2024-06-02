package currex.interpreter.error;

import currex.token.Position;

public class InterpreterErrorHandler {
    public InterpreterErrorHandler() {}

    public void handleInterpreterError(Exception exception, Position position) throws Exception {
        System.out.println("An error has occured in line "
                + position.getRow()
                + " in column "
                + position.getColumn() + ":\n"
                + exception.getMessage());
        throw exception;
    }
}
