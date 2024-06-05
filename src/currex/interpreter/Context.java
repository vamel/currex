package currex.interpreter;

import java.util.HashMap;

public class Context {
    private final HashMap<String, Value> variables;

    public Context() {
        this.variables = new HashMap<>();
    }

    public HashMap<String, Value> getVariables() {
        return variables;
    }

    public Value getVariable(String name) {
        return variables.get(name);
    }

    public void addVariable(String name, Value value) {
        variables.put(name, value);
    }

    public void updateVariable(String name, Value newValue) {
        variables.put(name, newValue);
    }
}
