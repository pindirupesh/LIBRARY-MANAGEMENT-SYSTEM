package library;

// Unit I: Inheritance - Member IS-A Person.
// Member adds a member ID to the name it gets from Person.
public class Member extends Person {

    private int memberId;

    // Constructor uses super() to call the Person constructor
    public Member(int memberId, String name) {
        super(name);
        this.memberId = memberId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    // Method overriding: uses super.getDetails() and adds the ID
    @Override
    public String getDetails() {
        return "Member ID: " + memberId + ", " + super.getDetails();
    }
}
