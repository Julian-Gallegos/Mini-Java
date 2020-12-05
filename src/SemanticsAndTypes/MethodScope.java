package SemanticsAndTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodScope {
    // this should count field declarations of all superclasses.
    public Map<String, Integer> variableDeclarationCount;

    public Map<String, String> methodVariables;
    public Map<String, Integer> variableOffsets;
    public String methodType;
    // list tuple elements storing the name & type of argument.
    public List<ArgumentType> arguments;

    public MethodScope(String methodType) {
        variableDeclarationCount = new HashMap<>(); 
        this.methodType = methodType;
        methodVariables = new HashMap<>();
        variableOffsets = new HashMap<>();
        arguments = new ArrayList<ArgumentType>();
    }

    // please send in a name that's an argument of this method.
    public String getTypeForName(String name) {
        for (ArgumentType at : arguments) {
            if (at.name.equals(name)) {
                return at.type;
            }
        }
        return null;
    }

    // assumes the name 'argName' is contained in the arguments list
    public int getArgumentOffset(String argName) {
        for (ArgumentType at : arguments) {
            if (at.name.equals(argName)) {
                return at.offset;
            }
        }
        return -1;
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