import java.util.ArrayList;
import java.util.List;

public class Member {
    private String name;
    private String memberId;
    private List<Book> borrowedBooks;
    protected int maxBooksAllowed; // protected to be accessible by subclass constructor

    public Member(String name, String memberId) {
        setName(name);
        setMemberId(memberId);
        this.borrowedBooks = new ArrayList<>();
        this.maxBooksAllowed = 3; // Default for regular members
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getMemberId() {
        return memberId;
    }

    public List<Book> getBorrowedBooks() {
        // Return a copy to maintain encapsulation of the internal list
        return new ArrayList<>(borrowedBooks);
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    // Setters with validation
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Member name cannot be empty.");
        }
        this.name = name;
    }

    public void setMemberId(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID cannot be empty.");
        }
        this.memberId = memberId;
    }

    // Borrowing and returning books
    public boolean borrowBook(Book book) {
        if (book == null) {
            System.out.println("Error: Book object is null.");
            return false;
        }
        if (!book.isAvailable()) {
            System.out.println("Sorry, the book \"" + book.getTitle() + "\" is currently not available.");
            return false;
        }
        if (borrowedBooks.size() >= maxBooksAllowed) {
            System.out.println("Sorry, " + name + ", you have reached your borrowing limit of " + maxBooksAllowed + " books.");
            return false;
        }
        if (book.borrowBook()) { // This method in Book class sets its availability to false
            borrowedBooks.add(book);
            System.out.println("Book \"" + book.getTitle() + "\" borrowed successfully by " + name + ".");
            return true;
        }
        // This case should ideally not be reached if book.isAvailable() was true initially
        // and book.borrowBook() failed, indicating an internal issue.
        System.out.println("Failed to borrow book \"" + book.getTitle() + "\". Unknown error.");
        return false;
    }

    public boolean returnBook(Book book) {
        if (book == null) {
            System.out.println("Error: Book object is null for return.");
            return false;
        }
        if (borrowedBooks.remove(book)) {
            book.returnBook(); // This method in Book class sets its availability to true
            System.out.println("Book \"" + book.getTitle() + "\" returned successfully by " + name + ".");
            return true;
        }
        System.out.println("Error: Book \"" + book.getTitle() + "\" was not found in " + name + "'s borrowed list.");
        return false;
    }

    // Method for displaying member details
    public void displayDetails() {
        System.out.println("Member Name: " + name);
        System.out.println("Member ID: " + memberId);
        System.out.println("Max Books Allowed: " + maxBooksAllowed);
        System.out.println("Borrowed Books (" + borrowedBooks.size() + "):");
        if (borrowedBooks.isEmpty()) {
            System.out.println("  None");
        } else {
            for (Book book : borrowedBooks) {
                System.out.println("  - " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
            }
        }
    }

    @Override
    public String toString() {
        return "Member [name=" + name + ", memberId=" + memberId + ", borrowedBooksCount=" + borrowedBooks.size() + "]";
    }
}