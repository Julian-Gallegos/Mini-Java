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
        gen("movq $" + bytes + ", %rdi");
        calloc();
        heapCounter++;
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
