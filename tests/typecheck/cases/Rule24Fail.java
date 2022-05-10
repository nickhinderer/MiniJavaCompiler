//oracle: should fail
class A {

    public static void main(String[] args) {
        int[] a;
        a = new int[2];
        a[3] = false;
    }
}

class B {
    public int method(int a) {
        return 1;
    }
}


class C extends B {
}
