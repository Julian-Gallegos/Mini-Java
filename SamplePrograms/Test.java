class Test{
    public static void main(String[] a) {
        System.out.println(new Person().testA(200));
    }
}

class Person {
 
    public int testA(int w) {
	BankAccount u;
	u = new BankAccount();
	return u.increment(w); 
    }
 
    public int controlFlowTest() {
	int a;
	int b;
	b = 7;
	a = 1;
	while (a < 10 && !(b < a)) {
	    a = a + 1; 
	}
	return a; 
    }

    public int incrementAge(int age) {
        int i;
        int j;
        i = 30;
        j = i + age;
        System.out.println(j);
        return 1;
    }

    public int testB() {
        System.out.println(2020);
	return 1;
    }
}

class BankAccount {
    int balance;

    public int increment(int i) {
	balance = i;
	return balance + 500; 
    }
}
