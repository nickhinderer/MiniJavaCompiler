package com.company;

import syntaxtree.Expression;
import syntaxtree.PrintStatement;
import syntaxtree.VarDeclaration;
import visitor.GJDepthFirst;


class Context {
    public String className;
    public String methodName;
    public boolean variable;
    public boolean parameter;
    public boolean field;
}

public class TypeCheckVisitor extends GJDepthFirst<Type, Context> {

    private static SymbolTable symbolTable;

    public TypeCheckVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public Type visit(PrintStatement n, Context c) {
        Type _ret=null;
        n.f0.accept(this, c);
        n.f1.accept(this, c);
        n.f2.accept(this, c);
        n.f3.accept(this, c);
        n.f4.accept(this, c);
        return _ret;
    }

    public Type visit(Expression n, Context c) {
        Type _ret=null;
        n.f0.accept(this, c);
        return _ret;
    }

    public Type visit(VarDeclaration n, Context c) {
        if (c.field);
        Type _ret=null; //symbolTable.
        n.f0.accept(this, c);
        n.f1.accept(this, c);
        n.f2.accept(this, c);
        return _ret;
    }

}
