class Test{
    public static void main(String[] a) {
        {
            Person p = new Person();
            p.testA();
            p.testB();
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
