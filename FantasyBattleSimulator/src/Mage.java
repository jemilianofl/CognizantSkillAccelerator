public class Mage extends GameCharacter {
    private int mana;
    private final int maxMana = 100;
    private final int fireballCost = 30;
    private final int healCost = 40;

    public Mage(String name) {
        super(name, 90, 30, 8); // Mages have high attack but low defense/HP
        this.mana = maxMana;
    }

    private void regenerateMana() {
        this.mana = Math.min(maxMana, this.mana + 15); // Regenerate some mana each turn or action
    }

    @Override
    public void attack(GameCharacter target) throws InvalidActionException, InvalidTargetException {
        super.attack(target); // Standard attack
        regenerateMana();
    }

    @Override
    public void defend() throws InvalidActionException {
        super.defend();
        regenerateMana();
    }

    @Override
    public void useSpecialAbility(GameCharacter target, GameCharacter self) // `target` can be enemy or ally for Mage
            throws InvalidActionException, InvalidTargetException, AbilityNotReadyException {
        if (this.isDefeated()) {
            throw new InvalidActionException(this.name + " is defeated and cannot cast spells.");
        }

        // For this example, let's say Mage has two abilities: Fireball (damage) and Heal (heal self or target)
        // In a real game, you'd let the player choose which ability. Here, we'll make a simple choice.
        // Or, pass an ability name/type parameter. For now, let's assume 'target' helps decide.
        // If target is the 'self' reference, it's a heal. Otherwise, it's a fireball.

        if (target == self) { // Heal self
            if (mana < healCost) {
                throw new AbilityNotReadyException("Not enough mana to cast Heal! Needs " + healCost + ", has " + mana);
            }
            System.out.println("\n" + this.name + " casts Heal on themself!");
            int healAmount = 50 + random.nextInt(26); // Heal for 50-75 HP
            this.healthPoints = Math.min(this.maxHealthPoints, this.healthPoints + healAmount);
            this.mana -= healCost;
            System.out.println(this.name + " heals for " + healAmount + " HP. Current HP: " + this.healthPoints + ". Mana: " + mana);
        } else { // Fireball on target
            if (target == null) {
                throw new InvalidTargetException("Target for Fireball cannot be null.");
            }
            if (target.isDefeated()) {
                throw new InvalidTargetException(target.getName() + " is already defeated. Fireball cannot be used.");
            }
            if (mana < fireballCost) {
                throw new AbilityNotReadyException("Not enough mana to cast Fireball! Needs " + fireballCost + ", has " + mana);
            }
            System.out.println("\n" + this.name + " casts Fireball on " + target.getName() + "!");
            int fireballDamage = 35 + random.nextInt(16); // 35-50 damage, less reliant on ATK stat
            target.takeDamage(fireballDamage); // Fireball might ignore some defense or have its own logic
            this.mana -= fireballCost;
            System.out.println(this.name + " mana: " + mana);
        }
        this.isDefending = false;
        regenerateMana(); // Regenerate a bit after casting too
    }

    @Override
    public void displayStatus() {
        super.displayStatus();
        System.out.printf(" -> Mana: %d/%d%n", mana, maxMana);
    }

    @Override
    public void reset() {
        super.reset();
        this.mana = maxMana;
    }
}