package currex.interpreter;

import java.util.HashMap;
import java.util.Optional;

public class Context {
    private final HashMap<String, Variable> variables;

    public Context(HashMap<String, Variable> variables) {
        this.variables = variables;
    }

    public Optional<Variable> getVariable(String name) {
        return Optional.ofNullable(variables.get(name));
    }

    public void addVariable(String name, Variable variable) {
        if (variables.containsKey(name)) {
            // HANDLE ERROR DUPLICATE KEY
            return;
        }
        variables.put(name, variable);
    }

    public boolean updateVariable(String name, Value newValue) {
        Variable previous = variables.get(name);
        if (previous.value().valueType() != newValue.valueType()) {
            // HANDLE ERROR VALUES NOT THE SAME
            return false;
        }
        variables.put(name, new Variable(name, newValue));
        return true;
    }
}
