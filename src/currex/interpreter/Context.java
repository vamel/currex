package currex.interpreter;

import currex.interpreter.error.InvalidVariableTypeError;
import currex.interpreter.error.VariableAlreadyExistsError;

import java.util.HashMap;

public class Context {
    private final HashMap<String, Variable> variables;

    public Context(HashMap<String, Variable> variables) {
        this.variables = variables;
    }

    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    public Variable getVariable(String name) {
        return variables.get(name);
    }

    public void addVariable(String name, Variable variable) throws Exception {
        if (variables.containsKey(name)) {
            throw new VariableAlreadyExistsError("VARIABLE " + name + " HAS BEEN ALREADY DEFINED");
        }
        variables.put(name, variable);
    }

    public void updateVariable(String name, Value newValue) throws InvalidVariableTypeError {
        Variable previous = variables.get(name);
        if (previous.value().valueType() != newValue.valueType()) {
            throw new InvalidVariableTypeError("VARIABLE " + name + " WITH TYPE " + previous.value().valueType() +
                    " CANNOT BE ASSIGNED WITH VALUE " + newValue.value() + " WITH TYPE " + newValue.valueType());
        }
        variables.put(name, new Variable(name, newValue));
    }
}
