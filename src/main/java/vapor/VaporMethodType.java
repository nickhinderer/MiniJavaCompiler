package vapor;

import com.company.MethodType;
import com.company.Symbol;

public class VaporMethodType {
    private String name;
    private String signature;
    private String statements;
    private final int offset;
    private int tempCount;

    public VaporMethodType(String className, String methodName, MethodType method, int offset) {
        signature = createSignature(className, methodName, method);
        statements = "";
        name = className + '.' + methodName;
        this.offset = 4 * offset;
        this.tempCount = 0;
    }

    private String createSignature(String className, String methodName, MethodType method) {
        StringBuilder start = new StringBuilder("func " + className + "." + methodName + "(this ");
        for (Symbol parameter : method.getOrder()) start.append(' ').append(parameter.toString());
        start.append(')');
        return start.toString();
    }

    public String getSignature() {
        return signature;
    }

    public String getName() {
        return name;
    }

    public String getTemp() {
        return "s" + tempCount++;
    }
}
