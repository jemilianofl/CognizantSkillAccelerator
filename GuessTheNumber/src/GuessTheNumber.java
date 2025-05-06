import java.util.*;

public class GuessTheNumber {

    // --- Configuration ---
    // You can change these values to adjust the game.
    // For user-defined range, you'd prompt the user for these at the start of playGame().
    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 100;
    private static final int MAX_ATTEMPTS = 10;
    // --- End Configuration ---

    private static Scanner scanner = new Scanner(System.in); // Used for user input throughout the game
    private static Random random = new Random();           // Used to generate random numbers

    public static void main(String[] args) {
        System.out.println("Welcome to the Guess the Number Game!");
        System.out.println("------------------------------------");

        boolean playAgain;

        do {
            playGame(); // Start a new game round
            System.out.print("\nDo you want to play again? (yes/no): ");
            String playAgainResponse = scanner.next().toLowerCase();
            playAgain = playAgainResponse.equals("yes") || playAgainResponse.equals("y");
            scanner.nextLine(); // Consume the leftover newline character
            System.out.println(); // Add a blank line for better readability
        } while (playAgain);

        System.out.println("Thanks for playing! Goodbye.");
        scanner.close(); // Close the scanner when the application exits
    }

    /**
     * Manages a single round of the Guess the Number game.
     */
    private static void playGame() {
        // 1. Random Number Generation
        int secretNumber = random.nextInt(MAX_RANGE - MIN_RANGE + 1) + MIN_RANGE;
        int attemptsMade = 0;
        boolean guessedCorrectly = false;

        System.out.println("I have generated a number between " + MIN_RANGE + " and " + MAX_RANGE + ".");
        System.out.println("You have " + MAX_ATTEMPTS + " attempts to guess it.");
        System.out.println("------------------------------------");

        // Game loop for attempts
        while (attemptsMade < MAX_ATTEMPTS && !guessedCorrectly) {
            System.out.print("Attempt " + (attemptsMade + 1) + "/" + MAX_ATTEMPTS + " - Enter your guess: ");
            int userGuess;

            // 2. User Input and Validation
            try {
                userGuess = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline

                if (userGuess < MIN_RANGE || userGuess > MAX_RANGE) {
                    System.out.println("Oops! Your guess is outside the valid range (" + MIN_RANGE + "-" + MAX_RANGE + "). Try again.");
                    // This attempt won't be counted against the user for an out-of-range guess,
                    // or you could choose to count it by moving attemptsMade++ before this check.
                    // For now, let's not count it as a "wasted" attempt for out-of-range.
                    continue; // Skip to the next iteration to ask for input again
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a whole number.");
                scanner.nextLine(); // Important to consume the invalid input
                continue; // Skip to the next iteration
            }

            attemptsMade++; // Increment attempts only after a valid in-range number is entered

            // 3. Feedback
            if (userGuess == secretNumber) {
                guessedCorrectly = true;
                System.out.println("Congratulations! You've guessed the number (" + secretNumber + ") correctly in " + attemptsMade + " attempts!");
            } else if (userGuess < secretNumber) {
                System.out.println("Your guess is too low.");
            } else {
                System.out.println("Your guess is too high.");
            }
        }

        // 4. Win/Lose Conditions
        if (!guessedCorrectly) {
            System.out.println("------------------------------------");
            System.out.println("Sorry, you've run out of attempts!");
            System.out.println("The secret number was: " + secretNumber);
        }
    }

}