package vapor;

import com.company.MethodType;
import com.company.Symbol;
import com.company.SymbolTable;
import com.company.TypeChecksType;
import syntaxtree.*;
import visitor.GJDepthFirst;
import visitor.GJVisitor;

import java.util.*;

public class VaporVisitor extends GJDepthFirst<String, SymbolTable> { //VaporVisitor<String>

    private final/*static*/ SymbolTable symbolTable;
//    private static java.lang.String file;
//    private static Map<Symbol, VaporClassType> classes;
//    private static Map<Symbol, VaporClassType> functions;


    public VaporVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
//        file = "";
//        classes = new HashMap(); functions = new HashMap();
        initialize();
    }

    private void initialize() {
        Map<Symbol, VaporClassType> classes = new HashMap<>();
//        String mainClassName = symbolTable.getMainClassName();
//        Set<String> classNames = symbolTable.getClassNames();
        for (var entry : symbolTable.classes().entrySet()) {
            if (entry.getValue().isMain())

//            else
                classes.put(entry.getKey(), new VaporClassType(entry.getValue()));
            //classes.put(Symbol.symbol(name), new VaporClassType(symbolTable.getClassTypeInfo(name)));
        } //get class info and strings
        //get methods info and their string and caclulate their names, just do all the structures that arent instructions
        //then add those to file, then start visit and do instructions (and now you have all the info you need contained in those vapormethodtypes (like if i am calling this method m in class c, then when i am at that point in my visitor, i can just go get my vapor classtypes and methodtypes and know that the offset for this method is x, these structures are the representations of your memory layout but representes objects with utility methods to not have to do those calculations mantually and associte related data and operations in one unit.
        Map<Symbol, VaporClassType> functions = new HashMap();


        //step 1: create visitor, give it MiniJava symbol table
        //step 2: take that symbol table, extract all classes and create
        //
    }

    public String visit(Goal n, SymbolTable st) {
        StringBuilder sb = new StringBuilder();
        String mainMethod = n.f0.accept(this, st);
        String classMethods = n.f1.accept(this, st);
        return sb.toString();
    }

    public String visit(MainClass n, SymbolTable st) {
//        n.f1.accept(this, st);
//        n.f11.accept(this, st);
//        n.f14.accept(this, st);
        st.state.classID = n.f1.f0.tokenImage;
//        n.f14.accept(this, st);
        st.state.methodID = "main";
        st.state.method = true;
        return "";
//        return n.f15.accept(this, st);
    }

//    public int method() {
//        Type type = symbolTable.getClassTypeInfo("name");
//        return 1;
//    }

    public String visit(NodeListOptional n, SymbolTable st) {
        StringBuffer methods = new StringBuffer();
        if (n.present()) {
            int _count = 0;
            for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                methods.append(e.nextElement().accept(this, st));
                _count++;
            }
            return methods.toString();
        } else
            return null;
    }

    public String visit(NodeList n, SymbolTable st) {
        StringBuilder methods = new StringBuilder();
        int _count = 0;
        for (Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            methods.append(e.nextElement().accept(this, st));
            _count++;
        }
        return methods.toString();
    }

    public String visit(TypeDeclaration n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    public String visit(ClassDeclaration n, SymbolTable st) {
        st.state.classID = n.f1.f0.tokenImage;
        st.state.method = true;
        return n.f4.accept(this, st);
    }

    public String visit(ClassExtendsDeclaration n, SymbolTable st) {
        st.state.classID = n.f1.f0.tokenImage;
        st.state.method = true;
        return n.f6.accept(this, st);
    }

    public String visit(MethodDeclaration n, SymbolTable st) {
        st.state.methodID = n.f2.f0.tokenImage;
        n.f8.accept(this, st);
        n.f10.accept(this, st);
        return null;
    }

    public String visit(PlusExpression n, SymbolTable st) {
        n.f0.accept(this, st);
        n.f2.accept(this, st);
        return null;
    }

    public String visit(Identifier n, SymbolTable st) {
//        st.p
        boolean b = st.isPV(st.state.classID, st.state.methodID, n.f0.tokenImage); //could probably get rid of first two params and just use this.state in the method (and other places too) but if you do that you loose functionality and you could nev ver find another class when it iis not in the currento ne
        if (b) {
            return n.f0.tokenImage;
        }
        return "[this" + st.typeC(st.state.classID).vapor.fieldOffset(n.f0.tokenImage) + "]";
//        switch (st.pvf(st.state.classID, st.state.methodID, n.f0.tokenImage)) {
//            case 0:
//            case 1:
//                return n.f0.tokenImage;
//            case 2:
//                //first variable is either "this" or the id of another object that is a field or variable/param in the method/class, second is the offset of the field/method ,
//                if (n.f0.tokenImage.equals("this"))
//                if (st.sameClass(st.typeC(st.state.classID), st.typeC(n.f0.tokenImage)))
//                return "[" +  + "+" + st.typeC(st.state.classID).vapor.fieldOffset(n.f0.tokenImage) * 4 + "]";
//        }
//        return n.f0.accept(this, st);
    }

    public String visit(AndExpression n, SymbolTable st) {
        String pe1 = n.f0.accept(this, st);
        String pe2 = n.f2.accept(this, st);

        //could be a true/false literal, or itendifierm since it type checked you know it is one of those two,
        //if it is true or false, handle it that way
        //otherwise, it is a parameter, varaiable, or field of type boolean
        return null;
    }

    public String visit(CompareExpression n, SymbolTable st) {


        String pe1 = n.f0.accept(this, st);
        String pe2 = n.f2.accept(this, st);

        String[] some = pe1.split(" ", 2);

        String temp1 = pe1.substring(0, pe1.indexOf(' '));
        String temp2 = pe2.substring(0, pe2.indexOf(' '));

//        return "LtS(" + temp1 + ", " + temp2 + ")";
        String ret = "";
        boolean empty1 = some[1].isBlank();
        boolean empty2 = some[1].isBlank();
        if (!empty1)
            ret += pe1 + '\n';
        if (!empty2)
            ret += pe2 + '\n';

        return ret + "lts(" + some[0] + ", " + some[0] + ")\n";
    }

    public String visit(PrimaryExpression n, SymbolTable st) {
        return n.f0.accept(this, st);
    }

    public String visit(Expression n, SymbolTable st) {
        String s = n.f0.accept(this, st);
        MethodType m = st.typeM(st.state.classID, st.state.methodID);
        String temp = m.vapor.getTemp();

//        StringBuffer expression = new StringBuffer( + " = " );
//        return expression.toString();
        return null;
        }

    public String visit(MessageSend n, SymbolTable st) {
        n.f0.accept(this, st);
        n.f2.accept(this, st);
        n.f4.accept(this, st);
        return null;
    }


    public String visit(IntegerLiteral n, SymbolTable st) {
        return n.f0.tokenImage;
    }

    public String visit(TrueLiteral n, SymbolTable st) {
        return n.f0.tokenImage;
    }

    public String visit(FalseLiteral n, SymbolTable st) {
        return n.f0.tokenImage;
    }

    public String visit(ThisExpression n, SymbolTable st) {
        return n.f0.tokenImage;
    }

    public String visit(WhileStatement n, SymbolTable st) {
        n.f2.accept(this, st);
        n.f4.accept(this, st);
        // remember, just go look at tree and deduce what you need to do
        // and keep in mind you are just translating things like while loops, practice
        // on c++ to c or c to a subset of c to simulate java to vapor
        // and remember that at this point it is type checked, so although
        // an expression could be a plus statement, you know it is not at this point
        // because it has type checked, so you can assume it is a boolean expression and
        // not anything that might be an expression
        // just remember it is just java to vapor, and figuring out the logistics with
        // the symbol table and what not (vapor classes etc.)

        // expression is either
        // and, compare, message send or primary expression (which itself could only be
        // an identifier to a boolean, or a boolean literal, remember... the program
        // has already type checked)
        return null;
    }

















//        public String visit(NodeList n, SymbolTable st) {
//            String _ret=null;
//            int _count=0;
//            for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
//                e.nextElement().accept(this,st);
//                _count++;
//            }
//            return _ret;
//        }

//        public String visit(NodeListOptional n, SymbolTable st) {
//            if ( n.present() ) {
//                String _ret=null;
//                int _count=0;
//                for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
//                    e.nextElement().accept(this,st);
//                    _count++;
//                }
//                return _ret;
//            }
//            else
//                return null;
//        }

        public String visit(NodeOptional n, SymbolTable st) {
            if ( n.present() )
                return n.node.accept(this,st);
            else
                return null;
        }

        public String visit(NodeSequence n, SymbolTable st) {
            String _ret=null;
            int _count=0;
            for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                e.nextElement().accept(this,st);
                _count++;
            }
            return _ret;
        }

        public String visit(NodeToken n, SymbolTable st) { return null; }

        //
        // User-generated visitor methods below
        //

        /**
         * f0 -> MainClass()
         * f1 -> ( TypeDeclaration() )*
         * f2 -> <EOF>
         */
//        public String visit(Goal n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            return _ret;
//        }

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
//        public String visit(MainClass n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            n.f3.accept(this, st);
//            n.f4.accept(this, st);
//            n.f5.accept(this, st);
//            n.f6.accept(this, st);
//            n.f7.accept(this, st);
//            n.f8.accept(this, st);
//            n.f9.accept(this, st);
//            n.f10.accept(this, st);
//            n.f11.accept(this, st);
//            n.f12.accept(this, st);
//            n.f13.accept(this, st);
//            n.f14.accept(this, st);
//            n.f15.accept(this, st);
//            n.f16.accept(this, st);
//            n.f17.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> ClassDeclaration()
         *       | ClassExtendsDeclaration()
         */
//        public String visit(TypeDeclaration n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> "class"
         * f1 -> Identifier()
         * f2 -> "{"
         * f3 -> ( VarDeclaration() )*
         * f4 -> ( MethodDeclaration() )*
         * f5 -> "}"
         */
//        public String visit(ClassDeclaration n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            n.f3.accept(this, st);
//            n.f4.accept(this, st);
//            n.f5.accept(this, st);
//            return _ret;
//        }

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
//        public String visit(ClassExtendsDeclaration n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            n.f3.accept(this, st);
//            n.f4.accept(this, st);
//            n.f5.accept(this, st);
//            n.f6.accept(this, st);
//            n.f7.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> Type()
         * f1 -> Identifier()
         * f2 -> ";"
         */
        public String visit(VarDeclaration n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            return _ret;
        }

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
//        public String visit(MethodDeclaration n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            n.f3.accept(this, st);
//            n.f4.accept(this, st);
//            n.f5.accept(this, st);
//            n.f6.accept(this, st);
//            n.f7.accept(this, st);
//            n.f8.accept(this, st);
//            n.f9.accept(this, st);
//            n.f10.accept(this, st);
//            n.f11.accept(this, st);
//            n.f12.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> FormalParameter()
         * f1 -> ( FormalParameterRest() )*
         */
        public String visit(FormalParameterList n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> Type()
         * f1 -> Identifier()
         */
        public String visit(FormalParameter n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> ","
         * f1 -> FormalParameter()
         */
        public String visit(FormalParameterRest n, SymbolTable st) {
            String _ret=null;
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
        public String visit(Type n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> "int"
         * f1 -> "["
         * f2 -> "]"
         */
        public String visit(ArrayType n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> "boolean"
         */
        public String visit(BooleanType n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> "int"
         */
        public String visit(IntegerType n, SymbolTable st) {
            String _ret=null;
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
        public String visit(Statement n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> "{"
         * f1 -> ( Statement() )*
         * f2 -> "}"
         */
        public String visit(Block n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> Identifier()
         * f1 -> "="
         * f2 -> Expression()
         * f3 -> ";"
         */
        public String visit(AssignmentStatement n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            n.f3.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> Identifier()
         * f1 -> "["
         * f2 -> Expression()
         * f3 -> "]"
         * f4 -> "="
         * f5 -> Expression()
         * f6 -> ";"
         */
        public String visit(ArrayAssignmentStatement n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            n.f3.accept(this, st);
            n.f4.accept(this, st);
            n.f5.accept(this, st);
            n.f6.accept(this, st);
            return _ret;
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
        public String visit(IfStatement n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            n.f3.accept(this, st);
            n.f4.accept(this, st);
            n.f5.accept(this, st);
            n.f6.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> "while"
         * f1 -> "("
         * f2 -> Expression()
         * f3 -> ")"
         * f4 -> Statement()
         */
//        public String visit(WhileStatement n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            n.f3.accept(this, st);
//            n.f4.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> "System.out.println"
         * f1 -> "("
         * f2 -> Expression()
         * f3 -> ")"
         * f4 -> ";"
         */
        public String visit(PrintStatement n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            n.f3.accept(this, st);
            n.f4.accept(this, st);
            return _ret;
        }

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
//        public String visit(Expression n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> PrimaryExpression()
         * f1 -> "&&"
         * f2 -> PrimaryExpression()
         */
//        public String visit(AndExpression n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> PrimaryExpression()
         * f1 -> "<"
         * f2 -> PrimaryExpression()
         */
//        public String visit(CompareExpression n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> PrimaryExpression()
         * f1 -> "+"
         * f2 -> PrimaryExpression()
         */
//        public String visit(PlusExpression n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> PrimaryExpression()
         * f1 -> "-"
         * f2 -> PrimaryExpression()
         */
        public String visit(MinusExpression n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> PrimaryExpression()
         * f1 -> "*"
         * f2 -> PrimaryExpression()
         */
        public String visit(TimesExpression n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> PrimaryExpression()
         * f1 -> "["
         * f2 -> PrimaryExpression()
         * f3 -> "]"
         */
        public String visit(ArrayLookup n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            n.f3.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> PrimaryExpression()
         * f1 -> "."
         * f2 -> "length"
         */
        public String visit(ArrayLength n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> PrimaryExpression()
         * f1 -> "."
         * f2 -> Identifier()
         * f3 -> "("
         * f4 -> ( ExpressionList() )?
         * f5 -> ")"
         */
//        public String visit(MessageSend n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            n.f1.accept(this, st);
//            n.f2.accept(this, st);
//            n.f3.accept(this, st);
//            n.f4.accept(this, st);
//            n.f5.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> Expression()
         * f1 -> ( ExpressionRest() )*
         */
        public String visit(ExpressionList n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> ","
         * f1 -> Expression()
         */
        public String visit(ExpressionRest n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            return _ret;
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
//        public String visit(PrimaryExpression n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> <INTEGER_LITERAL>
         */
//        public String visit(IntegerLiteral n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> "true"
         */
//        public String visit(TrueLiteral n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> "false"
         */
//        public String visit(FalseLiteral n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> <IDENTIFIER>
         */
//        public String visit(Identifier n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> "this"
         */
//        public String visit(ThisExpression n, SymbolTable st) {
//            String _ret=null;
//            n.f0.accept(this, st);
//            return _ret;
//        }

        /**
         * f0 -> "new"
         * f1 -> "int"
         * f2 -> "["
         * f3 -> Expression()
         * f4 -> "]"
         */
        public String visit(ArrayAllocationExpression n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            n.f3.accept(this, st);
            n.f4.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> "new"
         * f1 -> Identifier()
         * f2 -> "("
         * f3 -> ")"
         */
        public String visit(AllocationExpression n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            n.f3.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> "!"
         * f1 -> Expression()
         */
        public String visit(NotExpression n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            return _ret;
        }

        /**
         * f0 -> "("
         * f1 -> Expression()
         * f2 -> ")"
         */
        public String visit(BracketExpression n, SymbolTable st) {
            String _ret=null;
            n.f0.accept(this, st);
            n.f1.accept(this, st);
            n.f2.accept(this, st);
            return _ret;
        }

}