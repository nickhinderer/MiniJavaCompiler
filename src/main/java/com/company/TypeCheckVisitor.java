package com.company;

import syntaxtree.*;
import syntaxtree.ArrayType;
import visitor.GJDepthFirst;

import java.util.Enumeration;
public class TypeCheckVisitor extends GJDepthFirst<Type, SymbolTable> {

    public TypeCheckVisitor() {
    }
    public Type visit(MainClass n, SymbolTable st) {
        Type _ret = new TypeChecksType();
        st.state.classID = n.f1.f0.tokenImage;
//        n.f14.accept(this, st);
        st.state.methodID = "main";
        st.state.method = true;
        if (n.f15.accept(this, st) == null)
            return null;
        st.state.method = false;
        return _ret;
    }

    public Type visit(ClassDeclaration n, SymbolTable st) {
        st.state.classID = n.f1.f0.tokenImage;
        if (n.f4.accept(this, st) == null)
            return null;
        //if some typechecking rule for classes doesn't pass, return null
        return new TypeChecksType();
    }

    public Type visit(ClassExtendsDeclaration n, SymbolTable st) {
        st.state.classID = n.f1.f0.tokenImage;
        n.f1.accept(this, st);
        n.f3.accept(this, st);
        if (n.f6.accept(this, st) == null)
            return null;
        return new TypeChecksType();
    }

    public Type visit(MethodDeclaration n, SymbolTable st) {
        st.state.methodID = n.f2.f0.tokenImage;
        st.state.method = true;
//        Type declaredType = n.f1.accept(this, st);
//        n.f2.accept(this, st);
        if (n.f8.accept(this, st) == null)
            return null;
        Type t = n.f10.accept(this, st); //also check and make sure declared and actual return type match
        if (t == null)
            return null;
        if (!st.subType(st.typeM(st.state.classID, st.state.methodID).getReturnType(), t))
            return null;
        st.state.method=false;
        return new TypeChecksType();
    }





    public Type visit(PrintStatement n, SymbolTable st) {
        Type t = n.f2.accept(this, st);
        if (!isInt(t))
            return null;
        return new TypeChecksType();
    }

    public Type visit(Expression n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    public Type visit(PlusExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        if (t1 == null || t1.type != TYPE.PRIMITIVE || !((PrimitiveType) t1).subType.equals("int"))
            return null;
        Type t2 = n.f2.accept(this, st);
        if (t2 == null || t2.type != TYPE.PRIMITIVE || !((PrimitiveType) t2).subType.equals("int"))
            return null;
        return new PrimitiveType("int");
    }

    public Type visit(MinusExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        if (t1 == null || t1.type != TYPE.PRIMITIVE || !((PrimitiveType) t1).subType.equals("int"))
            return null;
        Type t2 = n.f2.accept(this, st);
        if (t2 == null || t2.type != TYPE.PRIMITIVE || !((PrimitiveType) t2).subType.equals("int"))
            return null;
        return new PrimitiveType("int");
    }

    public Type visit(TimesExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        if (t1 == null || t1.type != TYPE.PRIMITIVE || !((PrimitiveType) t1).subType.equals("int"))
            return null;
        Type t2 = n.f2.accept(this, st);
        if (t2 == null || t2.type != TYPE.PRIMITIVE || !((PrimitiveType) t2).subType.equals("int"))
            return null;
        return new PrimitiveType("int");
    }

    public Type visit(TrueLiteral n, SymbolTable st) {
        return new PrimitiveType("boolean");
    }

    public Type visit(FalseLiteral n, SymbolTable st) {
        return new PrimitiveType("boolean");
    }

    public Type visit(IntegerLiteral n, SymbolTable st) {
        return new PrimitiveType("int");
    }

    public Type visit(PrimaryExpression n, SymbolTable st) {
//        switch (n.f0.which) {
//            case 0:
//                return new PrimitiveType("int");
//            case 1:
//            case 2:
//                return new PrimitiveType("boolean");
//            case 3:
//
//        }
        return n.f0.accept(this, st);
    }

    public Type visit(Identifier n, SymbolTable st) {
        return st.typePVF(st.state.classID, st.state.methodID, n.f0.tokenImage);
    }

    public Type visit(VarDeclaration n, SymbolTable st) {
        Type _ret = null;
//        if (c.context.field)
//            _ret = symbolTable.getFieldTypeInfo(c.className, n.f1.f0.tokenImage);
//        if (c.parameter)
//            _ret = symbolTable.getParameterTypeInfo(c.className, c.methodName, n.f1.f0.tokenImage);
//        if (c.variable)
//            _ret = symbolTable.getVariableTypeInfo(c.className, c.methodName, n.f1.f0.tokenImage);
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        //n.f2.accept(this, st);
        return _ret;
    }



    public Type visit(AssignmentStatement n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        if (t1 == null || t2 == null)
            return null;
        if (t2.type != t1.type)
            return null;
        if (t1.type == TYPE.PRIMITIVE)
            if (!((PrimitiveType) t1).subType.equals(((PrimitiveType) t2).subType))
                return null;
        //handle classtype (call subtype) and arraytype too
        return new TypeChecksType();
    }

    public Type visit(Statement n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    public Type visit(IfStatement n, SymbolTable st) {
        Type t1 = n.f2.accept(this, st);
        if (!isBoolean(t1))
            return null;
        if (n.f4.accept(this, st) == null)
            return null;
        if (n.f6.accept(this, st) == null)
            return null;
        return new TypeChecksType();
    }

    public Type visit(CompareExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        if (isInt(t1) && isInt(t2))
            return new PrimitiveType("boolean");
        return null;
    }

    public Type visit(MessageSend n, SymbolTable st) {
        String classID = ((ClassType) n.f0.accept(this, st)).classID();
        ClassType c = st.typeC(classID);
        MethodType m = st.typeM(classID, n.f2.f0.tokenImage);
        boolean checks = true;
        if ( n.f4.present() )
            checks = validateParameters((ExpressionList) n.f4.node, m, st);
        if (checks)
            return m.getReturnType();
        else
            return null;
    }

    private boolean validateParameters(ExpressionList n, MethodType m, SymbolTable st) {
        Type t = n.f0.accept(this, st);
        int params = 0;
        if (!matchesExpectedParameter(t, m, st, 0))
            return false;
        else params++;

        if ( n.f1.present() ) {
            int count = 0;
            for ( Enumeration<Node> e = n.f1.elements(); e.hasMoreElements(); ) {
                t = e.nextElement().accept(this, st);
                if(!matchesExpectedParameter(t, m, st, count + 1))
                    return false;
                count++; params++;
            }
        }
//        System.out.println(params);
//        System.out.println(m.parameterCount());
        if (params != m.parameterCount())
            return false;
        return true;
    }

    private boolean matchesExpectedParameter(Type type, MethodType m, SymbolTable st, int index) {
        if (type == null)
            return false;
        String parameter = m.parameter(index);
        if (parameter == null)
            return false;
        if (!st.subType(type, m.getParameterType(parameter)))
            return false;
        return true;
    }












    /*public Type visit(MessageSend n, SymbolTable st) {
        ClassType c = (ClassType) n.f0.accept(this, st);
        MethodType m = c.typeMethod(n.f2.f0.tokenImage);
        //n.f2.accept(this, st); //has a method named ID?


        //n.f4.accept(this, st);
        if ( n.f4.present() ) {
//            return n.f4.node.accept(this,st);
//            return ((ExpressionList)n.f4.node).accept(this,st);
//            Expression e = ((ExpressionList)n.f4.node).f0;
            validateParameters()
        }
        return m.getReturnType();
    }

    private boolean validateParameters(ExpressionList n, MethodType m, SymbolTable st) {
        Type t1 = ((ExpressionList) n.f4.node).f0.accept(this, st);
        if (t1 == null)
            return null;
        String parameter = m.parameter(0);
        if (parameter == null)
            return null;
        if (!st.subType(t1, m.getParameterType(m.parameter(0))))
            return null;
//            NodeListOptional a = ((ExpressionList) n.f4.node).f1;
        if ( ((ExpressionList) n.f4.node).f1.present() ) {
            int _count=0;

            for ( Enumeration<Node> e = ((ExpressionList) n.f4.node).f1.elements(); e.hasMoreElements(); ) {
                t2 = e.nextElement().accept(this, st);
                if (t2 == null)
                    return null;
                parameter = m.parameter(_count+1);
                if (parameter == null)
                    return null;
                Type expected = m.getParameterType(parameter);
                if (!st.subType(t2, expected))
                    return null;
                _count++;
            }
        }
    }*/

    private boolean isBoolean(Type t) {
        if (t == null)
            return false;
        if (t.type != TYPE.PRIMITIVE)
            return false;
        if (!((PrimitiveType) t).subType.equals("boolean"))
            return false;
        return true;
    }

    private boolean isInt(Type t) {
        if (t == null)
            return false;
        if (t.type != TYPE.PRIMITIVE)
            return false;
        if (!((PrimitiveType) t).subType.equals("int"))
            return false;
        return true;
    }

    private boolean isArray(Type t) {
        if (t == null)
            return false;
        if (t.type != TYPE.ARRAY)
            return false;
        return true;
    }


















    ////////////////////////
//    public Type visit(NodeList n, SymbolTable st) {
//        int _count=0;
//        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
//            if (e.nextElement().accept(this, st) == null)
//                return null;
//            _count++;
//        }
//        return new TypeChecksType();
//    }
//
    public Type visit(NodeListOptional n, SymbolTable st) {
        Type checks = new TypeChecksType();
        if ( n.present() ) {
            int _count=0;
            for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                if (e.nextElement().accept(this, st) == null)
                    checks = null;
                _count++;
            }
            return checks;
        }
        else
            return checks;
    }
//
//    public Type visit(NodeSequence n, SymbolTable st) {
//        int _count=0;
//        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
//            if (e.nextElement().accept(this, st) == null )
//                return null;
//            _count++;
//        }
//        return new TypeChecksType();
//    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
    * */
    public Type visit(NodeList n, SymbolTable st) {
        Type _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,st);
            _count++;
        }
        return _ret;
    }

//    public Type visit(NodeListOptional n, SymbolTable st) {
//        if ( n.present() ) {
//            Type _ret=null;
//            int _count=0;
//            for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
//                e.nextElement().accept(this,st);
//                _count++;
//            }
//            return _ret;
//        }
//        else
//            return null;
//    }

    public Type visit(NodeOptional n, SymbolTable st) {
        if ( n.present() )
            return n.node.accept(this,st);
        else
            return null;
    }

    public Type visit(NodeSequence n, SymbolTable st) {
        Type _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,st);
            _count++;
        }
        return _ret;
    }

    public Type visit(NodeToken n, SymbolTable st) { return null; }

    //
    // User-generated visitor methods below
    //

    /**
     * f0 -> MainClass()
     * f1 -> ( TypeDeclaration() )*
     * f2 -> <EOF>
     */
    public Type visit(Goal n, SymbolTable st) {
        Type _ret=null;
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f1.accept(this, st);
        if (t1 == null || t2 == null)
            return null;
        return new TypeChecksType();
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
//    public Type visit(MainClass n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        n.f3.accept(this, st);
//        n.f4.accept(this, st);
//        n.f5.accept(this, st);
//        n.f6.accept(this, st);
//        n.f7.accept(this, st);
//        n.f8.accept(this, st);
//        n.f9.accept(this, st);
//        n.f10.accept(this, st);
//        n.f11.accept(this, st);
//        n.f12.accept(this, st);
//        n.f13.accept(this, st);
//        n.f14.accept(this, st);
//        n.f15.accept(this, st);
//        n.f16.accept(this, st);
//        n.f17.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> ClassDeclaration()
     *       | ClassExtendsDeclaration()
     */
    public Type visit(TypeDeclaration n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
//    public Type visit(ClassDeclaration n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        n.f3.accept(this, st);
//        n.f4.accept(this, st);
//        n.f5.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
//    public Type visit(ClassExtendsDeclaration n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        n.f3.accept(this, st);
//        n.f4.accept(this, st);
//        n.f5.accept(this, st);
//        n.f6.accept(this, st);
//        n.f7.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     * f2 -> ";"
     */
//    public Type visit(VarDeclaration n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
//    public Type visit(MethodDeclaration n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        n.f3.accept(this, st);
//        n.f4.accept(this, st);
//        n.f5.accept(this, st);
//        n.f6.accept(this, st);
//        n.f7.accept(this, st);
//        n.f8.accept(this, st);
//        n.f9.accept(this, st);
//        n.f10.accept(this, st);
//        n.f11.accept(this, st);
//        n.f12.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> FormalParameter()
     * f1 -> ( FormalParameterRest() )*
     */
    public Type visit(FormalParameterList n, SymbolTable st) {
        Type _ret=null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    public Type visit(FormalParameter n, SymbolTable st) {
        Type _ret=null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    public Type visit(FormalParameterRest n, SymbolTable st) {
        Type _ret=null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> ArrayType()
     *       | BooleanType()
     *       | IntegerType()
     *       | Identifier()
     */
    public Type visit(syntaxtree.Type n, SymbolTable st) {
        Type _ret=null;
        n.f0.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> "int"
     * f1 -> "["
     * f2 -> "]"
     */
    public Type visit(ArrayType n, SymbolTable st) {
        return new com.company.ArrayType();
    }

    /**
     * f0 -> "boolean"
     */
    public Type visit(BooleanType n, SymbolTable st) {
        return null; //this should never be visitied, remember, symtab does declarations, typecheck does stratements, and don't visit nodes you don't need to visit, like this one which is always "boolean" so youd just do that in the method that would call this and use that knowlecdge there. aLSO REMEMBER you can do stuff in loops before visiting which is how hw4 is done
//        return new PrimitiveType("boolean");//n.f0.accept(this, st);
    }

    /**
     * f0 -> "int"
     */
    public Type visit(IntegerType n, SymbolTable st) {
        Type _ret=null;
        n.f0.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> Block()
     *       | AssignmentStatement()
     *       | ArrayAssignmentStatement()
     *       | IfStatement()
     *       | WhileStatement()
     *       | PrintStatement()
     */
//    public Type visit(Statement n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> "{"
     * f1 -> ( Statement() )*
     * f2 -> "}"
     */
    public Type visit(Block n, SymbolTable st) {
        return n.f1.accept(this, st);
    }

    /**
     * f0 -> Identifier()
     * f1 -> "="
     * f2 -> Expression()
     * f3 -> ";"
     */
//    public Type visit(AssignmentStatement n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        n.f3.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> Identifier()
     * f1 -> "["
     * f2 -> Expression()
     * f3 -> "]"
     * f4 -> "="
     * f5 -> Expression()
     * f6 -> ";"
     */
    public Type visit(ArrayAssignmentStatement n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        Type t3 = n.f5.accept(this, st);
        if (!isArray(t1) || !isInt(t2) || !isInt(t3))
            return null; //throw new TypeCheckException("invalid array assignment");
        return new TypeChecksType();
    }

    /**
     * f0 -> "if"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     * f5 -> "else"
     * f6 -> Statement()
     */
//    public Type visit(IfStatement n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        n.f3.accept(this, st);
//        n.f4.accept(this, st);
//        n.f5.accept(this, st);
//        n.f6.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> "while"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> Statement()
     */
    public Type visit(WhileStatement n, SymbolTable st) {
        Type t1 = n.f2.accept(this, st);
        if (!t1.equals(new PrimitiveType("boolean"))) {
//            return null;
            throw new TypeCheckException("while expression is not boolean");
        }
//            throw new TypeCheckException("26 violated");
        Type t2 = n.f4.accept(this, st);
        if (t2 == null)
            return null;
        return new TypeChecksType();
    }

    /**
     * f0 -> "System.out.println"
     * f1 -> "("
     * f2 -> Expression()
     * f3 -> ")"
     * f4 -> ";"
     */
//    public Type visit(PrintStatement n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        n.f3.accept(this, st);
//        n.f4.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> AndExpression()
     *       | CompareExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | PrimaryExpression()
     */
//    public Type visit(Expression n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "&&"
     * f2 -> PrimaryExpression()
     */
    public Type visit(AndExpression n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        if (isBoolean(t1) && isBoolean(t2))
            return t1;
        return null;
//        throw new TypeCheckException("mismatched and expression");
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "<"
     * f2 -> PrimaryExpression()
     */
//    public Type visit(CompareExpression n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "+"
     * f2 -> PrimaryExpression()
     */
//    public Type visit(PlusExpression n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "-"
     * f2 -> PrimaryExpression()
     */
//    public Type visit(MinusExpression n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "*"
     * f2 -> PrimaryExpression()
     */
//    public Type visit(TimesExpression n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "["
     * f2 -> PrimaryExpression()
     * f3 -> "]"
     */
    public Type visit(ArrayLookup n, SymbolTable st) {
        Type t1 = n.f0.accept(this, st);
        Type t2 = n.f2.accept(this, st);
        if (!isArray(t1) || !isInt(t2))
            return null;
        return t2;
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> "length"
     */
    public Type visit(ArrayLength n, SymbolTable st) {
        Type t = n.f0.accept(this, st);
        if (!isArray(t))
            return null;
        return new PrimitiveType("int");
    }

    /**
     * f0 -> PrimaryExpression()
     * f1 -> "."
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( ExpressionList() )?
     * f5 -> ")"
     */
//    public Type visit(MessageSend n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        n.f1.accept(this, st);
//        n.f2.accept(this, st);
//        n.f3.accept(this, st);
//        n.f4.accept(this, st);
//        n.f5.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> Expression()
     * f1 -> ( ExpressionRest() )*
     */
    public Type visit(ExpressionList n, SymbolTable st) {
        Type _ret=null;
        n.f0.accept(this, st);
        n.f1.accept(this, st);
        return _ret;
    }

    /**
     * f0 -> ","
     * f1 -> Expression()
     */
    public Type visit(ExpressionRest n, SymbolTable st) {
        return n.f1.accept(this, st);
    }

    /**
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     *       | BracketExpression()
     */
//    public Type visit(PrimaryExpression n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
//    public Type visit(IntegerLiteral n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> "true"
     */
//    public Type visit(TrueLiteral n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> "false"
     */
//    public Type visit(FalseLiteral n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> <IDENTIFIER>
     */
//    public Type visit(Identifier n, SymbolTable st) {
//        Type _ret=null;
//        n.f0.accept(this, st);
//        return _ret;
//    }

    /**
     * f0 -> "this"
     */
    public Type visit(ThisExpression n, SymbolTable st) {
        return st.typeC(st.state.classID);
    }

    /**
     * f0 -> "new"
     * f1 -> "int"
     * f2 -> "["
     * f3 -> Expression()
     * f4 -> "]"
     */
    public Type visit(ArrayAllocationExpression n, SymbolTable st) {
        Type t = n.f3.accept(this, st);
        if (!isInt(t))
            return null;
        return new com.company.ArrayType();
    }

    /**
     * f0 -> "new"
     * f1 -> Identifier()
     * f2 -> "("
     * f3 -> ")"
     */
    public Type visit(AllocationExpression n, SymbolTable st) {
        //n.f1.accept(this, st);
        return st.typeC(n.f1.f0.tokenImage);
    }

    /**
     * f0 -> "!"
     * f1 -> Expression()
     */
    public Type visit(NotExpression n, SymbolTable st) {
        Type t = n.f1.accept(this, st);
        if (!isBoolean(t))
            return null;
        return t;
    }

    /**
     * f0 -> "("
     * f1 -> Expression()
     * f2 -> ")"
     */
    public Type visit(BracketExpression n, SymbolTable st) {
        return n.f1.accept(this, st);
    }

}
