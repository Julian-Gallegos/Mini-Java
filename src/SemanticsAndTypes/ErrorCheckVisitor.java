package SemanticsAndTypes;

import AST.*;
import AST.Visitor.Visitor;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class ErrorCheckVisitor implements Visitor {

    public SymbolTable symbolTable;
    public TypeTable typeTable;
    private String currentClass;
    private String currentMethod;

    public ErrorCheckVisitor(Program root, SymbolTable st, TypeTable tt) {
        // TODO
        symbolTable = st;
        typeTable = tt;
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
        currentClass = n.i1.s;  // TAG
        n.i1.accept(this);   // class name
        n.s.accept(this);  // main method body
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        currentClass = n.i.s;  // TAG
        n.i.accept(this);  // class name

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            currentMethod = n.ml.get(i).i.s;  // TAG
            n.ml.get(i).accept(this);
        }
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        currentClass = n.i.s;  // TAG
        n.i.accept(this); // class name
        n.j.accept(this); // extend class name

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            currentMethod = n.ml.get(i).i.s;  // TAG
            if (symbolTable.getClassScope(n.j.s).methodMap.containsKey(currentMethod)) {
                if (isDerived(symbolTable.getClassScope(n.j.s).getMethodScope(currentMethod).methodType, symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).methodType)) {
                    for (int j = 0; j < n.ml.get(i).fl.size(); j++) {
                        Type t = n.ml.get(i).fl.get(i).t;
                        String superType = symbolTable.getClassScope(n.j.s).getMethodScope(currentMethod).arguments.get(j).type;
                        if (!((       t instanceof BooleanType && superType.equals("boolean"))
                                || (t instanceof IntegerType && superType.equals("integer"))
                                || (t instanceof IntArrayType && superType.equals("intArray"))
                                || (t instanceof IdentifierType && superType.equals(((IdentifierType)t).s)))) {
                            System.out.println("Error (line " + n.ml.get(i).line_number + ") override method argument types do not match");
                        }
                    }
                } else {
                    System.out.println("Error (line " + n.ml.get(i).line_number + ") override method return type does not match");
                }
            }
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
        currentMethod = n.i.s;  // TAG
        n.i.accept(this);   // method name
        System.out.print("      returns ");
        // TODO
        // maybe check the return type of method
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
    public void visit(Formal n) {
        n.t.accept(this);
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
            return isVariableDefined(currentClass, currentMethod, (IdentifierExp) e, "integer");
        } else if (e instanceof Call) {
            return isCall(((Call) e).e, ((Call) e).i, ((Call) e).el, "integer");
        } else if (e instanceof ArrayLookup) {
            if (((ArrayLookup) e).e1 instanceof Call) {
                Call c = ((Call) ((ArrayLookup) e).e1);
                return isCall(c.e, c.i, c.el, "integer")
                        && arrayLookupCallHelper(((ArrayLookup) e).e2);
            } else if (((ArrayLookup) e).e1 instanceof IdentifierExp) {
                IdentifierExp ie = ((IdentifierExp) ((ArrayLookup) e).e1);
                return isVariableDefined(currentClass, currentMethod, ie, "integer")
                        && arrayLookupCallHelper(((ArrayLookup) e).e2);
            }
            return false;
        } else if (e instanceof ArrayLength) {
            // a . length
            if (((ArrayLength) e).e instanceof Call) {
                Call c = ((Call) ((ArrayLength) e).e);
                return isCall(c.e, c.i, c.el, "integer");
            } else if (((ArrayLength) e).e instanceof IdentifierExp) {
                IdentifierExp ie = ((IdentifierExp) ((ArrayLength) e).e);
                return isVariableDefined(currentClass, currentMethod, ie, "integer");
            }
            return false;
        }
        return false;
    }

    private boolean arrayLookupCallHelper(Exp exp) {
        // a    [0]
        // - call, id    - call, id, integer
        if (exp instanceof Call) {
            Call c = (Call) exp;
            return isCall(c.e, c.i, c.el, "integer");
        } else if (exp instanceof IntegerLiteral) {
            return true;
        } else if (exp instanceof IdentifierExp) {
            return isVariableDefined(currentClass, currentMethod, exp, "integer");
        }
        return false;
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
            return isVariableDefined(currentClass, currentMethod, (IdentifierExp) e, "boolean");
        } else if (e instanceof Call) {
            return isCall(((Call)e).e, ((Call)e).i, ((Call)e).el, "boolean");
        }
        return false;
    }

    // assuming that it does exist in the symboltable
    private String getMethodType(String className, Identifier id, ExpList expList) {
        String methodName = id.s;
        MethodScope ms = symbolTable.getClassScope(className).getMethodScope(methodName);
        for (int i = 0; i < ms.arguments.size(); i++) {
            ArgumentType at = ms.arguments.get(i);
            Exp exp = expList.get(i);
            if (at.type.equals("integer")) {
                if (!isArithmeticExpression(exp)) {
                    // TODO: some kind of printed error about args not matching required types
                    return null;
                }
            } else if (at.type.equals("boolean")) {
                if (!isBooleanExpression(exp)) {
                    // TODO: some kind of printed error
                    return null;
                }
            } else if (at.type.equals("intArray")) {
                if (exp instanceof Call) {
                    if(!isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, "intArray")) {
                        return null;
                    }
                } else if(exp instanceof IdentifierExp) {
                    if(!isVariableDefined(className, methodName, (IdentifierExp) exp, "intArray")) {
                        return null;
                    }
                } else {
                    // TODO: some kind of printed error
                    return null;
                }
            } else if (at.type.equals(typeTable.getType(at.type))) {
                // identifier case
                if (exp instanceof Call) {
                    if (!isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, at.type)) {
                        return null;
                    }
                } else if(exp instanceof IdentifierExp) {
                    if (!isVariableDefined(className, methodName, (IdentifierExp) exp, at.type)) {
                        return null;
                    }
                } else {
                    // TODO: some kind of printed error
                    return null;
                }
            }
        }
        return ms.methodType;
    }

    private String isCallHelper(Exp exp, Identifier id, ExpList expList) {
        if (exp instanceof Call) {
            // recursive shit
            String classname = isCallHelper(((Call)exp).e,((Call)exp).i,((Call)exp).el);
            if (!isExtendedFrom(classname, id.s)) {
                return null;
            }
            /*
            if (!symbolTable.getClassScope(classname).methodMap.containsKey(id.s)) {
                //TODO: error
                return null;
            }
             */
            return getMethodType(classname, id, expList);
        } else if (exp instanceof IdentifierExp) {
            if (!typeTable.types.containsKey(((IdentifierExp)exp).s)) {
                //TODO: error classname does not exist in scope
                return null;
            }
            if (!isExtendedFrom(((IdentifierExp) exp).s, id.s)) {
                return null;
            }
            /*
            if (!symbolTable.getClassScope(((IdentifierExp)exp).s).methodMap.containsKey(id.s)) {
                //TODO: error
                return null;
            }
             */
            return getMethodType(((IdentifierExp)exp).s, id, expList);

        }
        // Should never this as parser would already complain.
        return null;
    }
    private boolean isCall(Exp exp, Identifier id, ExpList expList, String type) {
        // check 'exp' recursively to make sure that it leads to a valid exp.
        //      - could be an identifier
        //      - nested exp. (ex. a.b.c.d)
        // if its not valid return false, else..
        // make sure that the id is a proper method of the class exp
        // make sure that the return type of exp is a type class that can call id


        // a.b(2,2)
        if (exp instanceof Call) {
            String classname = isCallHelper(exp, id, expList);
            if (classname == null) {
                //TODO: PRINT ERROR
                return false;
            }
            if (!isExtendedFrom(classname, id.s)) {
                return false;
            }
            /*
            if (!symbolTable.getClassScope(classname).methodMap.containsKey(id.s)) {
                //TODO: PRINT ERROR
                return false;
            }
             */
            return isMethodDefined(classname, id, expList, type);
        } else if (exp instanceof IdentifierExp) {
            if (!typeTable.types.containsKey(((IdentifierExp)exp).s)) {
                //TODO: error classname does not exist in scope
                return false;
            }
            if (!isExtendedFrom(((IdentifierExp) exp).s, id.s)) {
                //TODO: PRINT ERROR
                return false;
            }
            return isMethodDefined(((IdentifierExp)exp).s, id, expList, type);

        }
        return false;
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

    private boolean isMethodDefined(String className, Identifier id, ExpList expList, String type) {
        String methodName = id.s;
        ClassScope cs = symbolTable.getClassScope(className);
        if (cs.methodMap.containsKey(methodName)) {
            MethodScope ms = cs.getMethodScope(methodName);

            if (!isDerived(type, ms.methodType)) {
                // TODO: error for method type mismatch
                System.out.println("Error (line " + id.line_number + ") method return type invalid");
                return false;
            }
            if (ms.arguments.size() != expList.size()) {
                // TODO: some kind of printed error to describe provided method arguments mismatch
                return false;
            }
            for (int i = 0; i < ms.arguments.size(); i++) {
                ArgumentType at = ms.arguments.get(i);
                Exp exp = expList.get(i);
                // TODO
                // string, integer, boolean
                if (at.type.equals("integer")) {
                    if (!isArithmeticExpression(exp)) {
                        // TODO: some kind of printed error
                        return false;
                    }
                } else if (at.type.equals("boolean")) {
                    if (!isBooleanExpression(exp)) {
                        // TODO: some kind of printed error
                        return false;
                    }
                } else if (at.type.equals("intArray")) {
                    if (exp instanceof Call) {
                        return isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, "intArray");
                    } else if(exp instanceof IdentifierExp) {
                        return isVariableDefined(className, methodName, (IdentifierExp) exp, "intArray");
                    } else {
                        // TODO: some kind of printed error
                        return false;
                    }
                } else if (at.type.equals(typeTable.getType(at.type))) {
                    // identifier case
                    if (exp instanceof Call) {
                        return isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, at.type);
                    } else if(exp instanceof IdentifierExp) {
                        return isVariableDefined(className, methodName, (IdentifierExp) exp, at.type);
                    } else {
                        // TODO: some kind of printed error
                        return false;
                    }
                }
            }
        } else {
            // TODO: system.exit maybe, or print error about undefined method and return false.
            return false;
        }
        return true;
    }

    private boolean isVariableDefined(String className, String methodName, Exp e, String type) {
        String id = ((IdentifierExp) e).s;
        ClassScope cs = symbolTable.getClassScope(className);
        MethodScope ms = cs.getMethodScope(methodName);
        for (ArgumentType at : ms.arguments) {
            if (at.name.equals(id)) {
                if (isDerived(type, at.type)) {
                    return true;
                }
            }
        }
        if (ms.methodVariables.containsKey(id) && isDerived(type, ms.methodVariables.get(id))) {
            return true;
        }
        if (cs.variableMap.containsKey(id) && isDerived(type, cs.variableMap.get(id))) {
            return true;
        }
        return false;
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        if (!isBooleanExpression(n.e)) {
            System.out.println("Error (line " + n.e.line_number + ") condition is not of type boolean");
        }
        n.e.accept(this);
        n.s1.accept(this); // if true
        n.s2.accept(this);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        if (!isBooleanExpression(n.e)) {
            System.out.println("Error (line " + n.e.line_number + ") conditional not boolean");
        }
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
        if (!areEqualTypes(n.e, lookupTypeForID(n.i.s))) {
            System.out.println("Error (line " + n.i.line_number + ") types do not match");
        }
        n.e.accept(this);
    }

    private String lookupTypeForID(String id) {
        String type = symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).getVariableType(id);
        if (type == null) {
            type = symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).getParameterType(id);
            if (type == null) {
                return symbolTable.getClassScope(currentClass).getVariableType(id);
            }
        }
        return type;
    }

    //                            RHS      LHS
    private boolean areEqualTypes(Exp exp, String type) {
        if (type.equals("boolean")) {
            return isBooleanExpression(exp);
        } else if (type.equals("integer")) {
            return isArithmeticExpression(exp);
        } else if (type.equals(typeTable.getType(type))) {
            if (exp instanceof Call) {
                return isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, type);
            } else if (exp instanceof IdentifierExp) {
                return isDerived(type, lookupTypeForID(((IdentifierExp) exp).s));
            }
            return false;
        } else if (type.equals("intArray")) {
            if (exp instanceof Call) {
                return isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, type);
            } else if(exp instanceof IdentifierExp) {
                return isVariableDefined(currentClass, currentMethod, (IdentifierExp) exp, type);
            }
            return false;
        }
        return false;
    }

    //                        argument type      variable type
    private boolean isDerived(String leftAssign, String rightAssign) {
        if (leftAssign.equals(rightAssign)) {
            return true;
        }
        String superType = typeTable.getType(rightAssign);
        if (superType != null) {
            return isDerived(leftAssign, superType);
        }
        return false;
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        n.i.accept(this);
        if (!(lookupTypeForID(n.i.s).equals("intArray")
                && isArithmeticExpression(n.e1) && isArithmeticExpression(n.e2))) {
            System.out.println("Error: (line " + n.i.line_number + ") array assign failed");
        }
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(And n) {
        n.e1.accept(this);
        if (!(isBooleanExpression(n.e1) && isBooleanExpression(n.e2))) {
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        n.e1.accept(this);
        if (!(isArithmeticExpression(n.e1) && isArithmeticExpression(n.e2))) {
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        n.e1.accept(this);
        if (!(isArithmeticExpression(n.e1) && isArithmeticExpression(n.e2))) {
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        if (!(isArithmeticExpression(n.e1) && isArithmeticExpression(n.e2))) {
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        if (!(isArithmeticExpression(n.e1) && isArithmeticExpression(n.e2))) {
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);

        if (!isArray(n.e1)) {
            System.out.println("Error: (line " + n.e2.line_number + ") array described is not valid");
        }
        if (!isArithmeticExpression(n.e2)) {
            System.out.println("Error: (line " + n.e2.line_number + ") expression in bracket is not of integer type");
        }
        n.e2.accept(this);
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
        if (!isArray(n.e)) {
            System.out.println("Error: (line " + n.e.line_number + ") array described is not valid");
        }
    }

    private boolean isArray(Exp exp) {
        if (((ArrayLookup) exp).e1 instanceof Call) {
            Call c = ((Call) ((ArrayLookup) exp).e1);
            return isCall(c.e, c.i, c.el, "integer")
                    && arrayLookupCallHelper(((ArrayLookup) exp).e2);
        } else if (((ArrayLookup) exp).e1 instanceof IdentifierExp) {
            IdentifierExp ie = ((IdentifierExp) ((ArrayLookup) exp).e1);
            return isVariableDefined(currentClass, currentMethod, ie, "integer")
                    && arrayLookupCallHelper(((ArrayLookup) exp).e2);
        }
        return false;
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        if (isCallHelper(n.e, n.i, n.el) == null) {
            System.out.println("Error: (line " + n.e.line_number + ") invalid call");
        }
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
        if (!isArithmeticExpression(n.e)) {
          System.out.println("Error (line " + n.e.line_number + ") expression not of type integer");
        }
        n.e.accept(this);
    }

    // Identifier i;
    public void visit(NewObject n) {
        System.out.print("new ");
        if (symbolTable.getClassScope(n.i.s) == null) {
            System.out.println("Error (line " + n.i.line_number + ") class not found");
        }
        System.out.print(n.i.s);
    }

    // Exp e;
    public void visit(Not n) {
        if (!(isBooleanExpression(n.e))) {
            System.out.println("Error: (line " + n.e.line_number + ") not boolean expression");
        }
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
    }
}