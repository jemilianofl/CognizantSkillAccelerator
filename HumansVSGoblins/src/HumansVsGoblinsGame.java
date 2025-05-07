import java.util.*;

public class HumansVsGoblinsGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Initialize game world (e.g., 10x10 grid with 3 goblins)
        GameWorld world = new GameWorld(10, 5, 3);
        boolean gameOver = false;

        System.out.println("Humans VS Goblins - The Game Begins!");
        System.out.println("You are " + world.getPlayer().getRepresentation() + ". Goblins are " + Goblin.GOBLIN_REPRESENTATION + ".");
        System.out.println("Move using N, S, E, W keys, then press Enter.");

        while (!gameOver) {
            world.displayWorld();

            // Player Turn
            System.out.print("Enter your move (N/S/E/W): ");
            if (!scanner.hasNextLine()){ // Handle potential no input scenario
                System.out.println("Exiting due to no input.");
                break;
            }
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Quitting game...");
                gameOver = true;
                continue;
            }

            if (input.length() > 0) {
                char moveDirection = input.charAt(0);
                String moveResult = world.movePlayer(moveDirection);
                System.out.println(moveResult);

                if (moveResult.startsWith("Game Over") || !world.getPlayer().isAlive()) {
                    System.out.println("You have been defeated. GAME OVER.");
                    gameOver = true;
                    continue;
                }
            } else {
                System.out.println("No input detected. Try N, S, E, or W.");
                continue;
            }


            // Check win condition (all goblins defeated)
            boolean allGoblinsDefeated = true;
            for (Goblin goblin : world.getGoblins()) {
                if (goblin.isAlive()) {
                    allGoblinsDefeated = false;
                    break;
                }
            }
            if (allGoblinsDefeated) {
                world.displayWorld(); // Show final state
                System.out.println("Congratulations! You have defeated all the goblins! YOU WIN!");
                gameOver = true;
                continue;
            }

            // Goblins' Turn (simple random movement for now)
            if (!gameOver) { // Only if player is still alive and game not won
                System.out.println("\nGoblins are moving...");
                world.moveGoblins(); // Goblins move and can initiate combat
                if (!world.getPlayer().isAlive()) {
                    world.displayWorld();
                    System.out.println("You were defeated by a goblin during their turn. GAME OVER.");
                    gameOver = true;
                    continue;
                }
            }
        }
        world.displayWorld(); // Final display
        scanner.close();
    }
}