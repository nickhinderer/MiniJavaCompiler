package com.company;

import syntaxtree.*;
import visitor.GJDepthFirst;


//class Context {
//    public String className;
//    public String methodName;
//    public boolean variable;
//    public boolean parameter;
//    public boolean field;
//}

public class TypeCheckVisitor extends GJDepthFirst<Type, SymbolTable> {

    private static SymbolTable symbolTable;

    public TypeCheckVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }



//    private static Type type(Context c, final String name) {
//        if (c.field)
//            return symbolTable.getFieldTypeInfo(c.className, name);
//        if (c.parameter)
//            return symbolTable.getParameterTypeInfo(c.className, c.methodName, name);
//        if (c.variable)
//            return symbolTable.getVariableTypeInfo(c.className, c.methodName, name);
//        return null;
//    }




    public Type visit(ClassDeclaration n, SymbolTable st) {
        Type _ret = new TypeChecksType();
        st.state.className = n.f1.f0.tokenImage;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        st.state.field = true;
        n.f2.accept(this, st);
        st.state.field = false;
        n.f3.accept(this, st);
        n.f4.accept(this, st);
        n.f5.accept(this, st);
        //if some typechecking rule for classes doesn't pass, return null
        return _ret;
    }





    public Type visit(PrintStatement n, SymbolTable c) {
        Type _ret=null;
        n.f0.accept(this, c);
        n.f1.accept(this, c);
        n.f2.accept(this, c);
        n.f3.accept(this, c);
        n.f4.accept(this, c);
        return _ret;
    }

    public Type visit(Expression n, SymbolTable c) {
        Type _ret = null;
        n.f0.accept(this, c);
        return _ret;
    }

    public Type visit(PlusExpression n, SymbolTable c) {
        Type _ret = null;
        n.f0.accept(this, c);
        n.f1.accept(this, c);
        n.f2.accept(this, c);
        return _ret;
    }

    public Type visit(TrueLiteral n, SymbolTable c) {
        return new PrimitiveType("boolean");
    }

    public Type visit(FalseLiteral n, SymbolTable c) {
        return new PrimitiveType("boolean");
    }

    public Type visit(IntegerLiteral n, SymbolTable c) {
        return new PrimitiveType("int");
    }

    public Type visit(PrimaryExpression n, SymbolTable c) {
        Type _ret=null;
        n.f0.accept(this, c);
        return _ret;
    }

    public Type visit(Identifier n, SymbolTable c) {
        return c.type(n.f0.tokenImage);
    }

    public Type visit(VarDeclaration n, SymbolTable c) {
        Type _ret = null;
//        if (c.context.field)
//            _ret = symbolTable.getFieldTypeInfo(c.className, n.f1.f0.tokenImage);
//        if (c.parameter)
//            _ret = symbolTable.getParameterTypeInfo(c.className, c.methodName, n.f1.f0.tokenImage);
//        if (c.variable)
//            _ret = symbolTable.getVariableTypeInfo(c.className, c.methodName, n.f1.f0.tokenImage);
        n.f0.accept(this, c);
        n.f1.accept(this, c);
        //n.f2.accept(this, c);
        return _ret;
    }

}
