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
            k = b.method(k);
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
    public int method(int k) {
        return 1;
    }
}