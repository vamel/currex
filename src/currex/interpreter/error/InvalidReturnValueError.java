package currex.interpreter.error;

public class InvalidReturnValueError extends Exception {
    public InvalidReturnValueError(String message) {
        super(message);
    }
}
