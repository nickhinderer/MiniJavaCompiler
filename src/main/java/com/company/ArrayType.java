package com.company;

public class ArrayType extends Type {
    public int length;
    public ArrayType() {
        this.type = TYPE.ARRAY;
        this.length = -1;
    }
}
