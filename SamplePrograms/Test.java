class Test {
    public static void main(String[] a) {
	System.out.println(new Two().hi());
    }
}

class Two {
    public int hi() {
	boolean out;
	int a; 
	out = true;
        a = 0; 

	while (out) {
	    if (10 < a) {
		out = true; 
	    } else {
		a = a + 1;
	    }
	}
	return a; 
    }
}
