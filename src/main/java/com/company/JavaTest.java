package com.company;

import java.util.Arrays;

class C {
 int a = 1;
}

class D extends C {
 boolean a = false;
}
public class JavaTest {
    static int c = 1;
    C JavaTest() {

        return new D();
    }

    public static void main(String[] args) {
        int c = 2;
        int JavaTest = 1;
        System.out.println(c);
        System.out.println(JavaTest);
        System.out.println(new D().a);
        {
         int a;
//         int c;
//      int c;
        }
        String pe1 = "t0 = [this+8]";
        pe1 = "a ";
        String[] some = pe1.split(" ", 2);
        System.out.println(Arrays.toString(some));
        System.out.println(some[1].length());
        System.out.println(some[1].isEmpty());
        System.out.println(some[1].isBlank());

    }
}
