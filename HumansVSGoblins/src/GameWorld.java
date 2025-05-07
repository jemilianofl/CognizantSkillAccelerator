import java.util.*;

public class GameWorld {
    private GameEntity[][] grid;
    private int width;
    private int height;
    private Human player;
    private List<Goblin> goblins;
    private Random random = new Random();

    public GameWorld(int width, int height, int numGoblins) {
        this.width = width;
        this.height = height;
        this.grid = new GameEntity[height][width]; // Note: often [rows][cols] -> [y][x]
        this.goblins = new ArrayList<>();
        initializeWorld(numGoblins);
    }

    private void initializeWorld(int numGoblins) {
        // Fill with land
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Land(x, y);
            }
        }

        // Place player (e.g., at a fixed or random starting position)
        int playerX = 0; // width / 2;
        int playerY = 0; // height / 2;
        this.player = new Human(playerX, playerY);
        grid[playerY][playerX] = this.player;

        // Place goblins randomly
        for (int i = 0; i < numGoblins; i++) {
            int goblinX, goblinY;
            do {
                goblinX = random.nextInt(width);
                goblinY = random.nextInt(height);
            } while (!(grid[goblinY][goblinX] instanceof Land)); // Ensure spot is empty (Land)

            Goblin goblin = new Goblin(goblinX, goblinY);
            this.goblins.add(goblin);
            grid[goblinY][goblinX] = goblin;
        }
    }

    public void displayWorld() {
        System.out.println("\n--- World Map ---");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(grid[y][x].getRepresentation() + " ");
            }
            System.out.println(); // Newline after each row
        }
        System.out.println("Player " + player.getRepresentation() + " " + player.getStats());
        for (Goblin goblin : goblins) {
            if (goblin.isAlive()) {
                System.out.println("Goblin " + goblin.getRepresentation() + " at (" + goblin.getX() + "," + goblin.getY() + ") " + goblin.getStats());
            }
        }
        System.out.println("-----------------");
    }

    public boolean isPositionValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public GameEntity getEntityAt(int x, int y) {
        if (isPositionValid(x, y)) {
            return grid[y][x];
        }
        return null; // Or throw exception
    }

    // Method to move the player and handle collisions/interactions
    public String movePlayer(char direction) {
        int newX = player.getX();
        int newY = player.getY();

        switch (Character.toLowerCase(direction)) {
            case 'n': newY--; break;
            case 's': newY++; break;
            case 'e': newX++; break;
            case 'w': newX--; break;
            default: return "Invalid direction!";
        }

        if (!isPositionValid(newX, newY)) {
            return "You can't move there (out of bounds).";
        }

        GameEntity targetEntity = grid[newY][newX];

        if (targetEntity instanceof Goblin) {
            Goblin goblin = (Goblin) targetEntity;
            if (goblin.isAlive()) {
                initiateCombat(player, goblin);
                if (!goblin.isAlive()) { // If goblin was defeated
                    grid[newY][newX] = new Land(newX, newY); // Clear goblin spot
                    goblins.remove(goblin);
                    // Player doesn't move into the spot if combat happened, or moves after if won.
                    // For now, let's assume player moves after winning
                    return "You defeated a goblin! You can now move into the spot if desired or continue.";
                } else if (!player.isAlive()) {
                    return "Game Over - You were defeated by a goblin!";
                }
                return "Combat ended. Goblin is still alive."; // Player doesn't move into goblin's spot
            } else { // Goblin is already defeated, treat as land
                // This case might not happen if defeated goblins are immediately replaced by Land
            }
        }
        // For now, let's simplify: if not combat, player moves if it's land
        if (targetEntity instanceof Land /* || (targetEntity instanceof Goblin && !((Goblin)targetEntity).isAlive()) */) {
            // Clear old player position
            grid[player.getY()][player.getX()] = new Land(player.getX(), player.getY());
            // Update player's internal coordinates
            player.setX(newX);
            player.setY(newY);
            // Place player in new position on grid
            grid[newY][newX] = player;
            return "You moved " + directionName(direction) + ".";
        }
        // If target is treasure, etc. (extras)
        // else if (targetEntity instanceof TreasureChest) { ... }

        return "You can't move there (blocked or unknown entity).";
    }

    // Basic combat logic
    public void initiateCombat(Combatant attacker, Combatant defender) {
        System.out.println("\n--- COMBAT INITIATED: " + attacker.getRepresentation() + " vs " + defender.getRepresentation() + " ---");
        // Simple turn-based: attacker strikes first, then defender if still alive
        if (attacker.isAlive()) {
            attacker.attack(defender);
        }
        if (defender.isAlive()) { // Defender retaliates
            defender.attack(attacker);
        }

        if (!attacker.isAlive()) System.out.println(attacker.getRepresentation() + " has been defeated!");
        if (!defender.isAlive()) System.out.println(defender.getRepresentation() + " has been defeated!");
        System.out.println("--- COMBAT ENDED ---");
    }

    public Human getPlayer() {
        return player;
    }

    public List<Goblin> getGoblins() {
        return new ArrayList<>(goblins); // Return a copy
    }

    // Helper for direction name
    private String directionName(char direction) {
        switch (Character.toLowerCase(direction)) {
            case 'n': return "North";
            case 's': return "South";
            case 'e': return "East";
            case 'w': return "West";
            default: return "Unknown direction";
        }
    }

    // Goblin movement (simple random for now, non-colliding with player directly)
    public void moveGoblins() {
        for (Goblin goblin : this.goblins) {
            if (!goblin.isAlive()) continue;

            int attempts = 0;
            int maxAttempts = 4; // Try a few times to find a valid move

            while(attempts < maxAttempts) {
                int moveChoice = random.nextInt(4); // 0:N, 1:S, 2:E, 3:W
                int newX = goblin.getX();
                int newY = goblin.getY();

                if (moveChoice == 0) newY--; // North
                else if (moveChoice == 1) newY++; // South
                else if (moveChoice == 2) newX++; // East
                else newX--; // West

                if (isPositionValid(newX, newY)) {
                    GameEntity targetSpot = grid[newY][newX];
                    if (targetSpot instanceof Land) {
                        grid[goblin.getY()][goblin.getX()] = new Land(goblin.getX(), goblin.getY());
                        goblin.setX(newX);
                        goblin.setY(newY);
                        grid[newY][newX] = goblin;
                        break; // Goblin moved
                    } else if (targetSpot instanceof Human) {
                        // Goblin initiates combat if it moves into human's square
                        initiateCombat(goblin, (Human)targetSpot);
                        break; // Combat initiated, goblin's move ends
                    }
                }
                attempts++;
            }
        }
    }
}