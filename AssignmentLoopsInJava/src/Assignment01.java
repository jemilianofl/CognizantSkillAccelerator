import java.util.Scanner;

public class Assignment01 {
    public static void main(String[] args) {
        System.out.println("--- Assignment 1: Using For Loops ---");

        // Task 1: Print Numbers
        System.out.println("\nTask 1: Print Numbers from 1 to 10");
        for (int i = 1; i <= 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println("\n--------------------");

        // Task 2: Calculate Sum
        System.out.println("\nTask 2: Calculate Sum of the first 10 positive integers");
        int sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += i; // This is equivalent to sum = sum + i;
        }
        System.out.println("The sum of the first 10 positive integers is: " + sum);
        System.out.println("--------------------");

        // Task 3: Print Multiplication Table
        System.out.println("\nTask 3: Print Multiplication Table");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number to print its multiplication table: ");
        int number = scanner.nextInt();

        System.out.println("Multiplication table for " + number + ":");
        for (int i = 1; i <= 10; i++) {
            System.out.println(number + " x " + i + " = " + (number * i));
        }
        System.out.println("--------------------");

        // scanner.close(); // We will close the scanner at the end of all assignments if needed elsewhere
        // or create new scanners for each assignment block. For now, let's keep it simple.
    }
}