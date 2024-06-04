package currex.parser.error;

public class CurrencyNotDeclaredError extends Exception {
    public CurrencyNotDeclaredError(String message) {
        super(message);
    }
}
