package SemanticsAndTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodScope {

    public Map<String, String> methodVariables;
    public String methodType;
    // list tuple elements storing the name & type of argument.
    public List<ArgumentType> arguments;

    public MethodScope(String methodType) {
        this.methodType = methodType;
        methodVariables = new HashMap<>();
        arguments = new ArrayList<ArgumentType>();
    }

    public void insertArgument(ArgumentType t) {
        arguments.add(t);
    }

    // this does allow overriding existing variable binding.
    public void putVariable(String variableName, String type) {
        methodVariables.put(variableName, type);
    }

    public String getVariableType(String variableName) {
        if (!methodVariables.containsKey(variableName)) {
            //System.out.println("Error: Variable name " + variableName + " not defined.");
            // System.exit(1);
            return null;
        }
        return methodVariables.get(variableName);
    }

    public String getParameterType(String name) {
        for (ArgumentType t : arguments) {
            if (t.name.equals(name)) {
                return t.type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "under construction";
    }
}