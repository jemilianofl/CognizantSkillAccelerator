public class PremiumMember extends Member {

    public PremiumMember(String name, String memberId) {
        super(name, memberId);
        this.maxBooksAllowed = 10; // Premium members can borrow more books
    }

    // Override displayDetails to indicate premium status
    @Override
    public void displayDetails() {
        System.out.println("--- Premium Member ---"); // Indication of premium status
        super.displayDetails(); // Call the base class method to print common details
        // Optionally add any premium-specific details to display here
    }

    // The borrowBook and returnBook methods from the Member class will be inherited.
    // Since maxBooksAllowed is set in the constructor, the inherited borrowBook method
    // will correctly use the premium member's limit.
    // If PremiumMember had other special borrowing rules, then borrowBook would be overridden.

    @Override
    public String toString() {
        return "PremiumMember [name=" + getName() + ", memberId=" + getMemberId() +
                ", borrowedBooksCount=" + getBorrowedBooks().size() + ", maxAllowed=" + maxBooksAllowed + "]";
    }
}