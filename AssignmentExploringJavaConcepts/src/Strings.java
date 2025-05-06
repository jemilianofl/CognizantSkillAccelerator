import java.util.Scanner;

public class Strings {
    public static void main(String[] args) {
        // Create a Scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Request and read the user's first name
        System.out.print("Please enter your first name: ");
        String firstName = scanner.nextLine();

        // Request and read the user's last name
        System.out.print("Please enter your last name: ");
        String lastName = scanner.nextLine();

        // String manipulation
        String fullName = firstName + " " + lastName;
        String fullNameUppercase = fullName.toUpperCase();

        // Count how many times the first letter of the full name appears
        char firstLetter = fullName.charAt(0);
        int counter = 0;

        for (int i = 0; i < fullName.length(); i++) {
            if (fullName.charAt(i) == firstLetter) {
                counter++;
            }
        }

        // Display the results
        System.out.println("\nFull name: " + fullName);
        System.out.println("Full name in uppercase: " + fullNameUppercase);
        System.out.println("The letter '" + firstLetter + "' appears " + counter + " times in the full name.");

        // Close the scanner
        scanner.close();
    }
}
