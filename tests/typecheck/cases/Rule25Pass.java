//oracle: should pass
class A {

    public static void main(String[] args) {
        int[] a;
        boolean b;
        a = new int[2];
        if (0 < 1) {
            System.out.println(1);
            b = 1 < a[0];
        } else {
            System.out.println(1);
        }
    }
}

class B {
    public int method(int a) {
        return 1;
    }
}


class C extends B {
}
