//import TypeCheck.SymbolTableVisitor;
import syntaxtree.Node;
import visitor.PrettyPrinter;

public class test {
    public static void main(String[] args) {

        Node root = null;
        try {
            root = new MiniJavaParser(System.in).Goal();

            PrettyPrinter<Void,String> pp = new PrettyPrinter<Void, String>();
            root.accept(pp, "");

            //SymbolTableVisitor sv = new SymbolTableVisitor();

            //root.accept(sv, 0);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
