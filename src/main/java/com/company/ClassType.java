package com.company;

import java.util.HashMap;
import java.util.Map;

public class ClassType extends Type {
    private Map<Symbol, MethodType> methods;
    private Map<Symbol, Type> fields;
    private Symbol name;
    private Symbol parent;
    private boolean main;

    public ClassType(String className, String parentName) {
        name = Symbol.symbol(className);
        if (parentName != null)
            parent = Symbol.symbol(parentName);
        methods = new HashMap<>();
        fields = new HashMap<>();
        this.main = false;
    }

    public ClassType(String className, String parentName, boolean main) {
        name = Symbol.symbol(className);
        if (parentName != null)
            parent = Symbol.symbol(parentName);
        methods = new HashMap<>();
        fields = new HashMap<>();
        this.main = main;
    }

    public String className() {
        return name.toString();
    }

    public String parentName() {
        if (parent != null)
            return parent.toString();
        return null;
    }

    public boolean addMethod(String methodName, MethodType methodType) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null) {
            methods.put(m, methodType);
            return true;
        } else return false;
    }

    public boolean addField(String fieldName, Type fieldType) {
        Symbol f = Symbol.symbol(fieldName);
        if (fields.get(f) == null) {
            fields.put(f, fieldType);
            return true;
        } else return false;
    }

    public boolean addParameter(String methodName, String parameterName, Type parameterType) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return false;
        return methods.get(m).addParameter(parameterName, parameterType);
    }

    public boolean addVariable(String methodName, String variableName, Type variableType) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return false;
        return methods.get(m).addVariable(variableName, variableType);
    }

    public MethodType getMethodType(String methodName) {
        Symbol m = Symbol.symbol(methodName);
        return methods.get(m);
    }

    public Type getFieldType(String fieldName) {
        Symbol f = Symbol.symbol(fieldName);
        return fields.get(f);
    }

    public Type getParameterTypeInfo(String methodName, String parameterName) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return null;
        return methods.get(m).getParameterType(parameterName);
    }

    public Type getVaraibleTypeInfo(String methodName, String variableName) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return null;
        return methods.get(m).getVariableType(variableName);
    }

    public boolean isMain() {
        return main;
    }

    public int getMethodCount() {
        return methods.size();
    }

    public int getFieldCount() {
        return fields.size();
    }
}
