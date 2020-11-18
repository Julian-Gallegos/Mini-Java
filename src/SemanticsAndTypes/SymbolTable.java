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
            str += "\t" + globalScope.get(className).toString();
            str += "\n";
        }
        return str;
    }
}