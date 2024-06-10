package currex.interpreter.error;

public class InvalidFunctionCallError extends Exception {
    public InvalidFunctionCallError(String message) {
        super(message);
    }
}
