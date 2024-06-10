package currex.parser.error;

public class DuplicateCurrencyDeclarationError extends Exception {
    public DuplicateCurrencyDeclarationError(String message) {
        super(message);
    }
}
