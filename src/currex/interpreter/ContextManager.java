package currex.interpreter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class ContextManager {
    private final Deque<Context> contexts = new ArrayDeque<>();

    public Context getLastContext() {
        return contexts.getLast();
    }

    public void addContext(Context context) {
        contexts.push(context);
    }

    public void removeCurrentContext() {
        contexts.pop();
    }

    public void addVariable(Variable variable) {
        contexts.getLast().addVariable(variable);
    }

    public void updateVariable(String name, Value newValue) {
        Iterator<Context> iterator = contexts.descendingIterator();
        while (iterator.hasNext()) {
            Context currentContext = iterator.next();
            if (currentContext.getVariables().containsKey(name)) {
                currentContext.updateVariable(name, newValue);
                return;
            }
        }
        System.out.println("VariableDoesNotExistError(VARIABLE " + name + " DOES NOT EXIST IN ANY CONTEXT");
    }

    public Variable fetchVariable(String name) {
        Iterator<Context> iterator = contexts.descendingIterator();
        while (iterator.hasNext()) {
            Context currentContext = iterator.next();
            if (currentContext.getVariables().containsKey(name)) {
                return currentContext.getVariable(name);
            }
        }
        System.out.println("VariableDoesNotExistError(VARIABLE " + name + " DOES NOT EXIST IN ANY CONTEXT");
        return null;
    }
}
