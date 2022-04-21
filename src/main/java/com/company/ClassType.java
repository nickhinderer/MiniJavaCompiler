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
        this.type = TYPE.CLASS;
    }

    public ClassType(ClassType classType) {
        this.methods = new HashMap(classType.methods);
        this.fields = new HashMap(classType.fields);
        this.name = Symbol.symbol(classType.name.toString());
        if (classType.parent != null)
            this.parent = Symbol.symbol(classType.parent.toString());
        this.main = classType.main;
        this.type = TYPE.CLASS;
    }

    public ClassType(String className, String parentName, boolean main) {
        name = Symbol.symbol(className);
        if (parentName != null)
            parent = Symbol.symbol(parentName);
        methods = new HashMap<>();
        fields = new HashMap<>();
        this.main = main;
        this.type = TYPE.CLASS;
    }

    public Map<Symbol, MethodType> getMethods() {
        return methods;
    }

    public String className() {
        return name.toString();
    }

    public String parentName() {
        if (parent != null)
            return parent.toString();
        return null;
    }

    public void addParent(String parentName) {
        this.parent = Symbol.symbol(parentName);
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

    public Type type(String methodName, String name) {
        Symbol m = Symbol.symbol(methodName);
        if (methods.get(m) == null)
            return null;
        return methods.get(m).type(name);
    }

    public MethodType type(String methodName) {
        Symbol m = Symbol.symbol(methodName);
        return methods.get(m);
    }

    public MethodType typeMethod(String methodName) {
        Symbol m = Symbol.symbol(methodName);
        return methods.get(m);
    }

    public boolean inherit(ClassType parent) {
        for (var entry : parent.methods.entrySet()) {
            if (this.methods.containsKey(entry.getKey())) {
                if (!this.methods.get(entry.getKey()).equals(parent.methods.get(entry.getKey())))
                    return false;
                else
                    this.methods.put(entry.getKey(), entry.getValue());
            } else {
                this.methods.put(entry.getKey(), entry.getValue());
            }

        }
        for (var entry : parent.fields.entrySet()) {
            if (!this.fields.containsKey(entry.getKey())) {
                this.fields.put(entry.getKey(), entry.getValue());
            }
                //if (!this.fields.get(entry.getKey()).equals(parent.fields.get(entry.getKey())))
                    //return false;
                //else
                    //this.fields.put(entry.getKey(), entry.getValue());
                //this.fields.put(entry.getKey(), entry.getValue());

        }
        return true;
    }

    public boolean equals(ClassType other) {
            return this.name.toString().equals(other.name.toString());
    }
}
