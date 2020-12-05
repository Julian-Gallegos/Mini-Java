package SemanticsAndTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassScope {
    public Map<String, Integer> instanceVariableCount;
    public Map<String, MethodScope> methodMap;
    public Map<String, String> variableMap;

    public List<String> orderedMethodList;

    public Map<String, Integer> fieldOffsets;  // instance variables

    public ClassScope() {
        instanceVariableCount = new HashMap<>();
        methodMap = new HashMap<>();
        variableMap = new HashMap<>();
        orderedMethodList = new ArrayList<>();
        fieldOffsets = new HashMap<>();
    }

    public int getMethodOffset(String methodName) {
        // class Foo
        // methods: X Y Z
        int offset = 8;  // first 8 bytes of object consist of the vtable pointer
        for (String m : orderedMethodList) {
            if (m.equals(methodName)) {
                return offset;
            }
            offset += 8;  // assuming each method is 8 bytes - indifferent of # of params
        }
        return -1;
    }

    // class size is assumed to be the sum of the field sizes.
    public int getClassSize() {
        // TODO
        // deal with padding?
        int vTablePointerSize = 8;
        int defaultDataTypeSize = 8;

        return vTablePointerSize + (defaultDataTypeSize * variableMap.size());
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
            return null;
        }
        return variableMap.get(variableName);
    }

    @Override
    public String toString() {
        String str = "";

        return str;
    }
}
