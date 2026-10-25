package library;

// Unit I: Base class for inheritance.
// A Person has only a name. Member will extend this class.
public class Person {

    private String name;   // private field = encapsulation

    // Constructor
    public Person(String name) {
        this.name = name;
    }

    // Getter
    public String getName() {
        return name;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    // Shows the details of the person (Member overrides this)
    public String getDetails() {
        return "Name: " + name;
    }
}
