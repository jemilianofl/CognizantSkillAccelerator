import java.util.*;

public class DragonCave {

    public static void main(String[] args) {
        // Introduction to the game
        System.out.println("You are in a land full of dragons. In front of you,");
        System.out.println("you see two caves. In one cave, the dragon is friendly");
        System.out.println("and will share his treasure with you. The other dragon");
        System.out.println("is greedy and hungry and will eat you on sight.");
        System.out.println(); // Add a blank line for better readability

        // Get user input
        Scanner scanner = new Scanner(System.in);
        int chosenCave = 0;

        // Loop until a valid input (1 or 2) is given
        while (chosenCave != 1 && chosenCave != 2) {
            System.out.print("Which cave will you go into? (1 or 2): ");
            if (scanner.hasNextInt()) {
                chosenCave = scanner.nextInt();
                if (chosenCave != 1 && chosenCave != 2) {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                scanner.next(); // Consume the invalid input
            }
        }

        // Determine the outcome
        System.out.println("\nYou approach the cave...");
        System.out.println("It is dark and spooky...");

        // Randomly decide which cave has the friendly dragon
        Random random = new Random();
        int friendlyCave = random.nextInt(2) + 1; // Generates 1 or 2

        // Check the outcome based on the player's choice
        if (chosenCave == friendlyCave) {
            System.out.println("A large dragon jumps out in front of you!");
            System.out.println("He opens his jaws and...");
            System.out.println("Reveals a mountain of treasure! He shares some with you.");
            System.out.println("You made it out alive and rich!");
        } else {
            System.out.println("A large dragon jumps out in front of you!");
            System.out.println("He opens his jaws and...");
            System.out.println("Gobbles you down in one bite!");
            System.out.println("Game Over!");
        }

        scanner.close(); // Close the scanner
    }

}