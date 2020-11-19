package SemanticsAndTypes;

import AST.*;
import AST.Visitor.Visitor;

import java.lang.reflect.Method;

public class ErrorCheckVisitor implements Visitor {

    private int counter = 0;
    public SymbolTable symbolTable;

    public ErrorCheckVisitor(Program root, SymbolTable st) {
        // TODO
        symbolTable = st;
        root.accept(this);
    }

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {
        System.out.print("display ");
        n.e.accept(this);
        System.out.print(";");
    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        System.out.println("Program");
        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            n.cl.get(i).accept(this);
        }
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        n.i1.accept(this);   // class name
        n.s.accept(this);  // main method body
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        System.out.print("  Class ");
        n.i.accept(this);  // class name  + extends ...
        System.out.println(" (line " + n.i.line_number + ")");
        counter += 2;
        if (n.vl.size() > 0) {
            System.out.println("    variables:");
        }
        for ( int i = 0; i < n.vl.size(); i++ ) {
            System.out.print("      ");
            n.vl.get(i).accept(this);
            if ( i < n.vl.size() ) { System.out.println(); }
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            System.out.print("    ");
            counter += 2;
            n.ml.get(i).accept(this);
            if(i+1 < n.ml.size()) {
                System.out.println(" (line " + n.ml.get(i).line_number + ")");
            } else {
                System.out.print(" (line " + n.ml.get(i).line_number + ")");
            }
            counter -= 2;
        }
        counter -= 2;
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        System.out.print("Class ");
        counter += 2;
        n.i.accept(this);
        System.out.print(" extends ");
        n.j.accept(this);
        System.out.println("(line " + n.j.line_number + ")");
        if (n.vl.size() > 0) {
            System.out.println("    variables:");
        }
        for ( int i = 0; i < n.vl.size(); i++ ) {
            System.out.print("      ");
            n.vl.get(i).accept(this);
            if ( i < n.vl.size() ) { System.out.println(); }
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            System.out.print("    ");
            counter += 2;
            n.ml.get(i).accept(this);
            if(i+1 < n.ml.size()) {
                System.out.println(" (line " + n.ml.get(i).line_number + ")");
            } else {
                System.out.print(" (line " + n.ml.get(i).line_number + ")");
            }
            counter -= 2;
        }
        counter -= 2;
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {
        n.t.accept(this);
        System.out.print(" ");
        n.i.accept(this);
    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        System.out.print("MethodDecl ");
        n.i.accept(this);   // method name
        System.out.println("line (" + n.i.line_number + ")");
        System.out.print("      returns ");
        n.t.accept(this);   // return type
        System.out.println();
        System.out.println("      parameters:");

        for ( int i = 0; i < n.fl.size(); i++ ) {
            System.out.print("        ");
            n.fl.get(i).accept(this);
            System.out.println();
        }
        if (n.vl.size() > 0) {
            System.out.println("      variables:");
        }
        for ( int i = 0; i < n.vl.size(); i++ ) {
            System.out.print("        ");
            n.vl.get(i).accept(this);
            System.out.println();
        }
        for ( int i = 0; i < n.sl.size(); i++ ) {
            System.out.print("      ");
            counter += 2;
            n.sl.get(i).accept(this);
            System.out.println();
            counter -= 2;
        }
        System.out.print("      Return ");
        n.e.accept(this);
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {
        n.t.accept(this);
        System.out.print(" ");
        n.i.accept(this);
    }

    public void visit(IntArrayType n) {
        System.out.print("int []");
    }

    public void visit(BooleanType n) {
        System.out.print("boolean");
    }

    public void visit(IntegerType n) {
        System.out.print("int");
    }

    // String s;
    public void visit(IdentifierType n) {
        System.out.print(n.s);
    }

    // StatementList sl;
    public void visit(Block n) {
        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.get(i).accept(this);
            if(i+1 < n.sl.size()) {
                System.out.println();
            }
        }
    }

    private boolean isBooleanExpression(Exp e) {
        if (e instanceof And) {
            return isBooleanExpression(((And) e).e1)
                    && isBooleanExpression(((And) e).e2);
        } else if (e instanceof LessThan) {
            return isArithmeticExpression(((LessThan) e).e1)
                    && isArithmeticExpression(((LessThan) e).e2);
        } else if (e instanceof Not) {
            return isBooleanExpression(((Not) e).e);
        } else if (e instanceof True) {
            return true;
        } else if (e instanceof False) {
            return true;
        } else if (e instanceof IdentifierExp) {
            return isVariableDefined((IdentifierExp) e, "boolean");
        } else if (e instanceof Call) {

        }
        return false;
    }

    public boolean isCall(Exp exp, ExpList expList) {
        // TODO
        if (exp instanceof Call) {
            return isCall(((Call) exp).e, null);
        }
        return false;
    }

    private boolean isArithmeticExpression(Exp e) {
        if (e instanceof Plus) {
            return isArithmeticExpression(((Plus) e).e1)
                    && isArithmeticExpression(((Plus) e).e2);
        } else if (e instanceof Minus) {
            return isArithmeticExpression(((Minus) e).e1)
                    && isArithmeticExpression(((Minus) e).e2);
        } else if (e instanceof Times) {
            return isArithmeticExpression(((Times) e).e1)
                    && isArithmeticExpression(((Times) e).e2);
        } else if (e instanceof IntegerLiteral) {
            return true;
        } else if (e instanceof IdentifierExp) {
            return isVariableDefined((IdentifierExp) e, "integer");
        } else if (e instanceof Call) {

        } else if (e instanceof ArrayLookup) {

        } else if (e instanceof ArrayLength) {

        }
        return false;
    }

    private boolean isMethodDefined(Identifier id, ExpList expList) {
        String methodName = id.s;
        for (String c : symbolTable.globalScope.keySet()) {
            ClassScope cs = symbolTable.getClassScope(c);
            if (cs.methodMap.containsKey(methodName)) {
                MethodScope ms = cs.getMethodScope(methodName);
                for (int i = 0; i < ms.arguments.size(); i++) {
                    ArgumentType at = ms.arguments.get(i);
                    Exp exp = expList.get(i);
                    // TODO
                    // string, integer, boolean
                    if (at.type.equals("integer")) {
                        if (!isArithmeticExpression(exp)) {
                            return false;
                        }
                    } else if (at.type.equals("boolean")) {
                        if (!isBooleanExpression(exp)) {
                            return false;
                        }
                    } else if (at.type.equals("intArray")) {

                    } else {
                        // TODO
                        // identifer case
                    }
                }
            }
        }
        return true;
    }

    private boolean isVariableDefined(Exp e, String type) {
        String id = ((IdentifierExp) e).s;
        for (String c : symbolTable.globalScope.keySet()) {
            ClassScope cs = symbolTable.getClassScope(c);
            for (String m : cs.methodMap.keySet()) {
                MethodScope ms = cs.getMethodScope(m);
                for (ArgumentType at : ms.arguments) {
                    if (at.name.equals(id)) {
                        if (at.type.equals(type)) {
                            return true;
                        }
                    }
                }
                if (ms.methodVariables.containsKey(id) && ms.methodVariables.get(id).equals(type)) {
                    return true;
                }
            }
            if (cs.variableMap.containsKey(id) && cs.variableMap.get(id).equals(type)) {
                return true;
            }
        }
        return false;
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        System.out.print("if ");


        n.e.accept(this);

        counter += 2;
        System.out.println();
        for(int i = 0; i < counter; i++) {
            System.out.print(" ");
        }
        n.s1.accept(this); // if true
        counter -= 2;
        System.out.println();
        for(int i = 0; i < counter; i++) {
            System.out.print(" ");
        }
        System.out.print("else ");
        System.out.println();
        counter += 2;
        for(int i = 0; i < counter; i++) {
            System.out.print(" ");
        }
        n.s2.accept(this);
        counter -= 2;
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        System.out.print("while ");
        n.e.accept(this);
        System.out.print("\n");
        counter += 2;
        for(int i = 0; i < counter; i++) {
            System.out.print(" ");
        }
        n.s.accept(this);
        counter -= 2;
    }

    // Exp e;
    public void visit(Print n) {
        System.out.println("Print (line " + n.e.line_number + ")");
        counter += 2;
        for(int i = 0; i < counter; i++) {
            System.out.print(" ");
        }
        n.e.accept(this);
        counter -= 2;
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        n.i.accept(this);
        System.out.print(" = ");
        n.e.accept(this);
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        n.i.accept(this);
        System.out.print("[");
        n.e1.accept(this);
        System.out.print("] = ");
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(And n) {
        System.out.print("(");
        n.e1.accept(this);
        System.out.print(" && ");
        n.e2.accept(this);
        System.out.print(")");
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        System.out.print("(");
        n.e1.accept(this);
        System.out.print(" < ");
        n.e2.accept(this);
        System.out.print(")");
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        System.out.print("(");
        n.e1.accept(this);
        System.out.print(" + ");
        n.e2.accept(this);
        System.out.print(")");
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        System.out.print("(");
        n.e1.accept(this);
        System.out.print(" - ");
        n.e2.accept(this);
        System.out.print(")");
    }

    // Exp e1,e2;
    public void visit(Times n) {
        System.out.print("(");
        n.e1.accept(this);
        System.out.print(" * ");
        n.e2.accept(this);
        System.out.print(")");
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        System.out.print("[");
        n.e2.accept(this);
        System.out.print("]");
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
        System.out.print(".length");
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);
        System.out.print(".");
        n.i.accept(this);
        System.out.print("(");
        for ( int i = 0; i < n.el.size(); i++ ) {
            n.el.get(i).accept(this);
            if ( i+1 < n.el.size() ) { System.out.print(", "); }
        }
        System.out.print(")");
    }

    // int i;
    public void visit(IntegerLiteral n) {
        System.out.print(n.i);
    }

    public void visit(True n) {
        System.out.print("true");
    }

    public void visit(False n) {
        System.out.print("false");
    }

    // String s;
    public void visit(IdentifierExp n) {
        System.out.print(n.s);
    }

    public void visit(This n) {
        System.out.print("this");
    }

    // Exp e;
    public void visit(NewArray n) {
        System.out.print("new int [");
        n.e.accept(this);
        System.out.print("]");
    }

    // Identifier i;
    public void visit(NewObject n) {
        System.out.print("new ");
        System.out.print(n.i.s);
        System.out.print("()");
    }

    // Exp e;
    public void visit(Not n) {
        System.out.print("!");
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
        System.out.print(n.s);
    }
}