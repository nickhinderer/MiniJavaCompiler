class Trick {
    public static void main(String[] args) {
        int i;
        int j;
        B b;
        i = 1;
        j = 100;
        while(0 < j) {
            System.out.println(j);
            j = j - 1;
            i = new B().method(i);
            b = new B();
            //i = b.setK(2);
            //i = b.getK();
            //
            System.out.println(i);
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
    //public int setK(int r) {
    //    k = r;
    //    return 0;
    //}
    //public int getK() {
    //    return k;
   // }
}