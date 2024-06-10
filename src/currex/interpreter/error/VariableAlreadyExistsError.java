package currex.interpreter.error;

public class VariableAlreadyExistsError extends Exception {
    public VariableAlreadyExistsError(String message) {
        super(message);
    }
}
