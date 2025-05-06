import java.util.Scanner;

public class Calculadora {

    public static void main(String[] args) {
        // Create a Scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Declare variables
        double num1, num2, result = 0;
        char operation;
        char incrementOption;

        // Request and read the numbers
        System.out.print("Enter the first number: ");
        num1 = scanner.nextDouble();

        System.out.print("Enter the second number: ");
        num2 = scanner.nextDouble();

        // Request and read the operation
        System.out.print("Choose an operation (+, -, *, /): ");
        operation = scanner.next().charAt(0);

        // Perform the operation using switch
        switch (operation) {
            case '+':
                result = num1 + num2;
                System.out.println("Sum result: " + result);
                break;
            case '-':
                result = num1 - num2;
                System.out.println("Subtraction result: " + result);
                break;
            case '*':
                result = num1 * num2;
                System.out.println("Multiplication result: " + result);
                break;
            case '/':
                if (num2 != 0) {
                    result = num1 / num2;
                    System.out.println("Division result: " + result);
                } else {
                    System.out.println("Error: Cannot divide by zero.");
                    // Exit the program if an error occurs
                    scanner.close();
                    return;
                }
                break;
            default:
                System.out.println("Invalid operation. Must be +, -, * or /.");
                // Exit the program if an error occurs
                scanner.close();
                return;
        }

        // Ask if user wants to increment or decrement the result
        System.out.print("Do you want to increment or decrement the result by 1? (i/d/n): ");
        incrementOption = scanner.next().charAt(0);

        // Increment or decrement the result based on user's choice
        if (incrementOption == 'i' || incrementOption == 'I') {
            result++;
            System.out.println("Result after incrementing: " + result);
        } else if (incrementOption == 'd' || incrementOption == 'D') {
            result--;
            System.out.println("Result after decrementing: " + result);
        } else {
            System.out.println("No changes were made to the result.");
        }

        // Close the scanner
        scanner.close();
    }

}
