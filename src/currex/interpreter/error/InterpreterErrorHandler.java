package currex.interpreter.error;

public class InterpreterErrorHandler {
    public InterpreterErrorHandler() {}

    public void handleInterpreterError(Exception exception) throws Exception {
        System.out.println(exception.getMessage());
        throw exception;
    }
}
