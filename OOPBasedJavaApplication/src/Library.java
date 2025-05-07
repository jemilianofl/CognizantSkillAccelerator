import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private List<Member> members;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    // Book management
    public void addBook(Book book) {
        if (book == null) {
            System.out.println("Cannot add a null book.");
            return;
        }
        // Optional: Check for duplicate ISBN before adding
        for (Book existingBook : books) {
            if (existingBook.getIsbn().equals(book.getIsbn())) {
                System.out.println("Error: Book with ISBN " + book.getIsbn() + " already exists.");
                return;
            }
        }
        this.books.add(book);
        System.out.println("Added to library: \"" + book.getTitle() + "\" by " + book.getAuthor());
    }

    public Book findBookByIsbn(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null; // Not found
    }

    public void displayAvailableBooks() {
        System.out.println("\n--- Available Books in Library ---");
        boolean found = false;
        for (Book book : books) {
            if (book.isAvailable()) {
                book.displayDetails(); // Polymorphic call (Book or EBook details)
                System.out.println("-----");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books are currently available.");
        }
    }

    public void displayAllBooks() { // Added for completeness
        System.out.println("\n--- All Books in Library ---");
        if (books.isEmpty()) {
            System.out.println("The library has no books.");
            return;
        }
        for (Book book : books) {
            book.displayDetails(); // Polymorphic call
            System.out.println("-----");
        }
    }


    // Member management
    public void registerMember(Member member) {
        if (member == null) {
            System.out.println("Cannot register a null member.");
            return;
        }
        // Optional: Check for duplicate Member ID
        for (Member existingMember : members) {
            if (existingMember.getMemberId().equals(member.getMemberId())) {
                System.out.println("Error: Member with ID " + member.getMemberId() + " already registered.");
                return;
            }
        }
        this.members.add(member);
        System.out.println("Registered member: " + member.getName() + " (ID: " + member.getMemberId() + ")");
    }

    public Member findMemberById(String memberId) {
        for (Member member : members) {
            if (member.getMemberId().equals(memberId)) {
                return member;
            }
        }
        return null; // Not found
    }

    public void displayRegisteredMembers() {
        System.out.println("\n--- Registered Library Members ---");
        if (members.isEmpty()) {
            System.out.println("No members are currently registered.");
            return;
        }
        for (Member member : members) {
            member.displayDetails(); // Polymorphic call (Member or PremiumMember details)
            System.out.println("-----");
        }
    }

    // Transaction management
    public void borrowBookTransaction(String memberId, String isbn) {
        System.out.println("\nAttempting borrow: Member ID " + memberId + ", Book ISBN " + isbn);
        Member member = findMemberById(memberId);
        Book book = findBookByIsbn(isbn);

        if (member == null) {
            System.out.println("Transaction failed: Member with ID " + memberId + " not found.");
            return;
        }
        if (book == null) {
            System.out.println("Transaction failed: Book with ISBN " + isbn + " not found.");
            return;
        }

        member.borrowBook(book); // Member's borrowBook method handles availability & limit checks
    }

    public void returnBookTransaction(String memberId, String isbn) {
        System.out.println("\nAttempting return: Member ID " + memberId + ", Book ISBN " + isbn);
        Member member = findMemberById(memberId);
        Book book = findBookByIsbn(isbn); // Assuming book object needs to be identified for return

        if (member == null) {
            System.out.println("Transaction failed: Member with ID " + memberId + " not found.");
            return;
        }
        if (book == null) {
            // This check might be redundant if returnBook on Member only cares about the object
            // being in its borrowed list, but good for confirming book exists in library system.
            System.out.println("Transaction failed: Book with ISBN " + isbn + " not found in library records (though it might be in member's list).");
            // Proceeding to let member attempt return based on their list
        }

        // Find the specific instance of the book the member borrowed, if ISBNs can be non-unique
        // For simplicity, assuming the 'book' object from library is the one to be returned.
        // A more robust system might require checking the member's specific borrowed book instance.
        member.returnBook(book);
    }
}