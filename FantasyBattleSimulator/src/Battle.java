import java.util.*;
import java.util.stream.*;

public class Battle {
    private List<GameCharacter> team1;
    private List<GameCharacter> team2;
    private List<GameCharacter> turnOrder;
    private int currentTurnIndex;
    private int roundNumber;
    private Scanner scanner; // For user input during battle

    public Battle(Scanner scanner) {
        this.team1 = new ArrayList<>();
        this.team2 = new ArrayList<>();
        this.turnOrder = new ArrayList<>();
        this.currentTurnIndex = 0;
        this.roundNumber = 1;
        this.scanner = scanner;
    }

    public void setupBattle(List<GameCharacter> initialTeam1, List<GameCharacter> initialTeam2) throws GameSetupException {
        if (initialTeam1 == null || initialTeam1.isEmpty() || initialTeam2 == null || initialTeam2.isEmpty()) {
            throw new GameSetupException("Both teams must have at least one character.");
        }

        this.team1.clear();
        this.team2.clear();
        initialTeam1.forEach(c -> { c.reset(); this.team1.add(c); });
        initialTeam2.forEach(c -> { c.reset(); this.team2.add(c); });


        // Simple turn order: interleave characters from both teams
        // A more complex system might use a speed/initiative stat
        turnOrder.clear();
        int i = 0, j = 0;
        while (i < team1.size() || j < team2.size()) {
            if (i < team1.size()) {
                turnOrder.add(team1.get(i++));
            }
            if (j < team2.size()) {
                turnOrder.add(team2.get(j++));
            }
        }
        currentTurnIndex = 0;
        roundNumber = 1;
        System.out.println("Battle setup complete! Turn order determined.");
    }

    public void start() throws GameSetupException {
        if (turnOrder.isEmpty()) {
            throw new GameSetupException("Battle cannot start without characters in turn order. Run setupBattle first.");
        }

        System.out.println("\n========= BATTLE START! =========");

        while (!isBattleOver()) {
            if (currentTurnIndex == 0) {
                System.out.printf("\n--- Round %d ---%n", roundNumber);
            }
            GameCharacter currentCharacter = turnOrder.get(currentTurnIndex);

            // Skip turn if character is defeated
            if (currentCharacter.isDefeated()) {
                nextTurn();
                continue;
            }

            displayBattleStatus();
            System.out.println("\nIt's " + currentCharacter.getName() + "'s turn.");
            // Simple AI for now, or placeholder for player input
            try {
                if (isPlayerControlled(currentCharacter)) { // Assume team1 is player for now
                    playerAction(currentCharacter);
                } else {
                    aiAction(currentCharacter);
                }
            } catch (InvalidActionException | InvalidTargetException | AbilityNotReadyException e) {
                System.err.println("Action Error for " + currentCharacter.getName() + ": " + e.getMessage());
                // Optionally, the character might lose their turn or be prompted again
            } catch (Exception e) { // Catch any other unexpected runtime errors
                System.err.println("An unexpected error occurred during " + currentCharacter.getName() + "'s turn: " + e.getMessage());
                e.printStackTrace(); // Log or display stack trace for debugging
                // Decide if the game can continue or if this is a critical error
            }


            // Example: Recharge abilities for some characters (could be turn-based)
            if (currentCharacter instanceof Warrior) {
                // ((Warrior) currentCharacter).rechargePowerStrike(); // Maybe not every turn
            } else if (currentCharacter instanceof Archer) {
                // ((Archer) currentCharacter).rechargePiercingShot();
            }


            if (isBattleOver()) {
                break;
            }
            nextTurn();
        }
        announceVictor();
    }

    private boolean isPlayerControlled(GameCharacter character) {
        // For this example, let's assume characters in team1 are player-controlled
        return team1.contains(character);
    }

    private void playerAction(GameCharacter playerCharacter)
            throws InvalidActionException, InvalidTargetException, AbilityNotReadyException, CharacterNotFoundException {
        boolean actionTaken = false;
        while(!actionTaken) {
            System.out.println("Choose action for " + playerCharacter.getName() + ":");
            System.out.println("1. Attack");
            System.out.println("2. Defend");
            System.out.println("3. Special Ability");
            System.out.print("Enter choice (1-3): ");

            String choiceStr = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            List<GameCharacter> potentialTargets;
            GameCharacter target = null;

            switch (choice) {
                case 1: // Attack
                    potentialTargets = getOpposingTeam(playerCharacter).stream().filter(c -> !c.isDefeated()).collect(Collectors.toList());
                    if (potentialTargets.isEmpty()) {
                        System.out.println("No valid targets to attack!"); return;
                    }
                    target = selectTarget(playerCharacter, potentialTargets, "attack");
                    if (target != null) playerCharacter.attack(target); else continue; // re-prompt if target selection failed
                    actionTaken = true;
                    break;
                case 2: // Defend
                    playerCharacter.defend();
                    actionTaken = true;
                    break;
                case 3: // Special Ability
                    // Mage can target self for heal, others target opponents or specified.
                    if (playerCharacter instanceof Mage) {
                        System.out.println("Use special on (1) Enemy or (2) Self (for Heal)?");
                        System.out.print("Choice: ");
                        String mageTargetChoiceStr = scanner.nextLine();
                        if ("2".equals(mageTargetChoiceStr)) {
                            target = playerCharacter; // Target self for Mage's heal
                        } else {
                            potentialTargets = getOpposingTeam(playerCharacter).stream().filter(c -> !c.isDefeated()).collect(Collectors.toList());
                            if (potentialTargets.isEmpty()) {
                                System.out.println("No valid enemy targets for special ability!"); continue;
                            }
                            target = selectTarget(playerCharacter, potentialTargets, "special ability");
                        }
                    } else { // Warrior, Archer - target an enemy
                        potentialTargets = getOpposingTeam(playerCharacter).stream().filter(c -> !c.isDefeated()).collect(Collectors.toList());
                        if (potentialTargets.isEmpty()) {
                            System.out.println("No valid enemy targets for special ability!"); continue;
                        }
                        target = selectTarget(playerCharacter, potentialTargets, "special ability");
                    }
                    if (target != null) playerCharacter.useSpecialAbility(target, playerCharacter); else continue;
                    actionTaken = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private GameCharacter selectTarget(GameCharacter actor, List<GameCharacter> potentialTargets, String actionName) throws CharacterNotFoundException {
        if (potentialTargets.isEmpty()) {
            System.out.println("No targets available for " + actor.getName() + " to " + actionName + ".");
            return null;
        }
        System.out.println("Select target for " + actionName + ":");
        for (int i = 0; i < potentialTargets.size(); i++) {
            System.out.println((i + 1) + ". " + potentialTargets.get(i).getName() +
                    " (HP: " + potentialTargets.get(i).getHealthPoints() + ")");
        }
        while (true) {
            System.out.print("Enter target number: ");
            String targetChoiceStr = scanner.nextLine();
            try {
                int targetChoice = Integer.parseInt(targetChoiceStr);
                if (targetChoice >= 1 && targetChoice <= potentialTargets.size()) {
                    return potentialTargets.get(targetChoice - 1);
                } else {
                    System.out.println("Invalid target number. Please choose from the list.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number for the target.");
            }
            // Option to cancel target selection
            System.out.println("Or type 'cancel' to re-select action.");
            if ("cancel".equalsIgnoreCase(targetChoiceStr)){
                return null; // Signal to re-prompt action
            }
        }
    }


    private void aiAction(GameCharacter aiCharacter)
            throws InvalidActionException, InvalidTargetException, AbilityNotReadyException {
        // Simple AI: Attack a random, non-defeated player character from the opposing team
        List<GameCharacter> opposingTeam = getOpposingTeam(aiCharacter);
        List<GameCharacter> validTargets = opposingTeam.stream()
                .filter(c -> !c.isDefeated())
                .collect(Collectors.toList());

        if (validTargets.isEmpty()) {
            // This shouldn't happen if battle isn't over, but good to check
            return;
        }

        GameCharacter target = validTargets.get(new Random().nextInt(validTargets.size()));

        // Simple AI decision: 70% chance to attack, 30% chance to use special if possible, or defend
        double actionRoll = Math.random();
        try {
            if (actionRoll < 0.6 && canUseSpecial(aiCharacter)) { // Try special ability
                aiCharacter.useSpecialAbility(target, aiCharacter);
            } else if (actionRoll < 0.9) { // Attack
                aiCharacter.attack(target);
            } else { // Defend
                aiCharacter.defend();
            }
        } catch (AbilityNotReadyException e){
            // If special wasn't ready, default to attack
            System.out.println(aiCharacter.getName() + " tried special ability but it wasn't ready, defaults to attack!");
            aiCharacter.attack(target);
        }
    }

    private boolean canUseSpecial(GameCharacter character) {
        // Basic check, can be more sophisticated (e.g. check mana for Mage)
        if (character instanceof Warrior) return true; // Assume warrior special is always "try-able"
        if (character instanceof Archer) return true; // Assume archer special is always "try-able"
        if (character instanceof Mage) return true; // Mage has mana check inside useSpecialAbility
        return false;
    }


    private List<GameCharacter> getOpposingTeam(GameCharacter character) {
        return team1.contains(character) ? team2 : team1;
    }

    private void nextTurn() {
        currentTurnIndex = (currentTurnIndex + 1) % turnOrder.size();
        if (currentTurnIndex == 0) {
            roundNumber++; // New round if we've cycled through everyone
            // recharge some abilities at round start
            for(GameCharacter c : turnOrder) {
                if (c instanceof Warrior) ((Warrior)c).rechargePowerStrike();
                if (c instanceof Archer) ((Archer)c).rechargePiercingShot();
            }
        }
    }

    private boolean isBattleOver() {
        boolean team1Defeated = team1.stream().allMatch(GameCharacter::isDefeated);
        boolean team2Defeated = team2.stream().allMatch(GameCharacter::isDefeated);
        return team1Defeated || team2Defeated;
    }

    private void announceVictor() {
        System.out.println("\n========= BATTLE OVER! =========");
        boolean team1Defeated = team1.stream().allMatch(GameCharacter::isDefeated);
        boolean team2Defeated = team2.stream().allMatch(GameCharacter::isDefeated);

        if (team1Defeated && team2Defeated) {
            System.out.println("The battle is a DRAW! All characters are defeated.");
        } else if (team1Defeated) {
            System.out.println("Team 2 is VICTORIOUS!");
        } else if (team2Defeated) {
            System.out.println("Team 1 is VICTORIOUS!");
        }
        displayBattleStatus(); // Final status
    }

    public void displayBattleStatus() {
        System.out.println("\n--- Battle Status ---");
        System.out.println("Team 1:");
        team1.forEach(GameCharacter::displayStatus);
        System.out.println("Team 2:");
        team2.forEach(GameCharacter::displayStatus);
        System.out.println("---------------------");
    }
}