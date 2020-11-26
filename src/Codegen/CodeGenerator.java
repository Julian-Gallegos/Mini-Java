package Codegen;

public class CodeGenerator {
    int stackCounter;

    public CodeGenerator() {
        stackCounter = 0;
    }

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
     * handles the logic of creating a new object
     *
     * @param bytes     the size of the object to create in bytes
     */
    public void callocNewObject(int bytes) {
        // TODO
        gen("movq $" + bytes + ", %rdi");
        calloc();
        //gen("leaq One$$(%rip),%rdx");
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
