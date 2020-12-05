class SemanticsTest {
    public static void main(String[] args) {
        System.out.println(new Test().testMethod(3));
    }
}

class C {
    int a; 
}

class A extends C {
    int b; 
}

class Test extends A {

    public int testMethod(int a) {
	int b;
	b = 20; 
	return b;
    }
    
}
