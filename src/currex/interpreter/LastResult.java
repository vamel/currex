package currex.interpreter;



public record LastResult(boolean returned, boolean present, Value value) {

    public static LastResult empty() {
        return new LastResult(false, false, null);
    }
}
