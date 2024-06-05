package currex.interpreter.error;

public class InvalidFunctionDefinitionError extends Exception {
    public InvalidFunctionDefinitionError(String message) {
        super(message);
    }
}
