class Test{
    public static void main(String[] a) {
        System.out.println(new B().test());
    }
}

class A {
    int a;

    public int test() {
        return 2020;
    }

    public int getNum() {
        return 10;
    }
}

class B extends A {
    public int test() {
        a = 100;
        return a + this.getNum() + getNum();
    }
}