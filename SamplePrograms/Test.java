class Test {
    public static void main(String[] a) {
	System.out.println(new Two().getIt());
    }
}

class One {
    public int setTag() {
	return 1;  
    }

    public int getTag() {
	return 1; 
    }

    public int setIt(int it) {
	return 1;  
    }

    public int getIt() {
	return 2020; 
    }
}

class Two extends One {

    public int setTag() {
	return 1; 
    }
    
    public int getThat() {
	return 1; 
    }

    public int resetIt() {
	return 1;  
    }
}
