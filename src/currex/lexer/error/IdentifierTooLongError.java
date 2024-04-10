package currex.lexer.error;

public class IdentifierTooLongError extends Exception {
    public IdentifierTooLongError(String message) {
        super(message);
    }
}
