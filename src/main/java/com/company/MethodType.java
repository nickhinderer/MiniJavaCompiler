package com.company;

import java.util.HashMap;
import java.util.Map;

public class MethodType extends Type {
    private Map<Symbol, Type> parameters;
    private Map<Symbol, Type> variables;
    private Type returnType;

    public MethodType(Type returnType) {
        this.returnType = returnType;
        parameters = new HashMap<>();
        variables = new HashMap<>();
    }

    public boolean addParameter(String parameterName, Type parameterType) {
        Symbol p = Symbol.symbol(parameterName);
        if (parameters.get(p) == null) {
            if (variables.get(p) != null)
                return false;
            parameters.put(p, parameterType);
            return true;
        } else return false;
        //don't forget to also check in variables too (even though the type checking document has an error and says duplicates are allowed)
    }

    public boolean addVariable(String variableName, Type variableType) {
        Symbol v = Symbol.symbol(variableName);
        if (variables.get(v) == null) {
            if (parameters.get(v) != null)
                return false;
            variables.put(v, variableType);
            return true;
        } else return false;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Type getParameterType(String parameterName) {
        Symbol p = Symbol.symbol(parameterName);
        return parameters.get(p);
    }

    public Type getVariableType(String variableName) {
        Symbol v = Symbol.symbol(variableName);
        return variables.get(v);
    }
}
