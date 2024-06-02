package currex.interpreter.error;

public class VariableDoesNotExistError extends Exception {
    public VariableDoesNotExistError(String message) {
        super(message);
    }
}
