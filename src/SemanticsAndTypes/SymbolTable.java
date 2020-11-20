package SemanticsAndTypes;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    // ...
    public Map<String, ClassScope> globalScope;

    public SymbolTable() {
        globalScope = new HashMap<>();
        //ClassScope length = new ClassScope();
        //length.putMethod("length", new MethodScope("integer"));
        //globalScope.put("intArray", length);
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
            //System.out.println("Error: Class name " + className + " not defined.");
            return null;
        }
        return globalScope.get(className);
    }

    @Override
    public String toString() {
        String str = "";
        for (String className : globalScope.keySet()) {
            str += "Class name: " + className + ":\n";
            if (!globalScope.get(className).variableMap.isEmpty()) {
                str += "   Variable(s):\n";
                for (String variable : globalScope.get(className).variableMap.keySet()) {
                    str += "      Name: " + variable + ", Type: " + globalScope.get(className).variableMap.get(variable) + "\n";
                }
            }
            if (!globalScope.get(className).methodMap.isEmpty()) {
                str += "   Method(s):\n";
                for (String method : globalScope.get(className).methodMap.keySet()) {
                    str += "      Name: " + method + ", Return type: " + globalScope.get(className).methodMap.get(method).methodType + "\n";
                    if (!globalScope.get(className).methodMap.get(method).arguments.isEmpty()) {
                        str += "         Argument(s):\n";
                        for(ArgumentType arg : globalScope.get(className).methodMap.get(method).arguments) {
                            str += "            Name: " + arg.name + ", Type: " + arg.type + "\n";
                        }
                    }
                    if (!globalScope.get(className).methodMap.get(method).methodVariables.isEmpty()) {
                        str += "         Variable(s):\n";
                        for(String variable : globalScope.get(className).methodMap.get(method).methodVariables.keySet()) {
                            str += "            Name: " + variable + ", Type: " + globalScope.get(className).methodMap.get(method).methodVariables.get(variable) + "\n";
                        }
                    }
                }
            }

        }
        return str;
    }
}