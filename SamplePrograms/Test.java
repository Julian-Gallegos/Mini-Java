class Test{
    public static void main(String[] a) {
        System.out.println(new Person().incrementAge());
    }
}

class Person {
    public int incrementAge() {
        int i;
        int j;
        i = 30;
        j = i;
        System.out.print(j);
        return 1;
    }

    public int testB() {
        System.out.println(2020);
	    return 1;
    }
}
