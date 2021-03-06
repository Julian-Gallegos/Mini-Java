package Codegen;

import SemanticsAndTypes.*;

import java.util.List;

public class CodeGenerator {
    public int doneCounter;
    public int elseCounter;
    public int stackCounter;
    public int heapCounter;
    private boolean aligned;

    public CodeGenerator() {
        doneCounter = 0;
        elseCounter = 0;
        stackCounter = 0;
        heapCounter = 0;  // may not need?
        aligned = false;
    }

    public String genDoneLabel() {
        return ".Done" + doneCounter++;
    }

    public String genElseLabel() {
        return ".Else" + elseCounter++;
    }

    /* TODO: List of information we need for assembly code generation. Many of these could be done in the symbol table generation,
     *       but it might be easier to just compute on the fly.
     * *     Make method that generates the vtables for each class, make sure it keeps order of inherited methods consistent
     *       with the super class methods.
     * *     Maybe make helper method that takes all classes from symbol table and creates a set of lists for each class hierarchy
     *
     * *
     */

    /*
    public void GenerateVTables(SymbolTable symTable) {
        ClassScope currClass;
        List<ArgumentType> arguments;
        List<MethodScope> vtable;
        List<String> classNames;

        for (String className : symTable.globalScope.keySet()) {

        }

    }
    */

    public void put() {
        System.out.println("\tcall put");
    }

    public void calloc() {
        System.out.println("\tcall mjcalloc");
    }

    // write code string s to .asm output
    public void gen(String s) {
        String[] tokens = s.split(" ");
        if (tokens[0].equals("pushq")) {
            stackCounter += 8;
        } else if (tokens[0].equals("popq")) {
            stackCounter -= 8;
        }
        System.out.println("\t" + s);
    }

    // write label L to .asm output as 'L:'
    public void genLabel(String l) {
        System.out.println(l + ":");
    }

    /**
     * header for method table for a class
     *
     * @param className     the class to create a method table for
     * @param superClass    the class that 'className' extends.
     *                      if 'className' extends no objects, it
     *                      will be '0'.
     */
    public String vtableHeader(String className, String superClass) {
        String str = className + "$$: .quad " + superClass;
        str += superClass.equals("0") ? "" : "$$";
        return str;
        //System.out.println(str);
    }

    /**
     * handles the logic of creating a new object
     *
     * @param bytes     the size of the object to create in bytes
     */
    public void callocNewObject(int bytes) {
        // TODO
        gen("pushq %rdi");
        align();
        gen("movq $" + bytes + ", %rdi");
        calloc();
        heapCounter++;
        undoAlign();
        gen("popq %rdi");
    }

    public void genComment(String comment) {
        System.out.println("# " + comment);
    }

    public void align() {
        if (stackCounter % 16 != 0) {
            // need to do alignment
            this.gen("subq $8, %rsp");
            stackCounter += 8;
            aligned = true;
        }
    }

    public void undoAlign() {
        if (aligned) {
            // undo a alignment previously done
            this.gen("addq $8, %rsp");
            stackCounter -= 8;
            aligned = false;
        }
    }

    public String genTestLabel() {
        return null;
    }

    public String genBodyLabel() {
        return null;
    }


    // takes an identifier id.
    // recursively explores the super classes and looks for a match.
    // we know that id is somewhere in the super class hierarchy
    // it has to be a field for one of the super classes.
    /*
    public int getOffsetForIdentifierField (SymbolTable st, String id, String cn) {
        // checking to see if id is a field of this class
        if (st.getClassScope(cn).variableMap.containsKey(id)) {
            //return st.getClassScope(cn).
            return -1;
        }
        // call on the super class
        return -1;
    }

    class A {
        a = 100;
    }
    class B extends A {
        ..
        .. method () {
            a = 100;
        }
    }

    private boolean isExtendedFrom(String className, String methodName) {
        if (!symbolTable.getClassScope(className).methodMap.containsKey(methodName)) {
            // check for extended class
            String extendedClass = typeTable.getType(className);
            if (extendedClass != null) {
                return isExtendedFrom(extendedClass, methodName);
            } else {
                // no extended class
                return false;
            }
        }
        return true;
    }
    */


    // ================ METHODS WE MAY NOT NEED ================

    // write 'op src,dst' to .asm
    public void genbin(String op, String src, String dst) {
        // TODO
        // tbh may not even need this
    }

    public void gen(String op, String src, String dst) {
        System.out.println("\t" + op + " " + src + " " + dst);
        // TODO
        // maybe?
    }

    public void moveQ(String register) {
        // TODO
    }
}
