package com.company;

enum TYPE {
    PRIMITIVE, ARRAY, CLASS, METHOD
}
public abstract class Type {
    TYPE type;
    public abstract boolean equals(Type other);
//    public boolean equals(Type other) {
//        if (other.type != this.type)
//            return false;
//        if (this.type ==)
//    }
}
