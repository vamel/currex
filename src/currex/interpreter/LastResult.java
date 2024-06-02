package currex.interpreter;

public record LastResult(Value value) {

    public static LastResult nullify() {
        return new LastResult(null);
    }
}
