package com.company;

import syntaxtree.*;
import visitor.GJDepthFirst;

public class SymbolTableVisitor<R, A> extends GJDepthFirst<R, A> {


    private static SymbolTable symbolTable = new SymbolTable();
    private static String currentClass, currentMethod;
    private static boolean parameter = false, variable = false, field = false;

    public SymbolTable getSymbolTable() {
        return symbolTable;
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

    @Override
    public R visit(Goal n, A argu) {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        symbolTable.inheritAll();
        return null;
    }
    @Override
    public R visit(MainClass n, A argu) { //come back adn rewrite with new symboltable
        String className = n.f1.f0.tokenImage;
        ClassType classType = new ClassType(className, null, true);
        if (!symbolTable.addClass(className, classType))
            throw new TypeCheckException("Redefinition of of class '" + className + "'");
        currentClass = className;
        String methodName = "main";
        Type returnType = new PrimitiveType("void");
        MethodType methodType = new MethodType(returnType);
        if (!symbolTable.addMethod(currentClass, methodName, methodType))
            throw new TypeCheckException("Redefinition of of method '" + methodName + "' in class '" + currentClass + "'");
        currentMethod = methodName;
        variable = true;
        n.f14.accept(this, argu);
        variable = false;
        return null;
    }

    @Override
    public R visit(ClassDeclaration n, A argu) {
        String className = n.f1.f0.tokenImage;
        ClassType classType = new ClassType(className, null);
        if (!symbolTable.addClass(className, classType))
            throw new TypeCheckException("Redefinition of of class '" + className + "'");
        currentClass = className;
        field = true;
        n.f3.accept(this, argu);
        field = false;
        n.f4.accept(this, argu);
        return null;
    }

    @Override
    public R visit(ClassExtendsDeclaration n, A argu) {
        String className = n.f1.f0.tokenImage;
        String parentName = n.f3.f0.tokenImage;
        ClassType classType = new ClassType(className, parentName);
        if (!symbolTable.addClass(className, classType))
            throw new TypeCheckException("Redefinition of of class '" + className + "'");
        currentClass = className;
        field = true;
        n.f5.accept(this, argu);
        field = false;
        n.f6.accept(this, argu);
        return null;
    }

    @Override
    public R visit(MethodDeclaration n, A argu) {
        String methodName = n.f2.f0.tokenImage;
        Type returnType = getType(n.f1.f0);
        MethodType methodType = new MethodType(returnType);
        if (!symbolTable.addMethod(currentClass, methodName, methodType))
            throw new TypeCheckException("Redefinition of of method '" + methodName + "' in class '" + currentClass + "'");
        currentMethod = methodName;
        parameter = true;
        n.f4.accept(this, argu);
        parameter = false;
        variable = true;
        n.f7.accept(this, argu);
        variable = false;
        return null;
    }

    @Override
    public R visit(VarDeclaration n, A argu) {
        Type type = getType(n.f0.f0);
        String name = n.f1.f0.tokenImage;
        if (field)
            if (!symbolTable.addField(currentClass, name, type))
                throw new TypeCheckException("Redefinition of of field '" + name + "' in class '" + currentClass + "'");
        if (variable)
            if (!symbolTable.addVariable(currentClass, currentMethod, name, type))
                throw new TypeCheckException("Redefinition of variable '" + name + "' in method '" + currentMethod + "' in class '" + currentClass + "'");
        return null;
    }

    @Override
    public R visit(FormalParameter n, A argu) {
        Type type = getType(n.f0.f0);
        String name = n.f1.f0.tokenImage;
        if (parameter)
            if (symbolTable.addParameter(currentClass, currentMethod, name, type) == false)
                throw new TypeCheckException("Redefinition of variable '" + name + "' in method '" + currentMethod + "' in class '" + currentClass + "'");
        return null;
    }

}