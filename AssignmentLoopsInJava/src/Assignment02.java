import java.util.Scanner;

public class Assignment02 {

    public static void main(String[] args) {
        System.out.println("\n--- Assignment 2: Using While Loops ---");
        Scanner scanner = new Scanner(System.in);

        // Task: User Input, Sum the Digits, Output the Result
        System.out.print("Enter a positive integer: ");
        int number;

        // Optional Enhancement: Input validation
        while (true) {
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
                if (number > 0) {
                    break; // Exit the loop if input is a positive integer
                } else {
                    System.out.print("Invalid input. Please enter a POSITIVE integer: ");
                }
            } else {
                System.out.print("Invalid input. Please enter a valid integer: ");
                scanner.next(); // Consume the non-integer input
            }
        }

        int originalNumber = number; // Keep a copy of the original number for the output
        int sumOfDigits = 0;

        while (number > 0) {
            int digit = number % 10;  // Extract the last digit
            sumOfDigits += digit;     // Add it to the sum
            number /= 10;             // Remove the last digit
        }

        System.out.println("The sum of the digits of " + originalNumber + " is: " + sumOfDigits);
        System.out.println("--------------------");

        // Optional Enhancement: Allow multiple numbers
        // We'll wrap the core logic in another loop for this.
        // Note: For simplicity, the input validation is repeated.
        // In a more complex application, you might use a separate method for input.

        System.out.println("\nOptional: Calculate sum of digits for multiple numbers (enter 0 to exit)");
        while (true) {
            System.out.print("Enter a positive integer (or 0 to exit): ");
            int multiNumber;

            while (true) {
                if (scanner.hasNextInt()) {
                    multiNumber = scanner.nextInt();
                    if (multiNumber >= 0) { // Allow 0 for exiting
                        break;
                    } else {
                        System.out.print("Invalid input. Please enter a POSITIVE integer or 0 to exit: ");
                    }
                } else {
                    System.out.print("Invalid input. Please enter a valid integer: ");
                    scanner.next();
                }
            }

            if (multiNumber == 0) {
                System.out.println("Exiting multiple number calculation.");
                break; // Exit the outer loop
            }

            int tempNumber = multiNumber;
            int currentSum = 0;
            while (tempNumber > 0) {
                int digit = tempNumber % 10;
                currentSum += digit;
                tempNumber /= 10;
            }
            System.out.println("The sum of the digits of " + multiNumber + " is: " + currentSum);
        }
        System.out.println("--------------------");
        // scanner.close(); // Close at the very end if no more assignments
    }

}
