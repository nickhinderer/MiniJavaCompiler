//oracle: should fail
class A {

    public static void main(String[] args) {
        int a;
        B b;
        C c;
        b = new B();
        c = b;
    }
}

class B {
    public int method(int a) {
        return 1;
    }
}


class C extends B {
}
