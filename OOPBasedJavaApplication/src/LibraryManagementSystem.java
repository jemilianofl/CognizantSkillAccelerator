public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        System.out.println("===== Library Management System Startup =====");

        // 1. Adding books and eBooks to the library
        System.out.println("\n--- Adding Books ---");
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565");
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084");
        EBook ebook1 = new EBook("Java: The Complete Reference", "Herbert Schildt", "9781260440232", "PDF", 15.5);
        Book book3 = new Book("1984", "George Orwell", "9780451524935");


        library.addBook(book1);
        library.addBook(book2);
        library.addBook(ebook1); // Adding an EBook
        library.addBook(book3);
        library.addBook(new Book("The Catcher in the Rye", "J.D. Salinger", "9780316769488"));


        System.out.println("\n--- Displaying All Books (includes eBooks with specific details) ---");
        library.displayAllBooks();

        // 2. Registering regular and premium members
        System.out.println("\n--- Registering Members ---");
        Member member1 = new Member("Alice Smith", "M001");
        PremiumMember premiumMember1 = new PremiumMember("Bob Johnson (Premium)", "PM001");
        Member member2 = new Member("Carol White", "M002");

        library.registerMember(member1);
        library.registerMember(premiumMember1);
        library.registerMember(member2);

        System.out.println("\n--- Displaying Registered Members ---");
        library.displayRegisteredMembers(); // Shows polymorphism in member display

        // 3. Borrowing books for members
        System.out.println("\n--- Borrowing Transactions ---");
        // Alice (regular member, limit 3) borrows books
        library.borrowBookTransaction("M001", "9780743273565"); // Alice borrows Great Gatsby
        library.borrowBookTransaction("M001", "9780061120084"); // Alice borrows To Kill a Mockingbird

        // Bob (premium member, limit 10) borrows books
        library.borrowBookTransaction("PM001", "9781260440232"); // Bob borrows Java EBook
        library.borrowBookTransaction("PM001", "9780451524935");   // Bob borrows 1984

        // Attempt to borrow an already borrowed (unavailable) book
        System.out.println("\n--- Attempting to borrow an unavailable book ---");
        library.borrowBookTransaction("M002", "9780743273565"); // Carol tries to borrow Great Gatsby (unavailable)

        // Alice attempts to borrow a third book
        library.borrowBookTransaction("M001", "9780316769488"); // Alice borrows Catcher in the Rye

        // Alice attempts to borrow a fourth book (should hit limit)
        System.out.println("\n--- Alice attempting to borrow a 4th book (exceeds limit) ---");
        // Create a new available book for this test
        Book tempBook = new Book("Brave New World", "Aldous Huxley", "9780060850524");
        library.addBook(tempBook);
        library.borrowBookTransaction("M001", "9780060850524");


        // 4. Displaying the updated list of available books
        System.out.println("\n--- Displaying Available Books After Borrowing ---");
        library.displayAvailableBooks();

        // 5. Displaying member details along with borrowed books
        System.out.println("\n--- Displaying Member Details After Borrowing ---");
        library.displayRegisteredMembers();


        // 6. Returning a book
        System.out.println("\n--- Returning a Book ---");
        library.returnBookTransaction("M001", "9780743273565"); // Alice returns Great Gatsby

        // 7. Display available books and member details after returning
        System.out.println("\n--- Displaying Available Books After Returning ---");
        library.displayAvailableBooks();

        System.out.println("\n--- Displaying Member Details After Returning ---");
        library.displayRegisteredMembers();

        System.out.println("\n===== Demonstration Complete =====");
    }
}