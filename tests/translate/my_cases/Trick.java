class Trick {
    public static void main(String[] args) {
        int i;
        int j;
        int k;
        boolean l;
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
            k = new D().method(1);
            l = new D().method2();
            k = new D().setK(1);
            k = new D().getK();

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
    public int getK() {
        return k;
    }

}


class B extends A {
    public int setK(int r) {
        k = r;
        //k = this.method(r);
        return 0;
    }
    }


class C extends B {
    int k;
    public int method(int i) {
        return 222;
    }
}

class D extends C {
    int k;
    public boolean method2() {
        return false;
    }
}