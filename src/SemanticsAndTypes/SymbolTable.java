package SemanticsAndTypes;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    // ...
    public Map<String, ClassScope> globalScope;

    public SymbolTable() {
        globalScope = new HashMap<>();
    }

    public boolean putClass(String className, ClassScope classScope) {
        if (globalScope.containsKey(className)) {
            System.out.println("Error: Class name " + className + " already defined.");
            return false;
        }
        globalScope.put(className, classScope);
        return true;
    }

    public ClassScope getClassScope(String className) {
        if (!globalScope.containsKey(className)) {
            System.out.println("Error: Class name " + className + " not defined.");
            System.exit(1);
        }
        return globalScope.get(className);
    }

    @Override
    public String toString() {
        String str = "";
        for (String className : globalScope.keySet()) {
            str += className + "\n";
            if (!globalScope.get(className).variableMap.isEmpty()) {
                str += "\tVariable(s):\n"
                for (String variable : globalScope.get(className).variableMap.keySet()) {
                    str += "\tName: " + variable + ", Type: " + globalScope.get(className).variableMap.get(variable) + "\n";
                }
            }
            if (!globalScope.get(className).methodMap.isEmpty()) {
                str += "\tMethod(s):\n"
                for (String method : globalScope.get(className).methodMap.keySet()) {
                    str += "\tName: " + method + ", Return type: " + globalScope.get(className).methodMap.get(method).methodType + "\n";
                    if (!globalScope.get(className).methodMap.get(method).arguments.isEmpty()) {
                        str += "\t\tArgument(s):\n";
                        for(ArgumentType arg : globalScope.get(className).methodMap.get(method).arguments) {
                            str += "\t\tName: " + arg.name + ", Type: " + arg.type + "\n";
                        }
                    }
                    if (!globalScope.get(className).methodMap.get(method).methodVariables.isEmpty()) {
                        str += "\t\tVariable(s):\n";
                        for(String variable : globalScope.get(className).methodMap.get(method).methodVariables.keySet()) {
                            str += "\t\tName: " + variable + ", Type: " + globalScope.get(className).methodMap.get(method).methodVariables.get(variable) + "\n";
                        }
                    }
                }
            }

        }
        return str;
    }
}