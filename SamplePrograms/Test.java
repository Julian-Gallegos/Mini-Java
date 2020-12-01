class Test{
    public static void main(String[] a) {
        System.out.println(new Person().incrementAge(40));
    }
}

class Person {
    public int incrementAge(int age) {
        int i = 30;
        int j = i + age;
        System.out.print(j);
        return 1;
    }

    public int testB() {
        System.out.println(2020);
	    return 1;
    }
}
