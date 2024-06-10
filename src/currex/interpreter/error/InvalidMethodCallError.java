package currex.interpreter.error;

public class InvalidMethodCallError extends Exception {
    public InvalidMethodCallError(String message) {
        super(message);
    }
}
