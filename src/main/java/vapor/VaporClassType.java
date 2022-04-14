package vapor;

import com.company.ClassType;
import com.company.Symbol;

import java.util.Map;

public class VaporClassType {
    private Symbol name;
    private String vmt;
    private int size; //in bytes
    private Map<Symbol, String> methods;

    public String vmt() {
        return vmt;
    }

    public int size() {
        return size;
    }

    public VaporClassType(ClassType classType) {
        //create the vmt and do all that.
        classType.getMethodCount(); //wait nvm this is useless. vmt is always 4 bytes
        int fieldsCount = classType.getFieldCount();
        this.size = 4 * fieldsCount + 4;

    }
}
