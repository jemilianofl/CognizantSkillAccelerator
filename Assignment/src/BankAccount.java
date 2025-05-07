import java.text.*;
import java.util.*;


public class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    public BankAccount(String accountNumber, String accountHolder, double initialBalance) throws InvalidAmountException {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        }
        if (accountHolder == null || accountHolder.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be null or empty.");
        }
        if (initialBalance < 0) {
            throw new InvalidAmountException("Initial balance cannot be negative: " + currencyFormatter.format(initialBalance));
        }
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    public String getFormattedBalance() {
        return currencyFormatter.format(this.balance);
    }

    public void deposit(double amount) throws InvalidAmountException {
        System.out.println("\nAttempting to deposit: " + currencyFormatter.format(amount));
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive. Attempted: " + currencyFormatter.format(amount));
        }
        this.balance += amount;
        System.out.println("Deposit successful. New balance: " + getFormattedBalance());
    }

    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        System.out.println("\nAttempting to withdraw: " + currencyFormatter.format(amount));
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive. Attempted: " + currencyFormatter.format(amount));
        }
        if (amount > this.balance) {
            throw new InsufficientFundsException(
                    "Withdrawal amount " + currencyFormatter.format(amount) +
                            " exceeds current balance of " + getFormattedBalance());
        }
        this.balance -= amount;
        System.out.println("Withdrawal successful. New balance: " + getFormattedBalance());
    }

    @Override
    public String toString() {
        return "BankAccount {" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountHolder='" + accountHolder + '\'' +
                ", balance=" + getFormattedBalance() +
                '}';
    }
}
