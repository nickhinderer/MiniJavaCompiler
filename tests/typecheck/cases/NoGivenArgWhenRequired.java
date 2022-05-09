//oracle: should fail
class RECF {
    public static void main(String[] args) {
        B b;
        int i;
        b = new B();
        i = b.method();
    }
}

class A {
    int a;
    int b;
}

class B extends A {
    boolean b;
    int c;
    public int method(int a) {
        return 0;
    }
}