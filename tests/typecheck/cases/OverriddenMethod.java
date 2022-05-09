//oracle: should fail
class RECF {
    public static void main(String[] args) {

    }
}

class A {
    int a;
    int b;
    public int method() {
        return 1;
    }
}

class B extends A {
    boolean b;
    int c;
    public int method(int a) {
        return 1;
    }

}