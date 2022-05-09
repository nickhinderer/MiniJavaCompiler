//oracle: should pass
class RECF {
    public static void main(String[] args) {
        A a;
        B b;
        a = new A();
        b = new B();
        a = b;
    }
}

class A {
    int a;
    int b;
    public int method() {
        return 1;
    }
}

class B extends A {
    int c;
}