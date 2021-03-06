package Codegen;

import AST.*;
import AST.Visitor.Visitor;
import SemanticsAndTypes.ClassScope;
import SemanticsAndTypes.SymbolTable;
import SemanticsAndTypes.TypeTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildVTableVisitor implements Visitor {
    public Map<String, List<String>> vTables;

    private CodeGenerator codeGen;
    private SymbolTable symbolTable;
    private TypeTable typeTable;

    private String currentClass;

    public BuildVTableVisitor(Program root, SymbolTable symbolTable, TypeTable tt) {
        vTables = new HashMap<String, List<String>>();
        this.symbolTable = symbolTable;
        codeGen = new CodeGenerator();
        this.typeTable = tt;
        root.accept(this);
        currentClass = null;
    }

    public void printVTable() {
        for (String classLabel : vTables.keySet()) {
            System.out.println(classLabel);
            for (String classMethod : vTables.get(classLabel)) {
                System.out.println(classMethod);
            }
        }
    }

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {
        n.e.accept(this);
    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            n.cl.get(i).accept(this);
        }
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        n.i1.accept(this);
        n.i2.accept(this);
        n.s.accept(this);
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        currentClass = n.i.s;
        n.i.accept(this);
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }
        vTables.put(codeGen.vtableHeader(n.i.s, "0"), new ArrayList<String>());
        for ( int i = 0; i < n.ml.size(); i++ ) {
            vTables.get(codeGen.vtableHeader(n.i.s, "0"))
                    .add("\t.quad " + n.i.s + "$" + n.ml.get(i).i.s);
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
        n.i.accept(this);
        n.j.accept(this);
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }

        vTables.put(codeGen.vtableHeader(n.i.s, n.j.s), new ArrayList<String>());
        List<CodeGenPair> methodList = new ArrayList<>();
        buildMethodList(methodList, n.i.s);
        symbolTable.getClassScope(currentClass).vTableList = methodList;

        for (CodeGenPair pair : methodList) {
            String key = codeGen.vtableHeader(n.i.s, n.j.s);
            vTables.get(key).add("\t.quad " + pair.className + "$" + pair.methodName);
        }

        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }
    }

    private void buildMethodList(List<CodeGenPair> lst, String cn) {
        String extendedClass = typeTable.getType(cn);
        if (extendedClass != null) {
            // get to the root
            buildMethodList(lst, extendedClass);
        }
        // add the classes within lst
        for (String m : symbolTable.getClassScope(cn).orderedMethodList) {
            boolean updated = false;

            for (int i = 0; i < lst.size(); i++) {
                CodeGenPair p = lst.get(i);
                if (p.methodName.equals(m)) {
                    lst.set(i, new CodeGenPair(cn, m));
                    //p.className = cn;
                    updated = true;
                    //System.out.println("Debug: " + lst);
                }
            }
            if (!updated) {
                lst.add(new CodeGenPair(cn, m));
            }
        }
        //System.out.println("Debug: " + lst);
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
        n.t.accept(this);
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
        n.e1.accept(this);
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
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);
        n.i.accept(this);
        for ( int i = 0; i < n.el.size(); i++ ) {
            n.el.get(i).accept(this);
        }
    }

    // int i;
    public void visit(IntegerLiteral n) {
    }

    public void visit(True n) {
    }

    public void visit(False n) {
    }

    // String s;
    public void visit(IdentifierExp n) {
    }

    public void visit(This n) {
    }

    // Exp e;
    public void visit(NewArray n) {
        n.e.accept(this);
    }

    // Identifier i;
    public void visit(NewObject n) { }

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
    }
}
