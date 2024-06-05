package currex.interpreter.error;

public class MainFunctionNotDefinedError extends Exception {
    public MainFunctionNotDefinedError(String message) {
        super(message);
    }
}
