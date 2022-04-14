package com.company;

import syntaxtree.*;
import visitor.GJDepthFirst;

public class SymbolTableVisitor/*<void, Context>*/ extends GJDepthFirst<Void , SymbolTable> {


    private static SymbolTable symbolTable = new SymbolTable();
    private static String currentClass, currentMethod;
    private static boolean parameter = false, variable = false, field = false;

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }



    @Override
    public Void visit(MainClass n, SymbolTable c) { //come back adn rewrite with new symboltable
        String className = n.f1.f0.tokenImage;
        ClassType classType = new ClassType(className, null, true);
        symbolTable.addClass(className, classType);
        currentClass = className;
        n.f0.accept(this, c);
        n.f1.accept(this, c);
        n.f2.accept(this, c);
        n.f3.accept(this, c);
        n.f4.accept(this, c);
        n.f5.accept(this, c);
        n.f6.accept(this, c);
        n.f7.accept(this, c);
        n.f8.accept(this, c);
        n.f9.accept(this, c);
        n.f10.accept(this, c);
        n.f11.accept(this, c);
        n.f12.accept(this, c);
        n.f13.accept(this, c);
        n.f14.accept(this, c);
        n.f15.accept(this, c);
        n.f16.accept(this, c);
        n.f17.accept(this, c);
        return null;
    }

    @Override
    public R visit(ClassDeclaration n, A argu) {
        String className = n.f1.f0.tokenImage;
        ClassType classType = new ClassType(className, null);
        symbolTable.addClass(className, classType);
        currentClass = className;
        R _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        field = true;
        n.f3.accept(this, argu);
        field = false;
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        return _ret;
    }

    @Override
    public R visit(ClassExtendsDeclaration n, A argu) {
        R _ret = null;
        String className = n.f1.f0.tokenImage;
        String parentName = n.f3.f0.tokenImage;
        ClassType classType = new ClassType(className, parentName);
        symbolTable.addClass(className, classType);
        currentClass = className;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        field = true;
        n.f5.accept(this, argu);
        field = false;
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        return _ret;
    }

    @Override
    public R visit(MethodDeclaration n, A argu) {
        R _ret = null;
        String methodName = n.f2.f0.tokenImage;
        Type returnType = getType(n.f1.f0);
        MethodType methodType = new MethodType(returnType);
        symbolTable.addMethod(currentClass, methodName, methodType);
        currentMethod = methodName;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        parameter = true;
        n.f4.accept(this, argu);
        parameter = false;
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        variable = true;
        n.f7.accept(this, argu);
        variable = false;
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        return _ret;
    }

    @Override
    public R visit(VarDeclaration n, A argu) {
        R _ret=null;
        Type type = getType(n.f0.f0);
        String name = n.f1.f0.tokenImage;
        if (field)
            if(!symbolTable.addField(currentClass, name, type))
                throw new TypeCheckException("Redefinition of of field '" + name + "' in class '" + currentClass + "'");
        if (variable)
            if (!symbolTable.addVariable(currentClass, currentMethod, name, type))
                throw new TypeCheckException("Redefinition of variable '" + name + "' in method '" + currentMethod + "' in class '" + currentClass + "'");
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    @Override
    public R visit(FormalParameter n, A argu) {
        R _ret=null;
        Type type = getType(n.f0.f0);
        String name = n.f1.f0.tokenImage;
        if (parameter)
            if (symbolTable.addParameter(currentClass, currentMethod, name, type) == false)
                throw new TypeCheckException("Redefinition of variable '" + name + "' in method '" + currentMethod + "' in class '" + currentClass + "'");
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    private static Type getType(NodeChoice n) {
        Type type = null;
        switch (n.which) {
            case 0:
                type = new ArrayType();
                break;
            case 1:
                type = new PrimitiveType("boolean");
                break;
            case 2:
                type = new PrimitiveType("int");
                break;
            case 3:
                String className = ((Identifier) n.choice).f0.tokenImage;
                type = new ClassType(className, null);
                break;
        }
        return type;
    }

}
//switch (n.f0.f0.which) {
//        case 0:
//        type = new ArrayType();
//        break;
//        case 1:
//        type = new PrimitiveType("boolean");
//        break;
//        case 2:
//        type = new PrimitiveType("int");
//        break;
//        case 3:
//        String className = ((Identifier)n.f0.f0.choice).f0.tokenImage;
//        type = new ClassType(className, null);
//        break;
//        }