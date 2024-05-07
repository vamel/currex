package currex.parser.error;

public class NotEndOfStreamException extends Exception {
    public NotEndOfStreamException(String message) {
        super(message);
    }
}
