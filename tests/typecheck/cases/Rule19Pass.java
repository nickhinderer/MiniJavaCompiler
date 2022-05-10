//oracle: should pass
class A {

    public static void main(String[] args) {
        int a;
        int b;
        {
            System.out.println(a + b);
        }
    }
}

class B {
    boolean c;
    boolean f;
    int i;
    public int method(int a) {
        return 1;
    }
    public boolean other(boolean b) {
        return false;
    }
}


class C extends B {
}
