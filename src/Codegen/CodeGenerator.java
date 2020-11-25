package Codegen;

public class CodeGenerator {
    // TODO
    // declare any fields

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

    // write code string s to .asm output
    public void gen(String s) {
        System.out.println("\t" + s);
    }

    // write label L to .asm output as 'L:'
    public void genLabel(String l) {
        System.out.println(l + ":");
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
