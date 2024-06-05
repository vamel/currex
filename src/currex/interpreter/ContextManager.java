package currex.interpreter;

import currex.interpreter.error.InterpreterErrorHandler;
import currex.interpreter.error.InvalidVariableTypeError;
import currex.interpreter.error.VariableAlreadyExistsError;
import currex.interpreter.error.VariableDoesNotExistError;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class ContextManager {
    private final Deque<Context> contexts = new ArrayDeque<>();
    private final InterpreterErrorHandler errorHandler = new InterpreterErrorHandler();

    public Context getLastContext() {
        return contexts.getLast();
    }

    public void addContext(Context context) {
        contexts.add(context);
    }

    public void removeCurrentContext() {
        contexts.removeLast();
    }

    public void addVariable(String name, Value value) throws Exception {
        if (contexts.getLast().getVariables().containsKey(name)) {
            errorHandler.handleInterpreterError(new VariableAlreadyExistsError("VARIABLE " + name + " HAS BEEN ALREADY DEFINED!"));
        }
        contexts.getLast().addVariable(name, value);
    }

    public void updateVariable(String name, Value newValue) throws Exception {
        Iterator<Context> iterator = contexts.descendingIterator();
        while (iterator.hasNext()) {
            Context currentContext = iterator.next();
            if (currentContext.getVariables().containsKey(name)) {
                Value previous = currentContext.getVariables().get(name);
                if (previous.valueType() == newValue.valueType()) {
                    currentContext.updateVariable(name, newValue);
                    return;
                }
                errorHandler.handleInterpreterError(new InvalidVariableTypeError(
                        "VARIABLE " + name + " WITH TYPE " + previous.valueType() +
                        " CANNOT BE ASSIGNED WITH VALUE " + newValue.value() + " WITH TYPE " + newValue.valueType() + "!"
                ));
                return;
            }
        }
        errorHandler.handleInterpreterError(new VariableDoesNotExistError(
                "VARIABLE " + name + " DOES NOT EXIST IN ANY CONTEXT!"
        ));
    }

    public Value fetchVariable(String name) throws Exception {
        Iterator<Context> iterator = contexts.descendingIterator();
        while (iterator.hasNext()) {
            Context currentContext = iterator.next();
            if (currentContext.getVariables().containsKey(name)) {
                return currentContext.getVariable(name);
            }
        }
        errorHandler.handleInterpreterError(new VariableDoesNotExistError(
                "VARIABLE " + name + " DOES NOT EXIST IN ANY CONTEXT!"
        ));
        return null;
    }
}
