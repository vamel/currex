package currex.interpreter;

import java.util.HashMap;

public class Context {
    private final HashMap<String, Value> variables;

    public Context(HashMap<String, Value> variables) {
        this.variables = variables;
    }

    public HashMap<String, Value> getVariables() {
        return variables;
    }

    public Value getVariable(String name) {
        return variables.get(name);
    }

    public void addVariable(String name, Value value) {
        if (variables.containsKey(name)) {
            System.out.println("VariableAlreadyExistsError(VARIABLE  + " + name + " HAS BEEN ALREADY DEFINED");
        }
        variables.put(name, value);
    }

    public void updateVariable(String name, Value newValue) {
        Value previous = variables.get(name);
        if (previous.valueType() != newValue.valueType()) {
            System.out.println("InvalidVariableTypeError(VARIABLE " + name + " WITH TYPE " + previous.valueType() +
                    " CANNOT BE ASSIGNED WITH VALUE " + newValue.value() + " WITH TYPE " + newValue.valueType());
        }
        variables.put(name, newValue);
    }
}
