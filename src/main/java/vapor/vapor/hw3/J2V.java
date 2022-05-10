package vapor.vapor.hw3;//import TypeCheck.*;
import symboltable.SymbolTableVisitor;
import parser.MiniJavaParser;
import syntaxtree.*;
import vapor.vapor.VaporVisitor;

public class J2V {

    public static void main(String args[]){
        Node root = null;
        try {
            root = new MiniJavaParser(System.in).Goal();
            SymbolTableVisitor sv = new SymbolTableVisitor();
            root.accept(sv, null);
            VaporVisitor v = new VaporVisitor(sv.getSymbolTable());
            root.accept(v, null);
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.err.println(e.getMessage());
        }
    }
}

