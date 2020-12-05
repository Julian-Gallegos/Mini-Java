package Codegen;

import AST.*;
import AST.Visitor.Visitor;
import SemanticsAndTypes.ArgumentType;
import SemanticsAndTypes.ClassScope;
import SemanticsAndTypes.MethodScope;
import SemanticsAndTypes.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class CodegenVisitor implements Visitor {
    private CodeGenerator codeGen;
    private SymbolTable symbolTable;
    private BuildVTableVisitor buildVTableVisitor;
    private String currentClass;
    private List<String> vars;
    private String currentMethod;
    private int whileCounter;

    public CodegenVisitor(Program root, SymbolTable symbolTable, BuildVTableVisitor buildVTableVisitor) {
        this.symbolTable = symbolTable;
	    this.buildVTableVisitor = buildVTableVisitor;
        whileCounter = 0;
        codeGen = new CodeGenerator();
        // TODO: put in helper method
        // Set offsets for local variables at each method context.
        for (ClassScope classScope : symbolTable.globalScope.values()) {
            int j = 0;
            for (String fieldName : classScope.variableMap.keySet()) {
                classScope.fieldOffsets.put(fieldName, (8 * (j++)));
            }

            for (MethodScope method : classScope.methodMap.values()) {
                int i = 0;
                for (String variable : method.methodVariables.keySet()) {
                    method.variableOffsets.put(variable, i*8);
                    i++;
                }
                for (ArgumentType methodArg : method.arguments) {
                    methodArg.setVariableOffset(8 * (i++));
                }
            }
        }
        currentClass = null;
        currentMethod = null;
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
        // print v table
        codeGen.gen(".data");
        buildVTableVisitor.printVTable();
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
        prologue();
        n.s.accept(this);
        epilogue();
        //System.out.println("  }");
        //System.out.println("}");
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        currentClass = n.i.s;
        //System.out.print("class ");
        n.i.accept(this);
        //System.out.println(" { ");
        for ( int i = 0; i < n.vl.size(); i++ ) {
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
        currentClass = n.i.s;
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
        currentMethod = n.i.s;
        //System.out.print("  public ");
        n.t.accept(this);
        codeGen.genLabel(currentClass + "$" + n.i.s);
        prologue();
        n.i.accept(this);
        for ( int i = 0; i < n.fl.size(); i++ ) {
            n.fl.get(i).accept(this);
        }
        vars = new ArrayList<String>();
        if (n.vl.size() > 0) {
            codeGen.gen("subq $" + 8 * n.vl.size() + ", %rsp");
        }
        for (int i = 0; i < n.fl.size(); i++) {
            if (i == 0) {
                codeGen.gen("pushq %rsi");
            } else if (i == 1) {
                codeGen.gen("pushq %rdx");
            } else if (i == 2) {
                codeGen.gen("pushq %rcx");
            } else if (i == 3) {
                codeGen.gen("pushq %r8");
            } else if (i == 4) {
                codeGen.gen("pushq %r9");
            }
        }
        for ( int i = 0; i < n.vl.size(); i++ ) {
            //TODO: maybe do this? vars.add(n.vl.get(i).i.s);
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.get(i).accept(this);
        }
        //System.out.print("    return ");
        n.e.accept(this);
        epilogue();
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
        codeGen.genComment("Entering if statement.");
        //String elseLabel = codeGen.genElseLabel();
        //String doneLabel = codeGen.genDoneLabel();

        n.e.accept(this);
        codeGen.gen("cmpq $0, %rax");
        codeGen.gen("je .Else" + codeGen.elseCounter);

        n.s1.accept(this);  // run this if true
        codeGen.gen("jmp .Done" + codeGen.doneCounter);

        codeGen.genLabel(codeGen.genElseLabel());
        n.s2.accept(this);  // else branch
        codeGen.genLabel(codeGen.genDoneLabel());
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        codeGen.genComment("Entering while loop.");
        String whileLabel = ".Test" + whileCounter;

        codeGen.gen("jmp " + whileLabel);
        codeGen.genLabel(".Test" + whileCounter++);
        n.e.accept(this);  // test condition
        codeGen.gen("cmpq $0, %rax");
        codeGen.gen("je .Done" + codeGen.doneCounter);

        n.s.accept(this);  // body of while loop
        codeGen.gen("jmp " + whileLabel);  // loop

        codeGen.genLabel(codeGen.genDoneLabel());
    }

    // Exp e;
    public void visit(Print n) {
        n.e.accept(this);
        codeGen.gen("movq %rax, %rdi");
        // codeGen.align();
        codeGen.put();

    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        //TODO: currently just handles method variables, does not handle class instance variables.
        int offset;
        boolean isInstanceVariable = false;
        if (symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).methodVariables.containsKey(n.i.s)) {
            offset = symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).variableOffsets.get(n.i.s);
        } else if (symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).arguments.contains(new ArgumentType(n.i.s, "n/a"))) {
            offset = symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).getArgumentOffset(n.i.s);
        } else if (symbolTable.getClassScope(currentClass).fieldOffsets.containsKey(n.i.s)) {
            // fields of obj
            isInstanceVariable = true;
            offset = symbolTable.getClassScope(currentClass).fieldOffsets.get(n.i.s);
        } else {
            // should never reach this case
            offset = -1;
        }
        n.i.accept(this);
        n.e.accept(this);
        if (!isInstanceVariable) {
            codeGen.gen("movq %rax, -" + (offset + 8) + "(%rbp)");
        } else {
            codeGen.gen("movq %rax, -" + (offset + 8) + "(%rdi)");
        }
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
        codeGen.gen("pushq %rax");

        n.e2.accept(this);
        codeGen.gen("popq %rdx");
        codeGen.gen("cmpq %rdx, %rax");
        codeGen.gen("sete %al");
        codeGen.gen("movzbq %al, %rax");
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        n.e1.accept(this);
        codeGen.gen("pushq %rax");

        n.e2.accept(this);
        codeGen.gen("popq %rdx");
        codeGen.gen("cmpq %rax, %rdx");
        codeGen.gen("setl %al");
        codeGen.gen("movzbq %al, %rax");
        //codeGen.gen("jge .Else" + codeGen.elseCounter);

        //codeGen.gen("setge %rax");
        //codeGen.gen();
        // gen(condjump targetLabel)
        //      - a variable assignment, or to some code block
        //      boolean a = 2 < 3 || if (2 < 3) { ... } else { ... }
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        // generate code to eval e1, result in %rax
        n.e1.accept(this);

        // push e1 onto stack
        codeGen.gen("pushq %rax");

        // generate code to eval e2, result in %rax
        n.e2.accept(this);

        // pop left argument into %rdx; clean up stack
        codeGen.gen("popq %rdx");

        // perform the addition; result in %rax
        codeGen.gen("addq %rdx, %rax");
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        codeGen.gen("pushq %rax");
        n.e2.accept(this);
        codeGen.gen("popq %rdx");
        codeGen.gen("negq %rax");
        codeGen.gen("addq %rdx, %rax");
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        codeGen.gen("pushq %rax");

        n.e2.accept(this);
        codeGen.gen("popq %rdx");

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
        codeGen.gen("movq %rax, %rdi");
        codeGen.gen("pushq %rdi");


        //int objOffset = symbolTable.getClassOffset(((NewObject) n.e).i.s);
        // method offset depends on position in vtable, should just be calculated as 1,2,3,4...
        int methodOffset = -1;
        if (n.e instanceof NewObject) {
            methodOffset = symbolTable.getClassScope(((NewObject) n.e).i.s).getMethodOffset(n.i.s);
        } else if (n.e instanceof This) {
            methodOffset = symbolTable.getClassScope(currentClass).getMethodOffset(n.i.s);
        } else if (n.e instanceof IdentifierExp) {
            methodOffset = symbolTable.getClassScope(((IdentifierExp)n.e).s).getMethodOffset(n.i.s);
        }

        // movq
        for (int i = 0; i < n.el.size(); i++) {
            n.el.get(i).accept(this);
            String argumentRegister = null;
            if (i == 0) {
                argumentRegister = "%rsi";
            } else if (i == 1) {
                argumentRegister = "%rdx";
            } else if (i == 2) {
                argumentRegister = "%rcx";
            } else if (i == 3) {
                argumentRegister = "%r8";
            } else if (i == 4) {
                argumentRegister = "%r9";
            }
            codeGen.gen("movq %rax, " + argumentRegister);
        }

        codeGen.gen("popq %rdi");
        codeGen.gen("movq 0(%rdi), %rax");

        codeGen.align();
        codeGen.gen("call *" + methodOffset + "(%rax)");
        codeGen.undoAlign();

        //codeGen.gen("movq " + objOffset + "(%rbp), %rdi");    // first argument is obj prt ("this")
        //codeGen.gen("movq 0(%rdi), %rax");                         // load vtable address into %rax
        //codeGen.gen("call *" + methodOffset + "(%rax)");      // call function whose address is at
                                                                 // the specified offset in the vtable
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
        //TODO: currently just handles method variables, does not handle class instance variables.
        int offset;
        boolean isInstanceVariable = false;
        if (symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).methodVariables.containsKey(n.s)) {
            offset = symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).variableOffsets.get(n.s);
        } else if (symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).arguments.contains(new ArgumentType(n.s, "n/a"))) {
            offset = symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).getArgumentOffset(n.s);
        } else if (symbolTable.getClassScope(currentClass).fieldOffsets.containsKey(n.s)) {
            // fields of obj
            isInstanceVariable = true;
            offset = symbolTable.getClassScope(currentClass).fieldOffsets.get(n.s);
        } else {
            // should never reach this case
            offset = -1;
        }
        if (!isInstanceVariable) {
            codeGen.gen("movq -" + (offset + 8) + "(%rbp), %rax");
        } else {
            codeGen.gen("movq -" + (offset + 8) + "(%rdi), %rax");
        }
    }

    public void visit(This n) {
        codeGen.gen("movq %rdi, %rax");
    }

    // Exp e;
    public void visit(NewArray n) {
        //System.out.print("new int [");
        n.e.accept(this);
        //System.out.print("]");
    }

    // Identifier i;
    public void visit(NewObject n) {
        // TODO - align?
        codeGen.callocNewObject(8 + (symbolTable.getClassScope(n.i.s).variableMap.size() * 8));
        // TODO - undoAlign?
        codeGen.gen("leaq " + n.i.s + "$$, %rdx");  // get method table address
        codeGen.gen("movq %rdx, 0(%rax)");                // store vtbl ptr at beginning of object
    }

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);
        codeGen.gen("testq %rax, %rax");
        codeGen.gen("sete %al");
        codeGen.gen("movzbq %al, %rax");
    }

    // String s;
    public void visit(Identifier n) {
    }

    private void prologue() {
        codeGen.gen("pushq %rbp");
        codeGen.gen("movq %rsp, %rbp");
    }

    private void epilogue() {
        codeGen.gen("movq %rbp, %rsp");
        codeGen.gen("popq %rbp");
        codeGen.gen("ret");
    }

}
