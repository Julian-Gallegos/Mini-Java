package SemanticsAndTypes;

import java.util.HashMap;
import java.util.Map;

public class ClassScope {
    public Map<String, MethodScope> methodMap;
    public Map<String, String> variableMap;

    public ClassScope() {
        methodMap = new HashMap<>();
        variableMap = new HashMap<>();
    }

    public boolean putMethod(String methodName, MethodScope methodScope) {
        if (methodMap.containsKey(methodName)) {
            System.out.println("Error: Method name " + methodName + " already defined.");
            return false;
        }
        methodMap.put(methodName, methodScope);
        return true;
    }

    public MethodScope getMethodScope(String methodName) {
        if (!methodMap.containsKey(methodName)) {
            System.out.println("Error: Method name " + methodName + " not defined.");
            System.exit(1);
        }
        return methodMap.get(methodName);
    }

    // this does allow overriding existing variable binding.
    public void putVariable(String variableName, String type) {
        variableMap.put(variableName, type);
    }

    public String getVariableType(String variableName) {
        if (!variableMap.containsKey(variableName)) {
            System.out.println("Error: Variable name " + variableName + " not defined.");
            System.exit(1);
        }
        return variableMap.get(variableName);
    }

    @Override
    public String toString() {
        String str = "";

        return str;
    }
}
