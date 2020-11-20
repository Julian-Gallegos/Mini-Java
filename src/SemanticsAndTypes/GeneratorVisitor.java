package SemanticsAndTypes;

import AST.*;
import AST.Visitor.Visitor;

public class GeneratorVisitor implements Visitor {
    public SymbolTable symbolTable;
    public TypeTable typeTable;

    public GeneratorVisitor(Program root) {
        // TODO
        symbolTable = new SymbolTable();
        typeTable = new TypeTable();
        root.accept(this);
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
        symbolTable.putClass(n.i1.s, new ClassScope());
        n.i1.accept(this);   // class name
        n.s.accept(this);  // main method body
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        symbolTable.putClass(n.i.s, new ClassScope());
        typeTable.putType(n.i.s, null); // If class does not extend anything, set value as void.
        n.i.accept(this);  // class name
        for ( int i = 0; i < n.vl.size(); i++ ) {
            String variable = n.vl.get(i).i.s;
            String t = getType(n.vl.get(i).t);
            symbolTable.getClassScope(n.i.s).putVariable(variable, t);
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            String method = n.ml.get(i).i.s;
            String t = getType(n.ml.get(i).t);
            symbolTable.getClassScope(n.i.s).putMethod(method, new MethodScope(t));
            for (int j = 0; j < n.ml.get(i).fl.size(); j++) {
                Formal f = n.ml.get(i).fl.get(j);
                String argumentType = getType(n.ml.get(i).fl.get(j).t);
                symbolTable.getClassScope(n.i.s).getMethodScope(method).insertArgument(new ArgumentType(f.i.s, argumentType));
            }
            for (int j = 0; j < n.ml.get(i).vl.size(); j++) {
                VarDecl vd = n.ml.get(i).vl.get(j);
                String variableType = getType(n.ml.get(i).vl.get(j).t);
                symbolTable.getClassScope(n.i.s).getMethodScope(method).putVariable(vd.i.s, variableType);
            }

            n.ml.get(i).accept(this);
        }
    }

    public String getType(Type t) {
        if (t instanceof BooleanType) {
            return "boolean";
        } else if (t instanceof IdentifierType) {
            return ((IdentifierType) t).s;
        } else if (t instanceof IntArrayType) {
            return "intArray";
        } else if (t instanceof IntegerType) {
            return "integer";
        }
        return null;  // should be unreachable
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        symbolTable.putClass(n.i.s, new ClassScope());
        typeTable.putType(n.i.s, n.j.s);
        n.i.accept(this);
        n.j.accept(this);
        for ( int i = 0; i < n.vl.size(); i++ ) {
            String variable = n.vl.get(i).i.s;
            String t = getType(n.vl.get(i).t);
            symbolTable.getClassScope(n.i.s).putVariable(variable, t);
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            String method = n.ml.get(i).i.s;
            String t = getType(n.ml.get(i).t);
            symbolTable.getClassScope(n.i.s).putMethod(method, new MethodScope(t));
            for (int j = 0; j < n.ml.get(i).fl.size(); j++) {
                Formal f = n.ml.get(i).fl.get(j);
                String argumentType = getType(n.ml.get(i).fl.get(j).t);
                symbolTable.getClassScope(n.i.s).getMethodScope(method).insertArgument(new ArgumentType(f.i.s, argumentType));
            }
            for (int j = 0; j < n.ml.get(i).vl.size(); j++) {
                VarDecl vd = n.ml.get(i).vl.get(j);
                String variableType = getType(n.ml.get(i).vl.get(j).t);
                symbolTable.getClassScope(n.i.s).getMethodScope(method).putVariable(vd.i.s, variableType);
            }

            n.ml.get(i).accept(this);
        }
    }

    // Type t;
    // Identifier i;
    // TODO
    // maybe have to account for this for generating symbol table (!)
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
    // TODO
    // maybe have to account for this for generating symbol table (!)
    public void visit(MethodDecl n) {
        n.i.accept(this);   // method name
        n.t.accept(this);   // return type

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
    // TODO
    // maybe have to account for this for generating symbol table (!)
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
        n.s1.accept(this); // if true
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
    public void visit(NewObject n) {
    }

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
    }
}