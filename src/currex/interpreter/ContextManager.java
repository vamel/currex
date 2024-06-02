package currex.interpreter;

import currex.interpreter.error.VariableDoesNotExistError;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class ContextManager {
    private final Deque<Context> contexts = new ArrayDeque<>();

    public void addContext(Context context) {
        contexts.push(context);
    }

    public void removeCurrentContext() {
        contexts.pop();
    }

    public void addVariable(String name, Variable variable) throws Exception {
        contexts.getLast().addVariable(name, variable);
    }

    public void updateVariable(String name, Value newValue) throws Exception {
        Iterator<Context> iterator = contexts.descendingIterator();
        while (iterator.hasNext()) {
            Context currentContext = iterator.next();
            if (currentContext.getVariables().containsKey(name)) {
                currentContext.updateVariable(name, newValue);
                return;
            }
        }
        throw new VariableDoesNotExistError("VARIABLE " + name + " DOES NOT EXIST IN ANY CONTEXT");
    }

    public Variable fetchVariable(String name) throws Exception {
        Iterator<Context> iterator = contexts.descendingIterator();
        while (iterator.hasNext()) {
            Context currentContext = iterator.next();
            if (currentContext.getVariables().containsKey(name)) {
                return currentContext.getVariable(name);
            }
        }
        throw new VariableDoesNotExistError("VARIABLE " + name + " DOES NOT EXIST IN ANY CONTEXT");
    }
}
