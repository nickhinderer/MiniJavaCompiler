package com.company;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Context {
    public String className;
    public String methodName;
    public boolean variable;
    public boolean parameter;
    public boolean field;
    public Context() {
        variable = false; parameter = false; field = false;
    }
    //public final static SymbolTable symbolTable;
}

public class SymbolTable {
    //beginScope, enterScope, etc. begin scope. this. whiteboard. etc.
    private static Global table;
    public Context state;

    public SymbolTable() {
        table = new Global();
        state = new Context();
    }

    public boolean addClass(String className, ClassType classType) {
        return table.addClass(className, classType);
    }

    public boolean addMethod(String className, String methodName, MethodType methodType) {
        return table.addMethod(className, methodName, methodType);
    }

    public boolean addField(String className, String fieldName, Type fieldType) {
        return table.addField(className, fieldName, fieldType);
    }

    public boolean addParameter(String className, String methodName, String parameterName, Type parameterType) {
        return table.addParameter(className, methodName, parameterName, parameterType);
    }

    public boolean addVariable(String className, String methodName, String variableName, Type variableType) {
        return table.addVariable(className, methodName, variableName, variableType);
    }

    public ClassType getClassTypeInfo(String className) {
        return table.getClassType(className);
        //look in table in Global and return type (this is different from classTypes that are only associated with references inside of methods or as a field, those simply have the name which references a class in table otherwise it is type error, remember, no nested classes or global methods etc. just make something that works for minijava while knowing the universal case/solution too
    }

//    public Type getMethodTypeInfo(String methodName) {
//        return table.getMethodTypeInfo(state.className, methodName);
//    }
//
//    public Type getFieldTypeInfo(String fieldName) {
//        return table.getFieldTypeInfo(state.className, fieldName);
//    }
//
//    public Type getParameterTypeInfo(String parameterName) {
//        return table.getParameterTypeInfo(state.className, state.methodName, parameterName);
//    }
//
//    public Type getVariableTypeInfo(String variableName) {
//        return table.getVariableTypeInfo(state.className, state.methodName, variableName);
//    }

    public ClassType getFullClassType() {
        return null;
        //search inheritance tree and add appropriate methods, have this in symbolTable, and vapor initialize, covert classes to vapor classes
    }
    public String getMainClassName() {
        for (var entry : table.getClassTypes().entrySet()) { //for (Map.Entry<Symbol, ClassType> class_t : table.getClassTypes().entrySet()) {
            if (entry.getValue().isMain())
                return entry.getKey().toString();
        }
        return null;
//             table.getClassTypes().forEach( () ->
//                     {}
//             );
//        Set<Symbol> classesSet = table.getClassNames();
//        for (Symbol name : classesSet)
//            if (table.getClassType(name.toString()).isMain())
//                return name;
//        return null;
    }

    public Type type(String name) {
        if (state.field)
            return table.getFieldTypeInfo(state.className, name);
        if (state.variable)
            return table.getVariableTypeInfo(state.className, state.methodName, name);
        if (state.parameter)
            return table.getParameterTypeInfo(state.className, state.methodName, name);
        return null;
    }
    public Set<String> getClassNames() {
        table.getClassNames();
        HashSet<String> classNames = new HashSet();
        for (Symbol name : table.getClassNames())  //for (Map.Entry<Symbol, ClassType> class_t : table.getClassTypes().entrySet()) {
            classNames.add(name.toString());
        return classNames;
    }

}
