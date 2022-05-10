package sandbox;

import parser.MiniJavaParser;
import symboltable.SymbolTableVisitor;
import syntaxtree.Node;
import type.Type;
import vapor.vapor.VaporVisitor;


public class SymTab {
    public static void main(String args[]){
        Node root = null;
        try {
            root = new MiniJavaParser(System.in).Goal();
            SymbolTableVisitor sv = new SymbolTableVisitor();
            root.accept(sv, null);
            VaporVisitor vv = new VaporVisitor(sv.getSymbolTable());
            root.accept(vv, null);
//            Type t = sv.getSymbolTable().getFullClassType("Fac");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }
}
