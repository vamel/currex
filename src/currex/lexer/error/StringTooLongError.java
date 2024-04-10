package currex.lexer.error;

public class StringTooLongError extends Exception {
    public StringTooLongError(String message) {
        super(message);
    }
}
