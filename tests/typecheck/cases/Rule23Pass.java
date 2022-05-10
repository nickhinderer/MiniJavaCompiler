//oracle: should pass
class A {

    public static void main(String[] args) {
        int a;
        B b;
        C c;
        c = new C();
        b = c;
    }
}

class B {
    public int method(int a) {
        return 1;
    }
}


class C extends B {
}
