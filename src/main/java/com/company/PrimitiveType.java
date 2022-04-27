package com.company;

public class PrimitiveType extends Type {
    public String subType;
    //you'd also have stuff like visibility and other modifiers like static or volatile
    public PrimitiveType(String subType) {
        this.subType = subType;
        this.type = TYPE.PRIMITIVE;
    }

    public boolean equals(Type other) {
        if (other.type != this.type)
            return false;
        return this.subType.equals(((PrimitiveType) other).subType);
    }
}
