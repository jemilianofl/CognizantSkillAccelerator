import java.util.*;

public class Assignment03 {

    public static void main(String[] args) {
        System.out.println("\n--- Assignment 3: Using Do-While Loops ---");
        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("Welcome to the Calculator!");

        do {
            System.out.println("\nPlease select an operation:");
            System.out.println("1. Addition");
            System.out.println("2. Subtraction");
            System.out.println("3. Multiplication");
            System.out.println("4. Division");
            System.out.println("5. Exit");
            System.out.print("\nEnter your choice: ");

            choice = -1; // Reset choice for each iteration
            try {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                    scanner.next(); // Consume the invalid input
                    continue; // Skip to the next iteration of the do-while loop
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // consume the invalid token
                continue;
            }


            if (choice >= 1 && choice <= 4) {
                double num1 = 0, num2 = 0;
                boolean validInput = false;

                // Get first number with validation
                while (!validInput) {
                    try {
                        System.out.print("Enter the first number: ");
                        num1 = scanner.nextDouble();
                        validInput = true;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.next(); // Consume the invalid input
                    }
                }

                validInput = false; // Reset for second number
                // Get second number with validation
                while (!validInput) {
                    try {
                        System.out.print("Enter the second number: ");
                        num2 = scanner.nextDouble();
                        validInput = true;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.next(); // Consume the invalid input
                    }
                }


                switch (choice) {
                    case 1: // Addition
                        System.out.println("Result: " + num1 + " + " + num2 + " = " + (num1 + num2));
                        break;
                    case 2: // Subtraction
                        System.out.println("Result: " + num1 + " - " + num2 + " = " + (num1 - num2));
                        break;
                    case 3: // Multiplication
                        System.out.println("Result: " + num1 + " * " + num2 + " = " + (num1 * num2));
                        break;
                    case 4: // Division
                        if (num2 == 0) {
                            System.out.println("Error: Cannot divide by zero.");
                        } else {
                            System.out.println("Result: " + num1 + " / " + num2 + " = " + (num1 / num2));
                        }
                        break;
                }
            } else if (choice == 5) {
                System.out.println("\nExiting the calculator. Thank you!");
            } else {
                System.out.println("Invalid choice. Please select an option from 1 to 5.");
            }
            System.out.println(); // Add a blank line for readability

        } while (choice != 5);

        scanner.close(); // Close the scanner as we are done with all assignments
        System.out.println("--------------------");
    }

}
