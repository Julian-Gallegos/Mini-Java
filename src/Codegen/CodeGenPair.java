package Codegen;

public class CodeGenPair {

    public String className;
    public String methodName;

    public CodeGenPair (String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String toString() {
        return "[" + className + ", " + methodName + "]";
    }

}
