package vapor;

import com.company.MethodType;
import com.company.Symbol;

public class VaporMethodType {
    private String name;
    private String signature;
    private String statements;
    private final int offset;
    private volatile int tempCount;


    public VaporMethodType(String classID) {
        signature = "func Main()";
        statements = "";
        name = classID + ".Main";
        this.tempCount = 0;
        offset = 0;
    }

    public VaporMethodType(String className, String methodName, MethodType method, int offset) {
        signature = createSignature(className, methodName, method);
        statements = "";
        name = className + '.' + methodName;
        this.offset = 4 * offset;
        this.tempCount = 0;
    }

    public void setStatements(String statements) {
        this.statements = statements;
    }

    private String createSignature(String className, String methodName, MethodType method) {
        StringBuilder start = new StringBuilder("func " + className + "." + methodName + "(this");
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

    public synchronized String getTemp() {
        return "t." + tempCount++;
    }

    public int getOffset() {
        return offset;
    }

    public String statements() {
        return statements;
    }
}
