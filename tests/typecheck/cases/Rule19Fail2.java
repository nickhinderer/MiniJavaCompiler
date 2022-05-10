//oracle: should fail
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
    int k;
    boolean k;
    public int method(int a) {
        return 1;
    }
}


class C extends B {
}
