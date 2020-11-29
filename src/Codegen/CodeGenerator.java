package Codegen;

import SemanticsAndTypes.*;

import java.util.List;

public class CodeGenerator {
    int stackCounter;
    int heapCounter;

    public CodeGenerator() {
        stackCounter = 0;
        heapCounter = 0;  // may not need?
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

    public void pushQ(String register) {
        gen("pushq " + register);
        stackCounter++;
    }

    public void popQ(String register) {
        gen("popq " + register);
        stackCounter--;
    }

    public void put() {
        System.out.println("\tcall put");
    }

    public void calloc() {
        System.out.println("\tcall mjcalloc");
    }

    // write code string s to .asm output
    public void gen(String s) {
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
    public void vtableHeader(String className, String superClass) {
        String str = className + "$$: .quad " + superClass;
        str += superClass.equals("0") ? "" : "$$";
        System.out.println(str);
    }

    /**
     * handles the logic of creating a new object
     *
     * @param bytes     the size of the object to create in bytes
     */
    public void callocNewObject(int bytes) {
        // TODO
        gen("movq $" + bytes + ", %rdi");
        calloc();
        heapCounter++;
    }




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
