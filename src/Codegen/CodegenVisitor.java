package Codegen;

import AST.*;
import AST.Visitor.Visitor;

public class CodegenVisitor implements Visitor {
    private CodeGenerator codeGen;

    public CodegenVisitor(Program root) {
        codeGen = new CodeGenerator();
        root.accept(this);
    }

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {
        n.e.accept(this);
    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        codeGen.gen(".text");
        codeGen.gen(".globl asm_main");

        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            n.cl.get(i).accept(this);
        }
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        codeGen.genLabel("asm_main");
        //System.out.print("class ");
        n.i1.accept(this);
        //System.out.println(" {");
        //System.out.print("  public static void main (String [] ");
        n.i2.accept(this);
        //System.out.println(") {");
        //System.out.print("    ");
        n.s.accept(this);
        //System.out.println("  }");
        //System.out.println("}");
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        //System.out.print("class ");
        n.i.accept(this);
        //System.out.println(" { ");
        for ( int i = 0; i < n.vl.size(); i++ ) {
            //System.out.print("  ");
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        //System.out.print("class ");
        n.i.accept(this);
        //System.out.println(" extends ");
        n.j.accept(this);
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {
        n.t.accept(this);
        n.i.accept(this);
    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        //System.out.print("  public ");
        n.t.accept(this);
        codeGen.genLabel(n.i.s);
        n.i.accept(this);
        for ( int i = 0; i < n.fl.size(); i++ ) {
            n.fl.get(i).accept(this);
        }
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.get(i).accept(this);
        }
        //System.out.print("    return ");
        n.e.accept(this);
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {
        n.t.accept(this);
        n.i.accept(this);
    }

    public void visit(IntArrayType n) {
    }

    public void visit(BooleanType n) {
    }

    public void visit(IntegerType n) {
    }

    // String s;
    public void visit(IdentifierType n) {
    }

    // StatementList sl;
    public void visit(Block n) {
        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.get(i).accept(this);
        }
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        n.e.accept(this);
        n.s1.accept(this);
        n.s2.accept(this);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        n.e.accept(this);
        n.s.accept(this);
    }

    // Exp e;
    public void visit(Print n) {
        n.e.accept(this);
        /*
        movq    $5,%rdi     # System.out.println(5)
        call    _put
         */
        codeGen.gen("movq %rax, %rdi");
        codeGen.put();
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        n.i.accept(this);
        n.e.accept(this);
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        n.i.accept(this);
        //System.out.print("[");
        n.e1.accept(this);
        //System.out.print("] = ");
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(And n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        // generate code to eval e1, result in %rax
        n.e1.accept(this);

        // push e1 onto stack
        codeGen.pushQ("%rax");

        // generate code to eval e2, result in %rax
        n.e2.accept(this);

        // pop left argument into %rdx; clean up stack
        codeGen.popQ("%rdx");

        // perform the addition; result in %rax
        codeGen.gen("addq %rdx, %rax");
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        codeGen.pushQ("%rax");
        n.e2.accept(this);
        codeGen.popQ("%rdx");
        codeGen.gen("negq %rax");
        codeGen.gen("addq %rdx, %rax");
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        codeGen.pushQ("%rax");
        n.e2.accept(this);
        codeGen.popQ("%rdx");
        codeGen.gen("imulq %rdx, %rax");
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        //System.out.print("[");
        n.e2.accept(this);
        //System.out.print("]");
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
        //System.out.print(".length");
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);
        //System.out.print(".");
        n.i.accept(this);
        //System.out.print("(");
        for ( int i = 0; i < n.el.size(); i++ ) {
            n.el.get(i).accept(this);
        }
        //System.out.print(")");
    }

    // int i;
    public void visit(IntegerLiteral n) {
        codeGen.gen("movq $" + n.i + ", %rax");
    }

    public void visit(True n) {
        codeGen.gen("movq 0 %rax");
    }

    public void visit(False n) {
        codeGen.gen("movq 1 %rax");
    }

    // String s;
    public void visit(IdentifierExp n) {
        codeGen.gen("movq varoffset(%rbp), %rax");
    }

    public void visit(This n) {
    }

    // Exp e;
    public void visit(NewArray n) {
        //System.out.print("new int [");
        n.e.accept(this);
        //System.out.print("]");
    }

    // Identifier i;
    public void visit(NewObject n) {
        //System.out.print("new ");
        //System.out.print(n.i.s);
        //System.out.print("()");
    }

    // Exp e;
    public void visit(Not n) {
        //System.out.print("!");
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
    }
}
