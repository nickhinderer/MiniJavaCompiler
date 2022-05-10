class Trick {
    public static void main(String[] args) {
        int i;
        int j;
        int k;
        B b;
        i = 1;
        j = 100;
        b = new B();

        while(0 < j) {
            //System.out.println(j);
            j = j - 1;
            k = b.setK(j);
//            b = new B();
            k = b.getK();
            System.out.println(k);
            k = b.method2(new A());
            //i = b.setK(2);
            //i = b.getK();
            //
            //System.out.println(i);
            //i = new B().getK();
        }

    }
}
class A {
    int k;
    public int method(int i) {
        while((0 - i) < 0) {
            System.out.println(i);
            i = i * 2;
        }
        return i;
    }
}


class B extends A {
    int j;
    public int method(int a) {
        return 1;
    }
    public int method2(A a) {
        j = a.method(12);
        return 0;
    }
}