class Test{
    public static void main(String[] a) {
        {
            new Person().testA();
            new Person().testB();
        }
    }
}

class Person {
    public void testA() {
        System.out.println("401");
    }

    public void testB() {
        System.out.println(2020);
    }
}
