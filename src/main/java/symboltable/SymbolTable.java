package symboltable;

import type.Type;
import type.ClassType;
import type.MethodType;
import type.PrimitiveType;
import typecheck.TypeCheckException;

import type.enums.TYPE;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SymbolTable {
    //beginScope, enterScope, etc. begin scope. this. whiteboard. etc.
    public static Environment table;
    public Context state;

    public SymbolTable() {
        table = new Environment();
        state = new Context();
        ifCounter = 0;
        whileCounter = 0;
        nullCounter = 0;
    }

    public volatile int ifCounter;
    public volatile int whileCounter;
    public int nullCounter;
    public int andCounter;

    public synchronized int getWhileCounter() {
        return whileCounter++;
    }

    public int getAndCounter() {
        return andCounter++;
    }

    public int getNullCounter() {
        return nullCounter++;
    }

    public synchronized int getIfCounter() {
        int count = ifCounter;
        ifCounter++;
        return count;
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

//    public ClassType getClassTypeInfo(String className) {
//        return table.getClassType(className);
//        //look in table in Global and return type (this is different from classTypes that are only associated with references inside of methods or as a field, those simply have the name which references a class in table otherwise it is type error, remember, no nested classes or global methods etc. just make something that works for minijava while knowing the universal case/solution too
//    }

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
//
//    public ClassType getFullClassType(String className) {
////        return table.type(className);
//
//
//        ClassType t = table.type(className);
//        ClassType full = new ClassType(t);
//        while (t.parentName() != null) {
//            //if it has a method not contained in running class
//            ClassType parent = table.type(t.parentName());
//            boolean checks = full.inherit(parent);
//            if (!checks)
//                return null;
//            t = parent;
//        }


    //        //table.type(t.parentName());
//        //just remember the flow of setting up dummy skeleton methods and fililng in leater like he said, establish that workflow. and also remember that  one helper method from class
//        //memory layout too
//        //search inheritance tree and add appropriate methods, have this in symbolTable, and vapor initialize, covert classes to vapor classes
//        return full;
//    }
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
        //add same type method
        if (t1.type != t2.type) {
//            throw new TypeCheckException("foo");
            return false;

        }
        if (t1.type == TYPE.PRIMITIVE)
            if (!((PrimitiveType) t1).subType.equals(((PrimitiveType) t2).subType))
                return false;
        if (t1.type == TYPE.CLASS)
            if (((ClassType) t1).classID().equals("A"))
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


    public boolean isPV(String classID, String methodID, String id) {
        ClassType classType = this.typeC(classID);
        MethodType methodType = classType.getMethodType(methodID);
        boolean b = methodType.isPV(id);
        return b;
//        return this.getFullClassType(classID).getMethodType(methodID).isPV(id);
    }


    private ClassType getFullClassType(String classID) {
        ClassType t = table.type(classID);
        ClassType full = new ClassType(t);
        while (t.parentName() != null) {
            ClassType parent = table.type(t.parentName());
            boolean checks = full.inherit(parent);
            if (!checks)
                return null;
            t = parent;
        }
        return full;
    }

    public ClassType typeC(String id) {
//        return getFullClassType(id);
        return table.getClassType(id);
    }

    public MethodType typeM(String classID, String id) {
        return table.getMethodTypeInfo(classID, id);
    }

    public Type typeF(String classID, String id) {
        return table.getFieldTypeInfo(classID, id);
    }

    public Type typePVF(String classID, String methodID, String id) {
        Type type;
        type = table.getParameterTypeInfo(classID, methodID, id);
        if (type != null)
            return type;
        type = table.getVariableTypeInfo(classID, methodID, id);
        //type = table.getVariableTypeInfo(state.classID, state.methodID, id);
        if (type != null)
            return type;
        return table.getFieldTypeInfo(classID, id);
        //return table.getFieldTypeInfo(state.classID, id);
    }

    public Type typePV(String classID, String methodID, String id) {
        Type type;
        type = table.getParameterTypeInfo(classID, methodID, id);
        if (type != null)
            return type;
        type = table.getVariableTypeInfo(classID, methodID, id);
        return type;
    }

    public int pvf(String classID, String methodID, String id) {
        int type = -1;
        if (table.getParameterTypeInfo(state.classID, state.methodID, id) != null)
            type = 0;
        if (table.getVariableTypeInfo(state.classID, state.methodID, id) != null)
            type = 1;
        if (table.getFieldTypeInfo(state.classID, id) != null)
            type = 2;
        return type;
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
//private ClassType getFullClassType(String classID) {
//    ClassType t = table.type(classID);
//    ClassType full = new ClassType(t);
//    while (t.parentName() != null) {
//        ClassType parent = table.type(t.parentName());
//        boolean checks = full.inherit(parent);
//        if (!checks)
//            return null;
//        t = parent;
//    }
//    return full;
//}
//
//    public ClassType typeC(String id) {
//        return getFullClassType(id);
//    }
//
//    public MethodType typeM(String classID, String id) {
//        return table.getMethodTypeInfo(classID, id);
//    }
//
//    public Type typeF(String classID, String id) {
//        return table.getFieldTypeInfo(classID, id);
//    }
//
//    public Type typePVF(String id) {
//        Type type;
//        type = table.getParameterTypeInfo(state.classID, state.methodID, id);
//        if (type != null)
//            return type;
//        type = table.getVariableTypeInfo(state.classID, state.methodID, id);
//        if (type != null)
//            return type;
//        return table.getFieldTypeInfo(state.classID, id);
//    }
//    public Type typePVF(String name) {
//        Type type;
//        if (state.method) {
//            type = table.type(state.className, state.methodName, name); //search parameters and variables in current method
//            if (type != null)
//                return type;
//        }
//        return table.getFieldTypeInfo(state.className, name);
////        type = table.type(state.className, name); //no matching var/param; search methods in current class
////        if (type != null)
////            return type;
////        type = table.type(name); //no matching method; search classes in global, returns null if no matching classes are found
////        return type;
//    }

//    public MethodType typeMethod(String methodName) {
//        return table.type(state.className, methodName); //no matching var/param; search methods in current class
////        type = table.type(name); //no matching method; search classes in global, returns null if no matching classes are found
////        return type;
//    }

//    public Type typeClass(String className) {
//        return getFullClassType(className); //no matching var/param; search methods in current class
////        type = table.type(name); //no matching method; search classes in global, returns null if no matching classes are found
////        return type;
//
//
//        //call this at every class declaration to make sure it is checked
//    }

    public Map<Symbol, ClassType> classes() {
        return table.classes();
    }

    public void inheritAll() {
        Map<Symbol, ClassType> updated = new HashMap();
        for (var entry : table.classes().entrySet()) {
            if (!entry.getValue().hasParent()) {
                updated.put(entry.getKey(), entry.getValue());
                continue;
            }
            ClassType full = getFullClassType(entry.getValue().classID());
            if (full == null)
                throw new TypeCheckException("Rule xx inheritance violated");
            updated.put(entry.getKey(), full);
        }
        table.setClasses(updated);

    }

    public void printVapor() {
        table.classes().forEach(this::printVMT);
        printMethods(table.main());
        table.classes().forEach(this::printMethods);
        final String allocArray = "func AllocArray(size)\n  bytes = MulS(size 4)\n  bytes = Add(bytes 4)\n  v = HeapAllocZ(bytes)\n  [v] = size\n  ret v";
        System.out.println(allocArray);
    }

    private void printMethods(ClassType classType) {
        //print main first
        classType.getMethods().forEach(this::printMethod);
    }

    private void printMethods(Symbol symbol, ClassType classType) {
        if (classType.isMain())
            return;
        classType.getMethods().forEach(this::printMethod);
    }

    private void printMethod(Symbol symbol, MethodType methodType) {
        System.out.println(methodType.vapor.getSignature());
        System.out.println(methodType.vapor.statements());
    }

    private void printVMT(Symbol symbol, ClassType classType) {
        if (classType.isMain())
            return;
        System.out.println(classType.vapor.vmt());
    }

}
