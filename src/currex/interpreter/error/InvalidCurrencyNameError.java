package currex.interpreter.error;

public class InvalidCurrencyNameError extends Exception {
    public InvalidCurrencyNameError(String message) {
        super(message);
    }
}
