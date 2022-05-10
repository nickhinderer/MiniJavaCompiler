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
    public int method(int a) {
        return 1;
    }
}


class C extends B {
    public int method(int b) {
        return 0;
    }
}
