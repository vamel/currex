package currex.interpreter.error;

public class MainFunctionNotDefinedError extends Exception {
    MainFunctionNotDefinedError(String message) {
        super(message);
    }
}
