package vapor;

import com.company.ClassType;
import com.company.MethodType;
import com.company.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VaporClassType {
    private Symbol name;
    private String vmt;
    private /*final*/ int size; //in bytes
    private List<VaporMethodType> methods;
    private Map<Symbol, Integer> fieldOffsets;
//    private

    public String vmt() {
        return vmt;
    }

    public int size() {
        return size;
    }

//    public

    public VaporClassType(ClassType classType) {
        //create the vmt and do all that.
        classType.getMethodCount(); //wait nvm this is useless. vmt is always 4 bytes
        methods = new ArrayList<>();
        fieldOffsets = new HashMap<>();
        int fieldsCount = classType.getFieldCount();
        this.size = 4 * fieldsCount + 4;
        Map<Symbol, MethodType> classMethods = classType.getMethods();
        int offset = 0;
        for (Symbol id : classType.getMethodsOrder()) {
            methods.add(new VaporMethodType(classType.classID(), id.toString(), classMethods.get(id), offset++));
        }
//        for (var entry : classMethods.entrySet()) {
//            methods.add(new VaporMethodType(classType.classID(), entry.getKey().toString(), entry.getValue(), offset++));
//        }
        classType.vapor = this;
        offset = 1;
        for (Symbol id : classType.getFieldsOrder()) {
            fieldOffsets.put(id, offset++);
        }
        vmt = createVmt(classType.classID());
    }

    private String createVmt(String className) {
        StringBuilder vmt = new StringBuilder("const vmt_" + className + '\n');
        for (VaporMethodType method : methods)
            vmt.append("\t:").append(method.getName()).append('\n');
        return vmt.toString();
    }

    public int methodOffset(String id) {
//        return methods.indexOf();
        return -1;
    }

    public int fieldOffset(String id) {
        return fieldOffsets.get(Symbol.symbol(id));
    }
}
