public class EBook extends Book {
    private String fileFormat; // e.g., PDF, EPUB
    private double fileSize;   // e.g., in MB

    public EBook(String title, String author, String isbn, String fileFormat, double fileSize) {
        super(title, author, isbn); // Call the constructor of the Book class
        setFileFormat(fileFormat);
        setFileSize(fileSize);
    }

    // Getters
    public String getFileFormat() {
        return fileFormat;
    }

    public double getFileSize() {
        return fileSize;
    }

    // Setters with validation
    public void setFileFormat(String fileFormat) {
        if (fileFormat == null || fileFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("File format cannot be empty.");
        }
        this.fileFormat = fileFormat;
    }

    public void setFileSize(double fileSize) {
        if (fileSize <= 0) {
            throw new IllegalArgumentException("File size must be positive.");
        }
        this.fileSize = fileSize;
    }

    // Override displayDetails to include eBook specific information
    @Override
    public void displayDetails() {
        super.displayDetails(); // Display common book details
        System.out.println("File Format: " + fileFormat);
        System.out.println("File Size (MB): " + fileSize);
    }

    @Override
    public String toString() {
        return "EBook [title=" + getTitle() + ", author=" + getAuthor() + ", isbn=" + getIsbn() +
                ", available=" + isAvailable() + ", fileFormat=" + fileFormat + ", fileSize=" + fileSize + "MB]";
    }
}