class SemanticsTest {
    public static void main(String[] args) {
	System.out.println(new C().hello());
    }
}

class A {
    public int hello() {
	return 1010; 
    }
}

class C extends A {

}

