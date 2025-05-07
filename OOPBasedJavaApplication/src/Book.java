public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;

    public Book(String title, String author, String isbn) {
        setTitle(title);
        setAuthor(author);
        setIsbn(isbn);
        this.isAvailable = true; // New books are available by default
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setters with validation
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        this.title = title;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty.");
        }
        this.author = author;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            // Basic ISBN validation could be more complex (e.g., length, format)
            throw new IllegalArgumentException("ISBN cannot be empty.");
        }
        this.isbn = isbn;
    }

    // Public method to change availability, not a direct setter for isAvailable
    public boolean borrowBook() {
        if (this.isAvailable) {
            this.isAvailable = false;
            return true;
        }
        return false;
    }

    public void returnBook() {
        this.isAvailable = true;
    }

    // Method for displaying book details (can be overridden)
    public void displayDetails() {
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Available: " + (isAvailable ? "Yes" : "No"));
    }

    @Override
    public String toString() {
        return "Book [title=" + title + ", author=" + author + ", isbn=" + isbn + ", available=" + isAvailable + "]";
    }
}