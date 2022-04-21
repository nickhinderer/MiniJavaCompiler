package vapor;

import com.company.ClassType;
import com.company.MethodType;
import com.company.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VaporClassType {
    private Symbol name;
    private String vmt;
    private /*final*/ int size; //in bytes
    private List<VaporMethodType> methods;
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
        int fieldsCount = classType.getFieldCount();
        this.size = 4 * fieldsCount + 4;
        Map<Symbol, MethodType> classMethods = classType.getMethods();
        int offset = 0;
        for (var entry : classMethods.entrySet()) {
            methods.add(new VaporMethodType(classType.className(), entry.getKey().toString(), entry.getValue(), offset++));
        }
        vmt = createVmt(classType.className());
    }

    private String createVmt(String className) {
        StringBuilder vmt = new StringBuilder("const vmt_" + className + '\n');
        for (VaporMethodType method : methods)
            vmt.append("\t:").append(method.getName()).append('\n');
        return vmt.toString();
    }

    public int methodOffset(String methodName) {
//        return methods.indexOf();
        return -1;
    }
}
