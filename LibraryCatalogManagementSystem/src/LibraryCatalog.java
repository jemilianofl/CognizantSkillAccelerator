import java.util.*;
import java.util.stream.*;

public class LibraryCatalog {
    private Map<String, Book> books; // Key: bookId, Value: Book object

    public LibraryCatalog() {
        this.books = new HashMap<>();
    }

    // --- Data Validation for Book ID ---
    public boolean isValidBookIdFormat(String bookId) {
        if (bookId == null) return false;
        // Example format: B followed by 3 digits (e.g., B001, B123)
        return bookId.matches("B\\d{3,}"); // B followed by at least 3 digits
    }

    // --- CRUD Operations ---
    public boolean addBook(Book book) {
        if (book == null || !isValidBookIdFormat(book.getBookId())) {
            System.out.println("Error: Invalid book data or Book ID format. Book not added.");
            return false;
        }
        if (books.containsKey(book.getBookId())) {
            System.out.println("Error: Book with ID '" + book.getBookId() + "' already exists. Book not added.");
            return false;
        }
        books.put(book.getBookId(), book);
        System.out.println("Book '" + book.getTitle() + "' added successfully.");
        return true;
    }

    public Book findBookById(String bookId) {
        if (!isValidBookIdFormat(bookId)) {
            System.out.println("Info: Invalid Book ID format for search.");
            // return null; // or throw an exception
        }
        return books.get(bookId);
    }

    public boolean updateBookDetails(String bookId, String newTitle, String newAuthor, String newGenre) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Error: Book with ID '" + bookId + "' not found. Cannot update.");
            return false;
        }
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            book.setTitle(newTitle);
        }
        if (newAuthor != null && !newAuthor.trim().isEmpty()) {
            book.setAuthor(newAuthor);
        }
        if (newGenre != null) { // Genre can be empty string if allowed by business logic
            book.setGenre(newGenre);
        }
        System.out.println("Book '" + bookId + "' details updated.");
        return true;
    }

    public boolean updateBookAvailability(String bookId, boolean isAvailable) {
        Book book = findBookById(bookId);
        if (book == null) {
            System.out.println("Error: Book with ID '" + bookId + "' not found. Cannot update availability.");
            return false;
        }
        book.setAvailable(isAvailable);
        System.out.println("Book '" + bookId + "' availability updated to: " + (isAvailable ? "Available" : "Not Available"));
        return true;
    }

    // --- Viewing Operations ---
    public Collection<Book> getAllBooks() {
        if (books.isEmpty()){
            return new ArrayList<>(); // Return empty list instead of null
        }
        return new ArrayList<>(books.values()); // Return a new list to prevent modification of internal map values directly
    }

    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Book> getBooksByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            System.out.println("Error: Genre cannot be empty for search.");
            return new ArrayList<>();
        }
        String lowerCaseGenre = genre.toLowerCase();
        return books.values().stream()
                .filter(book -> book.getGenre() != null && book.getGenre().toLowerCase().contains(lowerCaseGenre))
                .collect(Collectors.toList());
    }

    public List<Book> getBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            System.out.println("Error: Author cannot be empty for search.");
            return new ArrayList<>();
        }
        String lowerCaseAuthor = author.toLowerCase();
        return books.values().stream()
                .filter(book -> book.getAuthor() != null && book.getAuthor().toLowerCase().contains(lowerCaseAuthor))
                .collect(Collectors.toList());
    }
}