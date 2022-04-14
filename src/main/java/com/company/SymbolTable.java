package com.company;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Context {
    public String className;
    public String methodName;
//    public boolean variable;
//    public boolean parameter;
//    public boolean field;

//    public boolean _class;
    public boolean method;
    public Context() {
        /*variable = false; parameter = false; field = false; _class = false;*/ method = false;
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

    public ClassType getFullClassType(String className) {
        return table.type(className);
        //just remember the flow of setting up dummy skeleton methods and fililng in leater like he said, establish that workflow. and also remember that  one helper method from class
        //memory layout too
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

//    public Type type(String name) {
//        if (state.method)
//            return table.getFieldTypeInfo(state.className, name);
//        if (state._class)
//            return table.getVariableTypeInfo(state.className, state.methodName, name);
//        if (state.parameter)
//            return table.getParameterTypeInfo(state.className, state.methodName, name);
//        return table.getClassType(name);
//        //return null;
//    }



    public boolean subType(Type t1, Type t2) {
        if (t1.type == TYPE.PRIMITIVE && t2.type == TYPE.PRIMITIVE)
            if (!((PrimitiveType) t1).subType.equals(((PrimitiveType) t2).subType))
                return false;
        return true;
    }
//    public Type type(String name) {
//        if (state.field)
//            return table.getFieldTypeInfo(state.className, name);
//        if (state.variable)
//            return table.getVariableTypeInfo(state.className, state.methodName, name);
//        if (state.parameter)
//            return table.getParameterTypeInfo(state.className, state.methodName, name);
//        return table.getClassType(name);
//        //return null;
//    }
    public Set<String> getClassNames() {
        table.getClassNames();
        HashSet<String> classNames = new HashSet();
        for (Symbol name : table.getClassNames())  //for (Map.Entry<Symbol, ClassType> class_t : table.getClassTypes().entrySet()) {
            classNames.add(name.toString());
        return classNames;
    }













//    public Type type(String name) {
//        Type type;
//        if (state.method) {
//            type = table.getVariableTypeInfo(state.className, state.methodName, name);
//            if (type != null)
//                return type;
//            type = table.getParameterTypeInfo(state.className, state.methodName, name);
//            if (type != null)
//                return type;
//        }
//        type = table.getFieldTypeInfo(state.className, name);
//        if (type != null)
//            return type;
//        return table.getClassType(name);
////        if (type != null)
////            return type;
//
////        return null;
//    }

    public Type typeParamaterORVariable(String name) {
        Type type;
        if (state.method) {
            type = table.type(state.className, state.methodName, name); //search parameters and variables in current method
            if (type != null)
                return type;
        }
        return null;
//        type = table.type(state.className, name); //no matching var/param; search methods in current class
//        if (type != null)
//            return type;
//        type = table.type(name); //no matching method; search classes in global, returns null if no matching classes are found
//        return type;
    }

    public MethodType typeMethod(String methodName) {
        return table.type(state.className, methodName); //no matching var/param; search methods in current class
//        type = table.type(name); //no matching method; search classes in global, returns null if no matching classes are found
//        return type;
    }

    public Type typeClass(String className) {
        return getFullClassType(className); //no matching var/param; search methods in current class
//        type = table.type(name); //no matching method; search classes in global, returns null if no matching classes are found
//        return type;
    }
}
