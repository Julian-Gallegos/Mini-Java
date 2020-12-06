class Test {
    public static void main(String[] a) {
	System.out.println(new Two().getIt(new One().hello()));
    }
}

class One {
    public int hello() {
	return 20; 
    }
}

class Two {
    public int getIt(int a) {
	return a + 10; 
    }
}
