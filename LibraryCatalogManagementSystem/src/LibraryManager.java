import java.util.*;

public class LibraryManager {
    private static LibraryCatalog catalog = new LibraryCatalog();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Pre-populate with some books for testing
        try {
            catalog.addBook(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", "Classic", true));
            catalog.addBook(new Book("B002", "To Kill a Mockingbird", "Harper Lee", "Classic", false));
            catalog.addBook(new Book("B003", "1984", "George Orwell", "Dystopian", true));
            catalog.addBook(new Book("B004", "Pride and Prejudice", "Jane Austen", "Romance", true));
            catalog.addBook(new Book("B005", "The Hobbit", "J.R.R. Tolkien", "Fantasy", false));
        } catch (IllegalArgumentException e) {
            System.err.println("Error initializing sample books: " + e.getMessage());
        }
        System.out.println("\n--- Initial Catalog Loaded ---");


        boolean running = true;
        while (running) {
            printMenu();
            try {
                System.out.print("Enter your choice: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // consume the invalid input
                    continue;
                }
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        addNewBook();
                        break;
                    case 2:
                        viewBookDetails();
                        break;
                    case 3:
                        updateBookDetails();
                        break;
                    case 4:
                        updateBookAvailability();
                        break;
                    case 5:
                        viewAllBooks();
                        break;
                    case 6:
                        viewAvailableBooks();
                        break;
                    case 7:
                        viewBooksByGenre();
                        break;
                    case 8:
                        viewBooksByAuthor();
                        break;
                    case 9:
                        running = false;
                        System.out.println("Exiting Library Catalog Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input type. Please enter a number for choice.");
                scanner.nextLine(); // consume the rest of the line
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace(); // For debugging
            }
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Library Catalog Management System ---");
        System.out.println("1. Add New Book");
        System.out.println("2. View Book Details (by ID)");
        System.out.println("3. Update Book Details");
        System.out.println("4. Update Book Availability");
        System.out.println("5. View All Books");
        System.out.println("6. View Available Books");
        System.out.println("7. View Books by Genre");
        System.out.println("8. View Books by Author");
        System.out.println("9. Exit");
    }

    private static void addNewBook() {
        System.out.println("\n--- Add New Book ---");
        String bookId, title, author, genre;
        boolean isAvailable = true; // Default

        while (true) {
            System.out.print("Enter Book ID (e.g., B001, B123): ");
            bookId = scanner.nextLine().trim();
            if (catalog.isValidBookIdFormat(bookId)) {
                if (catalog.findBookById(bookId) != null) {
                    System.out.println("Error: A book with this ID already exists. Please use a unique ID.");
                } else {
                    break;
                }
            } else {
                System.out.println("Invalid Book ID format. Please use the format 'B' followed by at least 3 digits (e.g., B001).");
            }
        }

        do {
            System.out.print("Enter Title: ");
            title = scanner.nextLine().trim();
            if (title.isEmpty()) System.out.println("Title cannot be empty.");
        } while (title.isEmpty());

        do {
            System.out.print("Enter Author: ");
            author = scanner.nextLine().trim();
            if (author.isEmpty()) System.out.println("Author cannot be empty.");
        } while (author.isEmpty());

        System.out.print("Enter Genre: ");
        genre = scanner.nextLine().trim(); // Genre can be empty if allowed

        System.out.print("Is the book available? (yes/no, default: yes): ");
        String availabilityInput = scanner.nextLine().trim().toLowerCase();
        isAvailable = !availabilityInput.equals("no"); // If anything other than "no", consider it "yes"

        try {
            Book newBook = new Book(bookId, title, author, genre, isAvailable);
            catalog.addBook(newBook);
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating book: " + e.getMessage());
        }
    }

    private static void viewBookDetails() {
        System.out.println("\n--- View Book Details ---");
        System.out.print("Enter Book ID to view: ");
        String bookId = scanner.nextLine().trim();

        if (!catalog.isValidBookIdFormat(bookId)) {
            System.out.println("Invalid Book ID format entered.");
            // return; // Optionally return early
        }

        Book book = catalog.findBookById(bookId);
        if (book != null) {
            System.out.println(book);
        } else {
            System.out.println("Book with ID '" + bookId + "' not found.");
        }
    }

    private static void updateBookDetails() {
        System.out.println("\n--- Update Book Details ---");
        System.out.print("Enter Book ID of the book to update: ");
        String bookId = scanner.nextLine().trim();

        Book book = catalog.findBookById(bookId);
        if (book == null) {
            System.out.println("Book with ID '" + bookId + "' not found.");
            return;
        }

        System.out.println("Current details: " + book);
        System.out.print("Enter new Title (or press Enter to keep current: '" + book.getTitle() + "'): ");
        String newTitle = scanner.nextLine().trim();
        if (newTitle.isEmpty()) newTitle = book.getTitle(); // Keep old if empty

        System.out.print("Enter new Author (or press Enter to keep current: '" + book.getAuthor() + "'): ");
        String newAuthor = scanner.nextLine().trim();
        if (newAuthor.isEmpty()) newAuthor = book.getAuthor(); // Keep old if empty

        System.out.print("Enter new Genre (or press Enter to keep current: '" + book.getGenre() + "'): ");
        String newGenre = scanner.nextLine().trim();
        if (newGenre.isEmpty()) newGenre = book.getGenre(); // Keep old if empty

        catalog.updateBookDetails(bookId, newTitle, newAuthor, newGenre);
    }

    private static void updateBookAvailability() {
        System.out.println("\n--- Update Book Availability ---");
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine().trim();

        Book book = catalog.findBookById(bookId);
        if (book == null) {
            System.out.println("Book with ID '" + bookId + "' not found.");
            return;
        }

        System.out.println("Book: " + book.getTitle() + ", Current Availability: " + (book.isAvailable() ? "Available" : "Not Available"));
        System.out.print("Set availability to (available/unavailable): ");
        String statusInput = scanner.nextLine().trim().toLowerCase();

        if (statusInput.equals("available") || statusInput.equals("yes")) {
            catalog.updateBookAvailability(bookId, true);
        } else if (statusInput.equals("unavailable") || statusInput.equals("no")) {
            catalog.updateBookAvailability(bookId, false);
        } else {
            System.out.println("Invalid availability status entered. No changes made.");
        }
    }

    private static void viewAllBooks() {
        System.out.println("\n--- All Books in Catalog ---");
        Collection<Book> books = catalog.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("The catalog is empty.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    private static void viewAvailableBooks() {
        System.out.println("\n--- Available Books ---");
        List<Book> books = catalog.getAvailableBooks();
        if (books.isEmpty()) {
            System.out.println("No books are currently available.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    private static void viewBooksByGenre() {
        System.out.println("\n--- View Books by Genre ---");
        System.out.print("Enter Genre to search: ");
        String genre = scanner.nextLine().trim();
        List<Book> books = catalog.getBooksByGenre(genre);
        if (books.isEmpty()) {
            System.out.println("No books found for genre '" + genre + "'.");
        } else {
            System.out.println("Books in genre '" + genre + "':");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    private static void viewBooksByAuthor() {
        System.out.println("\n--- View Books by Author ---");
        System.out.print("Enter Author to search: ");
        String author = scanner.nextLine().trim();
        List<Book> books = catalog.getBooksByAuthor(author);
        if (books.isEmpty()) {
            System.out.println("No books found by author '" + author + "'.");
        } else {
            System.out.println("Books by author '" + author + "':");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }
}