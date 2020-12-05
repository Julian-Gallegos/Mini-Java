class SemanticsTest {
    public static void main(String[] args) {
        System.out.println(new Test().testMethod(3, 10));
    }
}

class A {
    int a;
}

class Test extends A {
    int a;
    public int testMethod(int a) {
        return 10;
    }
}