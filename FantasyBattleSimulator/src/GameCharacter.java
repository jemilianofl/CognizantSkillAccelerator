import java.util.*;

public abstract class GameCharacter {
    protected String name;
    protected int healthPoints;
    protected int maxHealthPoints;
    protected int attackPower;
    protected int defensePower;
    protected boolean isDefending; // For a simple defend action
    protected Random random = new Random(); // For some variability

    public GameCharacter(String name, int maxHealthPoints, int attackPower, int defensePower) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Character name cannot be null or empty.");
        }
        if (maxHealthPoints <= 0 || attackPower < 0 || defensePower < 0) {
            throw new IllegalArgumentException("Health, attack, and defense must be positive values (or zero for attack/defense).");
        }
        this.name = name;
        this.maxHealthPoints = maxHealthPoints;
        this.healthPoints = maxHealthPoints; // Start with full health
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.isDefending = false;
    }

    public String getName() {
        return name;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public boolean isDefeated() {
        return this.healthPoints <= 0;
    }

    public void takeDamage(int damage) {
        if (damage < 0) damage = 0; // Cannot heal through damage method

        int actualDamage = damage;
        if (isDefending) {
            actualDamage = Math.max(0, damage - (defensePower * 2)); // Defending doubles defense effectiveness
            System.out.println(name + " is defending! Damage reduced.");
            isDefending = false; // Defend state usually lasts one turn or until next action
        } else {
            actualDamage = Math.max(0, damage - defensePower);
        }

        this.healthPoints -= actualDamage;
        System.out.println(name + " takes " + actualDamage + " damage. Current HP: " + Math.max(0, this.healthPoints));
        if (isDefeated()) {
            System.out.println(name + " has been defeated!");
        }
    }

    public void attack(GameCharacter target) throws InvalidActionException, InvalidTargetException {
        if (this.isDefeated()) {
            throw new InvalidActionException(this.name + " is defeated and cannot attack.");
        }
        if (target == null) {
            throw new InvalidTargetException("Target cannot be null.");
        }
        if (target.isDefeated()) {
            throw new InvalidTargetException(target.getName() + " is already defeated. Choose another target.");
        }
        // Optional: Check if target is self or teammate if friendly fire is off
        // if (this == target) {
        //     throw new InvalidTargetException("Cannot target self with a basic attack.");
        // }

        System.out.println("\n" + this.name + " attacks " + target.getName() + "!");
        // Add a little variability to attack power, e.g., +/- 20%
        int damageVariance = (int) (this.attackPower * (random.nextDouble() * 0.4 - 0.2)); // -20% to +20%
        int calculatedDamage = Math.max(1, this.attackPower + damageVariance); // Ensure at least 1 damage before defense

        target.takeDamage(calculatedDamage);
        this.isDefending = false; // Attacking usually cancels defending state
    }

    public void defend() throws InvalidActionException {
        if (this.isDefeated()) {
            throw new InvalidActionException(this.name + " is defeated and cannot defend.");
        }
        System.out.println("\n" + this.name + " is defending!");
        this.isDefending = true;
    }

    public void displayStatus() {
        System.out.printf("%s - HP: %d/%d, ATK: %d, DEF: %d %s%n",
                name, healthPoints, maxHealthPoints, attackPower, defensePower,
                (isDefeated() ? "[DEFEATED]" : (isDefending ? "[DEFENDING]" : "")));
    }

    public void reset() {
        this.healthPoints = this.maxHealthPoints;
        this.isDefending = false;
        // Reset other temporary states if any
    }

    // Abstract method for special abilities, to be implemented by subclasses
    public abstract void useSpecialAbility(GameCharacter target, GameCharacter self)
            throws InvalidActionException, InvalidTargetException, AbilityNotReadyException;

    // For character selection (can be more sophisticated later)
    @Override
    public String toString() {
        return String.format("%s (HP: %d/%d, ATK: %d, DEF: %d)", name, healthPoints, maxHealthPoints, attackPower, defensePower);
    }
}