package currex.interpreter.error;

public class FunctionDoesNotExistError extends Exception {
    public FunctionDoesNotExistError(String message) {
        super(message);
    }
}
