class NestedLoops {
    public static void main(String[] args) {
        int i;
        int j;
        int k;
        boolean b;
        N n;
        int[] arr;
        int z;
        T t;
        System.out.println(1);
        i = 0;
        b = true;
        n = new N();
        arr = new int[100];
        while (i < 100) {
            arr[i] = i;
            i = i + 1;
        }
        i = 0;
        while(i < 100) {
            j = 0;
            System.out.println(i);
            while (j < 10) {
                if (b) {
                    System.out.println(j);
                    k = 10;
                    z = n.method(1);
                    while (0 < (k - z)) {
                        System.out.println(k);
                        k = k - 1;
                        z = n.method(1);
                    }
                    b = false;
                } else {
                    System.out.println(j + i);
                }
                j = j + 1;
            }
            j = 10;
            while (0 < j) {
                System.out.println(j);
                j = j - 1;
            }
            i = i + 1;
        }
        i = 0;
        j = 0;
        while (i < 100) {
            j = arr[i];
            System.out.println(j);
            System.out.println(arr.length);
            i = i + 1;
        }
        t = new T();
        z = t.method(6);
        z = t.method(6);
        z = new T().method(6);
        z = t.method(6);
        z = t.method(6);
        z = t.method(6);
        z = t.method(6);
        z = t.method(6);
        z = t.method(6);
    }
}


class N {
    int i;
    public int method(int q) {
        int k;
        i = 0;
        k = 10;
        while (i < 10) {
            while (0 < (k + (0 + 0))) {
                System.out.println(k);
                k = k - 3;
                System.out.println(k);

            }
            System.out.println(i + k);
            System.out.println(i * k);
            System.out.println(k - i);
            i = i + 2;
        }
        return 1;
    }
}


class T extends N {
    int[] arr;
    public int method(int i) {
        int temp;
        arr = new int[15];
        i = 0;
        while ((i < 15)) {
            arr[i] = (0 - i);
            temp = arr[i];
            System.out.println(temp);
            i = i + 4;
        }
        return 1;
    }
}