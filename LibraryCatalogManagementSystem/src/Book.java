import java.util.*;

public class Book {
    private String bookId; // e.g., "B001"
    private String title;
    private String author;
    private String genre;
    private boolean isAvailable;

    public Book(String bookId, String title, String author, String genre, boolean isAvailable) {
        // Basic validation can be done here or before calling constructor
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be null or empty.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = isAvailable;
    }

    // Getters
    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setters
    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        } else {
            System.out.println("Error: Title cannot be set to null or empty.");
        }
    }

    public void setAuthor(String author) {
        if (author != null && !author.trim().isEmpty()) {
            this.author = author;
        } else {
            System.out.println("Error: Author cannot be set to null or empty.");
        }
    }

    public void setGenre(String genre) {
        this.genre = genre; // Genre can be empty or have specific validation later
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // bookId is generally not changed after creation, so no setter for it.

    @Override
    public String toString() {
        return "Book ID: " + bookId +
                ", Title: '" + title + '\'' +
                ", Author: '" + author + '\'' +
                ", Genre: '" + genre + '\'' +
                ", Available: " + (isAvailable ? "Yes" : "No");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(bookId, book.bookId); // Uniqueness based on Book ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }
}