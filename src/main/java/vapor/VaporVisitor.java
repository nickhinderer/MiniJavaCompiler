package vapor;

import com.company.Symbol;
import com.company.SymbolTable;
import com.company.Type;
import visitor.GJDepthFirst;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VaporVisitor extends GJDepthFirst<String, String> { //VaporVisitor<String>

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
            if (!entry.getValue().isMain())
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

    public int method() {
        Type type = symbolTable.getClassTypeInfo("name");
        return 1;
    }
}
