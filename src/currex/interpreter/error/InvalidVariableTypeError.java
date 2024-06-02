package currex.interpreter.error;

public class InvalidVariableTypeError extends Exception {
    public InvalidVariableTypeError(String message) {
        super(message);
    }
}
