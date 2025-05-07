import java.util.*;

public class FantasyBattleSimulator {

    private static List<GameCharacter> availableCharacters = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in); // Main scanner for the application

    public static void main(String[] args) {
        initializeAvailableCharacters();

        try { // Main try for application-level errors or resource cleanup
            boolean playing = true;
            while (playing) {
                System.out.println("\nWelcome to the Fantasy Battle Simulator!");
                System.out.println("1. Start New Battle");
                System.out.println("2. View Available Characters");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                String choiceStr = scanner.nextLine();
                int choice;
                try {
                    choice = Integer.parseInt(choiceStr);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        runBattle();
                        break;
                    case 2:
                        viewAvailableCharacters();
                        break;
                    case 3:
                        playing = false;
                        System.out.println("Exiting simulator. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception e) { // Catch any truly unexpected top-level errors
            System.err.println("A critical error occurred in the simulator: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Resource management: Ensure scanner is closed
            System.out.println("Closing application resources...");
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void initializeAvailableCharacters() {
        availableCharacters.add(new Warrior("Aragorn"));
        availableCharacters.add(new Mage("Gandalf"));
        availableCharacters.add(new Archer("Legolas"));
        availableCharacters.add(new Warrior("Gimli"));
        availableCharacters.add(new Mage("Saruman"));
        availableCharacters.add(new Archer("Hawkeye")); // Just for variety
    }

    private static void viewAvailableCharacters() {
        System.out.println("\n--- Available Characters ---");
        if (availableCharacters.isEmpty()) {
            System.out.println("No characters available.");
            return;
        }
        for (int i = 0; i < availableCharacters.size(); i++) {
            System.out.println((i + 1) + ". " + availableCharacters.get(i).toString());
        }
    }

    private static void runBattle() {
        System.out.println("\n--- Setup New Battle ---");
        List<GameCharacter> team1 = new ArrayList<>();
        List<GameCharacter> team2 = new ArrayList<>();

        try {
            System.out.println("Select characters for Team 1:");
            team1 = selectTeam(2, "Team 1"); // Let's say 2 characters per team for simplicity
            if (team1.isEmpty()) return; // User cancelled or error

            System.out.println("\nSelect characters for Team 2:");
            team2 = selectTeam(2, "Team 2", team1); // Pass team1 to avoid picking same instance
            if (team2.isEmpty()) return; // User cancelled or error


            Battle battle = new Battle(scanner); // Pass the shared scanner
            battle.setupBattle(team1, team2);
            battle.start();

        } catch (GameSetupException | CharacterNotFoundException e) {
            System.err.println("Battle Setup Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.err.println("Invalid input during setup. Please enter numbers where expected.");
            scanner.nextLine(); // Clear buffer
        } catch (Exception e) { // Catch any other setup/start errors
            System.err.println("An unexpected error occurred while setting up or starting the battle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<GameCharacter> selectTeam(int teamSize, String teamName, List<GameCharacter>... opposingTeams)
            throws CharacterNotFoundException {
        List<GameCharacter> selectedTeam = new ArrayList<>();
        List<GameCharacter> currentAvailable = new ArrayList<>(availableCharacters);

        // Remove characters already picked by opposing teams from selection pool
        if (opposingTeams != null) {
            for (List<GameCharacter> Oteam : opposingTeams) {
                if (Oteam != null) currentAvailable.removeAll(Oteam);
            }
        }

        System.out.println("Choose " + teamSize + " character(s) for " + teamName + " from the list below:");

        while (selectedTeam.size() < teamSize) {
            if(currentAvailable.isEmpty() && selectedTeam.size() < teamSize){
                System.out.println("Not enough unique characters available to complete " + teamName);
                throw new CharacterNotFoundException("Cannot form " + teamName + " due to lack of available unique characters.");
            }

            for (int i = 0; i < currentAvailable.size(); i++) {
                System.out.println((i + 1) + ". " + currentAvailable.get(i).getName());
            }
            System.out.println("0. Done / Cancel Selection for " + teamName);
            System.out.print("Pick character #" + (selectedTeam.size() + 1) + " (enter number): ");

            String choiceStr = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 0) {
                if(selectedTeam.size() > 0 && selectedTeam.size() < teamSize) {
                    System.out.println("Team selection incomplete. Do you want to proceed with fewer characters for "+teamName+"? (yes/no)");
                    if (!scanner.nextLine().trim().equalsIgnoreCase("yes")){
                        System.out.println(teamName + " selection cancelled.");
                        return new ArrayList<>(); // Return empty if cancelled
                    }
                } else if (selectedTeam.isEmpty()) {
                    System.out.println(teamName + " selection cancelled.");
                    return new ArrayList<>(); // Return empty if cancelled
                }
                break; // Done selecting
            }

            if (choice > 0 && choice <= currentAvailable.size()) {
                GameCharacter pickedChar = currentAvailable.get(choice - 1);
                selectedTeam.add(pickedChar);
                currentAvailable.remove(pickedChar); // Remove from current selection pool to avoid picking same instance for this team
                System.out.println(pickedChar.getName() + " added to " + teamName + ".");
            } else {
                System.out.println("Invalid character number. Please try again.");
            }
        }
        if(selectedTeam.isEmpty() && teamSize > 0){
            throw new CharacterNotFoundException(teamName + " could not be formed.");
        }
        System.out.println(teamName + " selection complete.");
        return selectedTeam;
    }
}