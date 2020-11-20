package SemanticsAndTypes;

import java.util.HashMap;
import java.util.Map;

public class TypeTable {
    // ...
    public Map<String, String> types;

    public TypeTable() {
        types = new HashMap<>();
    }

    public boolean putType(String typeName, String extendType) {
        if (types.containsKey(typeName)) {
            System.out.println("Error: Type name " + typeName + " already defined.");
            return false;
        }
        types.put(typeName, extendType);
        return true;
    }

    public String getType(String typeName) {
        if (!types.containsKey(typeName)) {
            System.out.println("Error: Type name " + typeName + " not defined.");
            return null;
        }
        return types.get(typeName);
    }
}