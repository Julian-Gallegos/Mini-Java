package SemanticsAndTypes;

import AST.*;
import AST.Visitor.Visitor;

import javax.lang.model.type.ArrayType;
import java.beans.Expression;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

public class ErrorCheckVisitor implements Visitor {

    public SymbolTable symbolTable;
    public TypeTable typeTable;
    private String currentClass;
    private String currentMethod;

    private int errorCounter;

    public ErrorCheckVisitor(Program root, SymbolTable st, TypeTable tt) {
        errorCounter = 0;
        symbolTable = st;
        typeTable = tt;
        root.accept(this);
    }

    public boolean errorsInProgram() {
        return (errorCounter > 0);
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
            if (symbolTable.getClassScope(currentClass).instanceVariableCount.get(n.vl.get(i).i.s) != 1) {
                System.out.println("Error: (Line " + n.vl.get(i).line_number + ") variable " + n.vl.get(i).i.s + " is already defined.");
                errorCounter++;
            }
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            currentMethod = n.ml.get(i).i.s;

            for (int j = 0; j < n.ml.get(i).fl.size(); j++) {
                String argumentName = n.ml.get(i).fl.get(j).i.s;
                // check if argument is declared already as a field
                if (symbolTable.getClassScope(currentClass).instanceVariableCount.containsKey(argumentName)) {
                    System.out.println("Error: (Line " + n.ml.get(i).fl.get(j).line_number + ") argument " + argumentName + " is already defined as field.");
                    errorCounter++;
                }
                if (symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).variableDeclarationCount.get(argumentName) != 1) {
                    System.out.println("Error: (Line " + n.ml.get(i).fl.get(j).line_number + ") argument " + argumentName + " is already defined in method scope.");
                    errorCounter++;
                }
            }

            // checking the method variables
            for (int j = 0; j < n.ml.get(i).vl.size(); j++) {
                String methodVarName = n.ml.get(i).vl.get(j).i.s;

                // check if variable in method is declared already as a field
                if (symbolTable.getClassScope(currentClass).instanceVariableCount.containsKey(methodVarName)) {
                    System.out.println("Error: (Line " + n.ml.get(i).vl.get(j).line_number + ") variable " + methodVarName + " is already defined as field.");
                    errorCounter++;
                }
                if (symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).variableDeclarationCount.get(methodVarName) != 1) {
                    System.out.println("Error: (Line " + n.ml.get(i).vl.get(j).line_number + ") variable " + methodVarName + " is already defined in method scope.");
                    errorCounter++;
                }
            }
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
        if (!checkLoopInheritance(n.i.s, new HashSet<>())) {
            // if loop encountered, abandon ship
            System.exit(1);
        }
        n.j.accept(this); // extend class name

        for ( int i = 0; i < n.vl.size(); i++ ) {
            String fieldName = n.vl.get(i).i.s;
            // check for fieldname in superclass of any depth
            if (fieldFoundInSuper(fieldName, currentClass)) {
                System.out.println("Error: (Line " + n.vl.get(i).line_number + ") field " + fieldName + " already inherited.");
                errorCounter++;
            }
            // check if its already defined in class scope
            if (symbolTable.getClassScope(currentClass).instanceVariableCount.get(fieldName) != 1) {
                System.out.println("Error: (Line " + n.vl.get(i).line_number + ") variable " + fieldName + " is already defined.");
                errorCounter++;
            }
            n.vl.get(i).accept(this);
        }
        if (symbolTable.getClassScope(n.j.s) == null) {
            System.out.println("Error: (Line " + n.line_number + ") superclass " + n.j.s + " does not exist.");
            errorCounter++;
        }
        for ( int i = 0; i < n.ml.size(); i++ ) {
            currentMethod = n.ml.get(i).i.s;

            if (symbolTable.getClassScope(n.j.s) != null) {
                for (int j = 0; j < n.ml.get(i).fl.size(); j++) {
                    String argumentName = n.ml.get(i).fl.get(j).i.s;
                    if (fieldFoundInSuper(argumentName, currentClass)) {
                        System.out.println("Error: (Line " + n.ml.get(i).fl.get(j).line_number + ") argument " + argumentName + " already inherited.");
                        errorCounter++;
                    }

                    if (symbolTable.getClassScope(currentClass).instanceVariableCount.containsKey(argumentName)) {
                        System.out.println("Error: (Line " + n.ml.get(i).fl.get(j).line_number + ") argument " + argumentName + " is already defined as field.");
                        errorCounter++;
                    }

                    if (symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).variableDeclarationCount.get(argumentName) != 1) {
                        System.out.println("Error: (Line " + n.ml.get(i).fl.get(j).line_number + ") argument " + argumentName + " is already defined in method scope.");
                        errorCounter++;
                    }
                }

                for (int j = 0; j < n.ml.get(i).vl.size(); j++) {
                    String variableName = n.ml.get(i).vl.get(j).i.s;

                    if (fieldFoundInSuper(variableName, currentClass)) {
                        System.out.println("Error: (Line " + n.ml.get(i).vl.get(j).line_number + ") argument " + variableName + " already inherited.");
                        errorCounter++;
                    }

                    // check if variable in method is declared already as a field
                    if (symbolTable.getClassScope(currentClass).instanceVariableCount.containsKey(variableName)) {
                        System.out.println("Error: (Line " + n.ml.get(i).vl.get(j).line_number + ") variable " + variableName + " is already defined as field.");
                        errorCounter++;
                    }
                    if (symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).variableDeclarationCount.get(variableName) != 1) {
                        System.out.println("Error: (Line " + n.ml.get(i).vl.get(j).line_number + ") variable " + variableName + " is already defined in method scope.");
                        errorCounter++;
                    }

                }

                if (symbolTable.getClassScope(n.j.s).methodMap.containsKey(currentMethod)) {
                    if (isDerived(symbolTable.getClassScope(n.j.s).getMethodScope(currentMethod).methodType, symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).methodType)) {
                        for (int j = 0; j < n.ml.get(i).fl.size(); j++) {
                            Type t = n.ml.get(i).fl.get(i).t;
                            String superType = symbolTable.getClassScope(n.j.s).getMethodScope(currentMethod).arguments.get(j).type;
                            if (!((t instanceof BooleanType && superType.equals("boolean"))
                                    || (t instanceof IntegerType && superType.equals("integer"))
                                    || (t instanceof IntArrayType && superType.equals("intArray"))
                                    || (t instanceof IdentifierType && superType.equals(((IdentifierType) t).s)))) {
                                errorCounter++;
                                System.out.println("Error (line " + n.ml.get(i).line_number + ") override method argument types do not match");
                            }
                        }
                    } else {
                        errorCounter++;
                        System.out.println("Error (line " + n.ml.get(i).line_number + ") override method return type does not match");
                    }
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
        n.t.accept(this);   // return type

        for ( int i = 0; i < n.fl.size(); i++ ) {

	        String idLookup = lookupTypeForID(n.fl.get(i).i.s);
	        if (idLookup != null && !typeTable.types.containsKey(idLookup)) {
                System.out.println("Error: (Line " + n.fl.line_number + ") argument type " + idLookup + " is not defined.");
                errorCounter++;
            }

            n.fl.get(i).accept(this);
        }
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }
        for ( int i = 0; i < n.sl.size(); i++ ) {
            n.sl.get(i).accept(this);
        }

        checkTypesMatch(n.e, n.t);
        n.e.accept(this);
    }

    public void checkTypesMatch(Exp e, Type t) {
        if (isArithmeticExpression(e, false)) {
            if (!(t instanceof IntegerType)) {
                System.out.println("Error: (line " + e.line_number + ") return types don't match.");
                errorCounter++;
            }
        } else if (isBooleanExpression(e)) {
            if (!(t instanceof BooleanType)) {
                System.out.println("Error: (line " + e.line_number + ") return types don't match.");
                errorCounter++;
            }
        } else if (e instanceof Call) {
            if (isCall(((Call) e).e, ((Call) e).i, ((Call) e).el, "intArray", false)) {
                if (!(t instanceof IntArrayType)) {
                    System.out.println("Error: (line " + e.line_number + ") return types don't match.");
                    errorCounter++;
                }
            }
            if (t instanceof IdentifierType) {
                if (!isCall(((Call) e).e, ((Call) e).i, ((Call) e).el, ((IdentifierType)t).s, false)) {
                    System.out.println("Error: (line " + e.line_number + ") return types don't match.");
                    errorCounter++;
                }
            } else {
                // some sort of error
            }
        } else if (e instanceof IdentifierExp) {
            if (t instanceof IdentifierType) {
                String lookup = lookupTypeForID(((IdentifierExp) e).s);
                if (lookup == null) {
                    System.out.println("Error: (Line " + e.line_number + ") " + (((IdentifierExp) e).s) + " is an undeclared variable.");
                    errorCounter++;
                } else if (!((IdentifierType) t).s.equals(lookup)) {
                    System.out.println("Error: (line " + e.line_number + ") return types don't match.");
                    errorCounter++;
                }
            } else {
                // some sort of error
            }
        }
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

    private boolean isArithmeticExpression(Exp e, boolean printError) {
        if (e instanceof Plus) {
            return isArithmeticExpression(((Plus) e).e1, true)
                    && isArithmeticExpression(((Plus) e).e2, true);
        } else if (e instanceof Minus) {
            return isArithmeticExpression(((Minus) e).e1, true)
                    && isArithmeticExpression(((Minus) e).e2, true);
        } else if (e instanceof Times) {
            return isArithmeticExpression(((Times) e).e1, true)
                    && isArithmeticExpression(((Times) e).e2, true);
        } else if (e instanceof IntegerLiteral) {
            return true;
        } else if (e instanceof IdentifierExp) {
            return isVariableDefined(currentClass, currentMethod, e, "integer");
        } else if (e instanceof Call) {
            return isCall(((Call) e).e, ((Call) e).i, ((Call) e).el, "integer", printError);
        } else if (e instanceof ArrayLookup) {
            if (((ArrayLookup) e).e1 instanceof Call) {
                Call c = ((Call) ((ArrayLookup) e).e1);
                return isCall(c.e, c.i, c.el, "intArray", true)
                        && isArithmeticExpression(((ArrayLookup) e).e2, true);
            } else if (((ArrayLookup) e).e1 instanceof IdentifierExp) {
                IdentifierExp ie = ((IdentifierExp) ((ArrayLookup) e).e1);
                return isVariableDefined(currentClass, currentMethod, ie, "intArray")
                        && isArithmeticExpression(((ArrayLookup) e).e2, true);
            }
            return false;
        } else if (e instanceof ArrayLength) {
            // a . length
            if (((ArrayLength) e).e instanceof Call) {
                Call c = ((Call) ((ArrayLength) e).e);
                return isCall(c.e, c.i, c.el, "intArray", true);
            } else if (((ArrayLength) e).e instanceof IdentifierExp) {
                IdentifierExp ie = ((IdentifierExp) ((ArrayLength) e).e);
                return isVariableDefined(currentClass, currentMethod, ie, "intArray");
            }
            return false;
        }
        return false;
    }

    private boolean isBooleanExpression(Exp e) {
        if (e instanceof And) {
            return isBooleanExpression(((And) e).e1)
                    && isBooleanExpression(((And) e).e2);
        } else if (e instanceof LessThan) {
            return isArithmeticExpression(((LessThan) e).e1, true)
                    && isArithmeticExpression(((LessThan) e).e2, true);
        } else if (e instanceof Not) {
            return isBooleanExpression(((Not) e).e);
        } else if (e instanceof True) {
            return true;
        } else if (e instanceof False) {
            return true;
        } else if (e instanceof IdentifierExp) {
            return isVariableDefined(currentClass, currentMethod, e, "boolean");
        } else if (e instanceof Call) {
            return isCall(((Call)e).e, ((Call)e).i, ((Call)e).el, "boolean", false);
        }
        return false;
    }

    private String isCallHelper(Exp exp, Identifier id, ExpList expList) {
        if (exp instanceof Call) {
            String classname = isCallHelper(((Call)exp).e,((Call)exp).i,((Call)exp).el);
            if (classname == null || !isExtendedFrom(classname, id.s)) {
                errorCounter++;
                System.out.println("Error (line " + exp.line_number + ") Method: " + id.s + " does not exist in scope");
                return null;
            }
            return getMethodType(classname, id, expList);
        } else if (exp instanceof IdentifierExp) {
            String variableClass = lookupTypeForID(((IdentifierExp) exp).s);
            if (variableClass == null) {
                errorCounter++;
                System.out.println("Error (line " + exp.line_number + ") Variable: " + ((IdentifierExp) exp).s + " does not exist in scope");
                return null;
            }
            if (!isExtendedFrom(variableClass, id.s)) {
                errorCounter++;
                System.out.println("Error (line " + exp.line_number + ") Method: " + id.s + " not in scope of " + variableClass);
                return null;
            }
            return getMethodType(variableClass, id, expList);
        } else if (exp instanceof This) {
            if (!isExtendedFrom(currentClass, id.s)) {
                errorCounter++;
                System.out.println("Error (line " + exp.line_number + ") Method: " + id.s + " not in scope of current class");
                return null;
            }
            return getMethodType(currentClass, id, expList);
        } else if (exp instanceof NewObject) {
            if(!typeTable.types.containsKey(((NewObject) exp).i.s)) {
                errorCounter++;
                System.out.println("Error (line " + exp.line_number + ") Class: " + ((NewObject) exp).i.s + " does not exist in scope");
                return null;
            }
            return getMethodType(((NewObject) exp).i.s, id, expList);
        } else if (exp instanceof NewArray) {
            // MiniJava array only has the .length field.
            if (!isArithmeticExpression(((NewArray) exp).e, true) || !id.s.equals("length")) {
                errorCounter++;
                System.out.println("Error (line " + exp.line_number + ") Invalid Array.length call");
                return null;
            }
            return "integer";
        }
        // Should never get to this as parser would already complain.
        return null;
    }
    private boolean isCall(Exp exp, Identifier id, ExpList expList, String type, boolean printError) {
        // check 'exp' recursively to make sure that it leads to a valid exp.
        //      - could be an identifier
        //      - nested exp. (ex. a.b.c.d)
        // if its not valid return false, else..
        // make sure that the id is a proper method of the class exp
        // make sure that the return type of exp is a type class that can call id

        // a.b(2,2)
        if (exp instanceof Call) {
            String classname = isCallHelper(((Call)exp).e, ((Call)exp).i, ((Call)exp).el);
            if (classname == null) {
                return false;
            }
            if (!isExtendedFrom(classname, id.s)) {
                return false;
            }
            return isMethodDefined(classname, id, expList, type, printError);
        } else if (exp instanceof IdentifierExp) {
            String variableClass = lookupTypeForID(((IdentifierExp) exp).s);
            if (variableClass == null) {
                if (printError) {
                    errorCounter++;
                    System.out.println("Error (line " + exp.line_number + ") Variable: " + ((IdentifierExp) exp).s + " does not exist in scope");
                }
                return false;
            }
            if (!isExtendedFrom(variableClass, id.s)) {
                if (printError) {
                    errorCounter++;
                    System.out.println("Error (line " + exp.line_number + ") Method: " + id.s + " not in scope of " + variableClass);
                }
                return false;
            }
            return isMethodDefined(variableClass, id, expList, type, printError);

        } else if (exp instanceof This) {
            if (!isExtendedFrom(currentClass, id.s)) {
                if (printError) {
                    errorCounter++;
                    System.out.println("Error (line " + exp.line_number + ") Method: " + id.s + " not in scope of current class");
                }
                return false;
            }
            return isMethodDefined(currentClass, id, expList, type, printError);
        } else if (exp instanceof NewObject) {
            if(!typeTable.types.containsKey(((NewObject) exp).i.s)) {
                if (printError) {
                    errorCounter++;
                    System.out.println("Error (line " + exp.line_number + ") Class: " + ((NewObject) exp).i.s + " does not exist");
                }
                return false;
            }
            return isMethodDefined(((NewObject) exp).i.s, id, expList, type, printError);
        } else if (exp instanceof NewArray) {
            // MiniJava array only has the .length field.
            return isArithmeticExpression(((NewArray) exp).e, true) && id.s.equals("length");
        }
        return false;
    }

    // returns true if fieldname is found in a super class of any depth, false otherwise.
    private boolean fieldFoundInSuper(String fieldName, String cn) {
        String extendedClass = typeTable.getType(cn);
        if (extendedClass != null) {
            if (symbolTable.getClassScope(extendedClass).instanceVariableCount.containsKey(fieldName)) {
                return true;
            }
            return fieldFoundInSuper(fieldName, extendedClass);
        }
        return false;
    }

    private String findClassForMethodInScope(String mn, String cn) {
        String extendedClass = typeTable.getType(cn);
        if (extendedClass != null) {
            if (symbolTable.getClassScope(extendedClass).methodMap.containsKey(mn)) {
                return extendedClass;
            }
            return findClassForMethodInScope(mn, extendedClass);
        }
        return null;
    }

    private boolean isMethodDefined(String className, Identifier id, ExpList expList, String type, boolean printError) {
        String methodName = id.s;
        ClassScope cs = symbolTable.getClassScope(className);
        String extendedClass = findClassForMethodInScope(methodName, className);
        if (cs.methodMap.containsKey(methodName)) {
            MethodScope ms = cs.getMethodScope(methodName);

            if (!isDerived(type, ms.methodType)) {
                if (printError) {
                    errorCounter++;
                    System.out.println("Error: (line " + id.line_number + ") method return type invalid");
                }
                return false;
            }
            if (ms.arguments.size() != expList.size()) {
                errorCounter++;
                System.out.println("Error: (line " + id.line_number + ") method " + methodName + " expected " + ms.arguments.size() + " arguments. "
                        + expList.size() + " arguments given");
                return false;
            }
            for (int i = 0; i < ms.arguments.size(); i++) {
                ArgumentType at = ms.arguments.get(i);
                Exp exp = expList.get(i);
                // string, integer, boolean
                if (at.type.equals("integer")) {
                    if (!isArithmeticExpression(exp, true)) {
                        errorCounter++;
                        System.out.println("Error: (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return false;
                    }
                } else if (at.type.equals("boolean")) {
                    if (!isBooleanExpression(exp)) {
                        errorCounter++;
                        System.out.println("Error: (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return false;
                    }
                } else if (at.type.equals("intArray")) {
                    if (exp instanceof Call) {
                        if(!isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, "intArray", true)) {
                            errorCounter++;
                            System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                            return false;
                        }
                    } else if(exp instanceof IdentifierExp) {
                        if (!isVariableDefined(className, methodName, exp, "intArray")) {
                            errorCounter++;
                            System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                            return false;
                        }
                    } else {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return false;
                    }
                } else if (typeTable.types.containsKey(at.type)) {
                    // identifier case
                    if (exp instanceof Call) {
                        return isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, at.type, printError);
                    } else if(exp instanceof IdentifierExp) {
                        String variableType = lookupTypeForID(((IdentifierExp) exp).s);
                        if (!(variableType != null && isDerived(at.type, variableType))) {
                            errorCounter++;
                            System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                            return false;
                        }
                    } else if (exp instanceof This) {
                        if(!isDerived(at.type, currentClass)) {
                            errorCounter++;
                            System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                            return false;
                        }
                    } else {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return false;
                    }
                }
            }
        } else if (extendedClass != null) {
            MethodScope ms = symbolTable.getClassScope(extendedClass).getMethodScope(methodName);

            if (!isDerived(type, ms.methodType)) {
                if (printError) {
                    errorCounter++;
                    System.out.println("Error (line " + id.line_number + ") method return type invalid");
                }
                return false;
            }
            if (ms.arguments.size() != expList.size()) {
                errorCounter++;
                System.out.println("Error (line " + id.line_number + ") method " + methodName + " expected " + ms.arguments.size() + " arguments. "
                        + expList.size() + " arguments given");
                return false;
            }
            for (int i = 0; i < ms.arguments.size(); i++) {
                ArgumentType at = ms.arguments.get(i);
                Exp exp = expList.get(i);
                // string, integer, boolean
                if (at.type.equals("integer")) {
                    if (!isArithmeticExpression(exp, true)) {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return false;
                    }
                } else if (at.type.equals("boolean")) {
                    if (!isBooleanExpression(exp)) {
                            errorCounter++;
                            System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return false;
                    }
                } else if (at.type.equals("intArray")) {
                    if (exp instanceof Call) {
                        if(!isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, "intArray", true)) {
                            errorCounter++;
                            System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                            return false;
                        }
                    } else if(exp instanceof IdentifierExp) {
                        if (!isVariableDefined(className, methodName, exp, "intArray")) {
                            errorCounter++;
                            System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                            return false;
                        }
                    } else {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return false;
                    }
                } else if (typeTable.types.containsKey(at.type)) {
                    // identifier case
                    if (exp instanceof Call) {
                        return isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, at.type, printError);
                    } else if(exp instanceof IdentifierExp) {
                        String variableType = lookupTypeForID(((IdentifierExp) exp).s);
                        if (!(variableType != null && isDerived(at.type, variableType))) {
                            errorCounter++;
                            System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                            return false;
                        }
                    } else if (exp instanceof This) {
                        if(!isDerived(at.type, currentClass)) {
                            errorCounter++;
                            System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                            return false;
                        }
                    } else {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return false;
                    }
                }
            }
        } else {
            errorCounter++;
            System.out.println("Error (line " + id.line_number + ") Method: " + id.s + " not defined in scope");
            return false;
        }
        return true;
    }

    // assuming that it does exist in the SymbolTable
    private String getMethodType(String className, Identifier id, ExpList expList) {
        String methodName = id.s;
        String extendedClass = findClassForMethodInScope(methodName, className);
        MethodScope ms;
        if (extendedClass != null){
            ms = symbolTable.getClassScope(extendedClass).getMethodScope(methodName);
        } else if (symbolTable.getClassScope(className).methodMap.containsKey(methodName)) {
            ms = symbolTable.getClassScope(className).getMethodScope(methodName);
        } else {
            errorCounter++;
            System.out.println("Error (line " + id.line_number + ") Method " + id.s + " not defined in scope");
            return null;
        }

        if (ms.arguments.size() != expList.size()) {
            errorCounter++;
            System.out.println("Error (line " + id.line_number + ") method " + methodName + " expected " + ms.arguments.size() + " arguments. "
                    + expList.size() + " arguments given");

            return null;
        }

        for (int i = 0; i < ms.arguments.size(); i++) {
            ArgumentType at = ms.arguments.get(i);
            Exp exp = expList.get(i);
            if (at.type.equals("integer")) {
                if (!isArithmeticExpression(exp, true)) {
                    errorCounter++;
                    System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                    return null;
                }
            } else if (at.type.equals("boolean")) {
                if (!isBooleanExpression(exp)) {
                    errorCounter++;
                    System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                    return null;
                }
            } else if (at.type.equals("intArray")) {
                if (exp instanceof Call) {
                    if(!isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, "intArray", true)) {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return null;
                    }
                } else if(exp instanceof IdentifierExp) {
                    if(!isVariableDefined(className, methodName, exp, "intArray")) {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return null;
                    }
                } else {
                    errorCounter++;
                    System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                    return null;
                }
            } else if (typeTable.types.containsKey(at.type)) {
                // identifier case
                if (exp instanceof Call) {
                    if (!isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, at.type, true)) {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return null;
                    }
                } else if(exp instanceof IdentifierExp) {
                    String variableType = lookupTypeForID(((IdentifierExp) exp).s);
                    if (!(variableType != null && isDerived(at.type, variableType))) {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return null;
                    }
                } else if (exp instanceof This) {
                    if(!isDerived(at.type, currentClass)) {
                        errorCounter++;
                        System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                        return null;
                    }
                } else {
                    errorCounter++;
                    System.out.println("Error (line " + exp.line_number + ") method argument number " + i + " has type mismatch");
                    return null;
                }
            }
        }
        return ms.methodType;
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

        //System.out.println("Classname: " + className + " Methodname: " + methodName + " ID: " + id + " Type: " + type);
        //System.out.println("Line number: " + e.line_number);
        if (ms.methodVariables.containsKey(id) && isDerived(type, ms.methodVariables.get(id))) {
            return true;
        }
        if (cs.variableMap.containsKey(id) && isDerived(type, cs.variableMap.get(id))) {
            return true;
        }
        if (fieldFoundInSuper(id, className)) {
            return true;
        }
        //new Exception().printStackTrace(System.out);
        //System.out.println("Error: (Line " + e.line_number + ") variable " + id + " is not defined.");
        //errorCounter++;
        return false;
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        if (!isBooleanExpression(n.e)) {
            errorCounter++;
            System.out.println("Error: (line " + n.e.line_number + ") condition is not of type boolean");
        }
        n.e.accept(this);
        n.s1.accept(this); // if true
        n.s2.accept(this);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        if (!isBooleanExpression(n.e)) {
            errorCounter++;
            System.out.println("Error: (line " + n.e.line_number + ") conditional not boolean");
        }
        n.e.accept(this);
        n.s.accept(this);
    }

    // Exp e;
    public void visit(Print n) {
        if (!isArithmeticExpression(n.e, true)) {
            System.out.println("Error: (line " + n.e.line_number + ") print applied to non-int expression or print expression invalid.");
            errorCounter++;
        }

        n.e.accept(this);
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        n.i.accept(this);

        String lookup = lookupTypeForID(n.i.s);
        if (lookup == null) {
            System.out.println("Error: (line " + n.i.line_number + ") " + n.i.s + " is undeclared.");
            errorCounter++;
        } else {
            if (!areEqualTypes(n.e, lookup)) {
                errorCounter++;
                System.out.println("Error: (line " + n.i.line_number + ") types do not match.");
            }
        }

        n.e.accept(this);
    }

    private String lookupTypeForID(String id) {
        // is id a variable declared in this class and method?
        String type = symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).getVariableType(id);
        if (type == null) {
            // is id a variable declared as a parameter of this method, in this class
            type = symbolTable.getClassScope(currentClass).getMethodScope(currentMethod).getParameterType(id);
            if (type == null) {
                // is id declared as a field of this class?
                type = symbolTable.getClassScope(currentClass).getVariableType(id);
                if (type == null) {
                    // is id declared in any superclass?
                    return findFieldInSuperClass(id, currentClass);
                }
            }
        }
        return type;
    }

    // checks to see if a id is a field from a super class of any depth
    private String findFieldInSuperClass(String id, String className) {
        String superClass = typeTable.getType(className);
        if (superClass != null) {
            // className extends superClass
            // check to see if that superClass has a field 'id'
            if (symbolTable.getClassScope(superClass).variableMap.containsKey(id)) {
                // check to see if that superClass has a field 'id'
                // if it does, return that class
                return symbolTable.getClassScope(superClass).getVariableType(id);
            } else {
                // if not keep looking
                return findFieldInSuperClass(id, superClass);
            }
        }
        // superClass is null
        return symbolTable.getClassScope(className).variableMap.containsKey(id) ? symbolTable.getClassScope(className).getVariableType(id): null;
    }

    //                            RHS      LHS
    private boolean areEqualTypes(Exp exp, String type) {
        if (type.equals("boolean")) {
            return isBooleanExpression(exp);
        } else if (type.equals("integer")) {
            return isArithmeticExpression(exp, false);
        } else if (type.equals("intArray")) {
            if (exp instanceof Call) {
                return isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, type, false);
            } else if (exp instanceof IdentifierExp) {
                return isVariableDefined(currentClass, currentMethod, exp, type);
            } else if (exp instanceof NewArray) {
                return isArithmeticExpression(((NewArray) exp).e, false);
            }
            return false;
        } else if (typeTable.types.containsKey(type)) {
            if (exp instanceof Call) {
                return isCall(((Call) exp).e, ((Call) exp).i, ((Call) exp).el, type, false);
            } else if (exp instanceof IdentifierExp) {
                return isDerived(type, lookupTypeForID(((IdentifierExp) exp).s));
            } else if (exp instanceof NewObject) {
                return isDerived(type, ((NewObject) exp).i.s);
            } else if (exp instanceof This) {
                return isDerived(type, currentClass);
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


    // ...
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

    private boolean checkLoopInheritance(String className, Set<String> visitedClasses) {
        if (visitedClasses.contains(className)) {
            // already visited this class
            System.out.println("Error: Class inheritance loop detected with " + className);
            return false;
        } else {
            visitedClasses.add(className);
        }
        // check for extended class
        String extendedClass = typeTable.getType(className);
        if (extendedClass != null) {
            return checkLoopInheritance(extendedClass, visitedClasses);
        } else {
            // no extended class
            return true;
        }
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        n.i.accept(this);
        if (!(lookupTypeForID(n.i.s).equals("intArray")
                && isArithmeticExpression(n.e1, true) && isArithmeticExpression(n.e2, true))) {
            errorCounter++;
            System.out.println("Error: (line " + n.i.line_number + ") array assign failed");
        }
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(And n) {
        n.e1.accept(this);
        if (!(isBooleanExpression(n.e1) && isBooleanExpression(n.e2))) {
            errorCounter++;
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        n.e1.accept(this);
        if (!(isArithmeticExpression(n.e1, true) && isArithmeticExpression(n.e2, true))) {
            errorCounter++;
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        n.e1.accept(this);
        if (!(isArithmeticExpression(n.e1, true) && isArithmeticExpression(n.e2, true))) {
            errorCounter++;
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        if (!(isArithmeticExpression(n.e1, true) && isArithmeticExpression(n.e2, true))) {
            errorCounter++;
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        if (!(isArithmeticExpression(n.e1, true) && isArithmeticExpression(n.e2, true))) {
            errorCounter++;
            System.out.println("Error: (line " + n.e1.line_number + ") types do not match");
        }
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        if (!isArray(n)) {
            errorCounter++;
            System.out.println("Error: (line " + n.e1.line_number + ") array described is not valid");
        }
        n.e1.accept(this);
        if (!isArithmeticExpression(n.e2, true)) {
            errorCounter++;
            System.out.println("Error: (line " + n.e2.line_number + ") expression in bracket is not of integer type");
        }
        n.e2.accept(this);
    }

    // Exp e;
    public void visit(ArrayLength n) {
        if (!isArrayLength(n.e)) {
            errorCounter++;
            System.out.println("Error: (line " + n.e.line_number + ") array described is not valid");
        }
        n.e.accept(this);
    }
    private boolean isArrayLength(Exp exp) {
        if (exp instanceof Call) {
            Call c = ((Call) exp);
            return isCall(c.e, c.i, c.el, "intArray", true);
        } else if (exp instanceof IdentifierExp) {
            IdentifierExp ie = ((IdentifierExp) exp);
            return isVariableDefined(currentClass, currentMethod, ie, "intArray");
        }
        return false;
    }
    private boolean isArray(ArrayLookup arr) {
        if (arr.e1 instanceof Call) {
            Call c = ((Call) arr.e1);
            return isCall(c.e, c.i, c.el, "intArray", true)
                    && isArithmeticExpression(arr.e2, true);
        } else if (arr.e1 instanceof IdentifierExp) {
            IdentifierExp ie = ((IdentifierExp) arr.e1);
            return isVariableDefined(currentClass, currentMethod, ie, "intArray")
                    && isArithmeticExpression(arr.e2, true);
        }
        return false;
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        isCallHelper(n.e, n.i, n.el);
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
        if (!isArithmeticExpression(n.e, true)) {
            errorCounter++;
          System.out.println("Error: (line " + n.e.line_number + ") expression not of type integer.");
        }
        n.e.accept(this);
    }

    // Identifier i;
    public void visit(NewObject n) {
        if (symbolTable.getClassScope(n.i.s) == null) {
            errorCounter++;
            System.out.println("Error: (line " + n.i.line_number + ") class not found.");
        }
    }

    // Exp e;
    public void visit(Not n) {
        if (!(isBooleanExpression(n.e))) {
            errorCounter++;
            System.out.println("Error: (line " + n.e.line_number + ") not boolean expression");
        }
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
    }
}