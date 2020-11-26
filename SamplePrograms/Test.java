class Test{
    public static void main(String[] a) {
	System.out.println(new Person().testA());
    }
}

class Person {
    public int testA() {
        System.out.println(401);
	return 1; 
    }

    public int testB() {
        System.out.println(2020);
	return 1; 
    }
}
