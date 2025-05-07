import java.util.*;

public class BankingApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Resource that should be closed

        BankAccount account1 = null;

        try {
            System.out.println("--- Creating a New Bank Account ---");
            // Scenario 1: Create a new bank account
            try {
                account1 = new BankAccount("ACC001", "Alice Wonderland", 1000.00);
                System.out.println("Account created successfully: " + account1);
                System.out.println("Initial Balance: " + account1.getFormattedBalance());
            } catch (InvalidAmountException e) {
                System.err.println("Error creating account: " + e.getMessage());
                // Exit or handle appropriately if account creation fails critically
                return;
            } catch (IllegalArgumentException e) {
                System.err.println("Error creating account due to invalid argument: " + e.getMessage());
                return;
            }


            // Scenario 2: Attempting to deposit a negative amount
            System.out.println("\n--- Attempting to Deposit Invalid Amount ---");
            try {
                account1.deposit(-50.00);
            } catch (InvalidAmountException e) {
                System.err.println("Deposit Error: " + e.getMessage());
                System.out.println("Current Balance after failed deposit attempt: " + account1.getFormattedBalance());
            }

            // Scenario 3: Attempting to deposit a zero amount
            System.out.println("\n--- Attempting to Deposit Zero Amount ---");
            try {
                account1.deposit(0.00);
            } catch (InvalidAmountException e) {
                System.err.println("Deposit Error: " + e.getMessage());
                System.out.println("Current Balance after failed deposit attempt: " + account1.getFormattedBalance());
            }

            // Scenario 4: Making a valid deposit
            System.out.println("\n--- Making a Valid Deposit ---");
            try {
                account1.deposit(200.00);
            } catch (InvalidAmountException e) {
                // This should not happen for a valid amount
                System.err.println("Unexpected Deposit Error: " + e.getMessage());
            }


            // Scenario 5: Attempting to withdraw an amount exceeding the current balance
            System.out.println("\n--- Attempting to Withdraw More Than Balance ---");
            try {
                account1.withdraw(1500.00); // Balance is 1200 at this point
            } catch (InsufficientFundsException e) {
                System.err.println("Withdrawal Error: " + e.getMessage());
                System.out.println("Current Balance after failed withdrawal attempt: " + account1.getFormattedBalance());
            } catch (InvalidAmountException e) {
                System.err.println("Withdrawal Error (Invalid Amount): " + e.getMessage());
            }

            // Scenario 6: Attempting to withdraw a negative amount
            System.out.println("\n--- Attempting to Withdraw Invalid Amount ---");
            try {
                account1.withdraw(-100.00);
            } catch (InsufficientFundsException e) {
                System.err.println("Withdrawal Error: " + e.getMessage());
            } catch (InvalidAmountException e) {
                System.err.println("Withdrawal Error (Invalid Amount): " + e.getMessage());
                System.out.println("Current Balance after failed withdrawal attempt: " + account1.getFormattedBalance());
            }


            // Scenario 7: Making a valid withdrawal
            System.out.println("\n--- Making a Valid Withdrawal ---");
            try {
                account1.withdraw(300.00); // Balance is 1200, after this it should be 900
            } catch (InsufficientFundsException | InvalidAmountException e) {
                // This should not happen for a valid amount and sufficient funds
                System.err.println("Unexpected Withdrawal Error: " + e.getMessage());
            }

            System.out.println("\n--- Final Account Status ---");
            if (account1 != null) {
                System.out.println(account1);
                System.out.println("Final Balance: " + account1.getFormattedBalance());
            }

            // Demonstrating creating an account with an invalid initial balance
            System.out.println("\n--- Attempting to Create Account with Negative Initial Balance ---");
            BankAccount account2 = null;
            try {
                account2 = new BankAccount("ACC002", "Bob The Builder", -100.00);
                System.out.println("Account created: " + account2); // Should not reach here
            } catch (InvalidAmountException e) {
                System.err.println("Error creating account: " + e.getMessage());
            } catch (IllegalArgumentException e) { // For other constructor validations
                System.err.println("Error creating account: " + e.getMessage());
            }


        } finally {
            // Ensure proper resource management by closing the Scanner
            if (scanner != null) {
                System.out.println("\nClosing resources...");
                scanner.close();
            }
        }
    }
}
