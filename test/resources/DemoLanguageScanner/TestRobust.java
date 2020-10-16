/*
This is a simple class.
*/
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello!"); 

        Person[] people = new Person[3]; 
        people[0] = new Person("steve", 5, true);
        people[1] = new Person("john", 10, false);
        people[2] = new Person("justin", 3, true);
        
        // each person will say hello.
        for (int i = 0; i < people.length; i++) {
            Person p = people[i];
            int j = p.getRepeat() * 2; 
            System.out.println(p.getName() + "_says_hello!");
            while (j != 0) {
                if (p.isEmployed() && true) {
                    System.out.println("Where's_my_hazard_pay!");
                } else {
                    System.out.println("Where's_my_stimulus?");
                }
                j--;
            }
        }
    }
}

/*
extending object is redundant. 
*/ 
class Person extends Object {
    private String name; 
    private int repeat; 
    private boolean isEmployed; 

    Person(String name, int repeat, boolean isEmployed) {
        this.name = name; 
        this.repeat = repeat; 
        this.isEmployed = isEmployed;
    }

    int getRepeat() {
        return repeat; 
    }

    boolean isEmployed() {
        return isEmployed; 
    }

    String getName() {
        return name; 
    }
}