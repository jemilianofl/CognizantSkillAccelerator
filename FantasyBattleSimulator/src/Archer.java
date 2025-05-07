public class Archer extends GameCharacter {
    private boolean piercingShotReady = true;

    public Archer(String name) {
        super(name, 110, 22, 10); // Balanced stats
    }

    @Override
    public void useSpecialAbility(GameCharacter target, GameCharacter self)
            throws InvalidActionException, InvalidTargetException, AbilityNotReadyException {
        if (this.isDefeated()) {
            throw new InvalidActionException(this.name + " is defeated and cannot use Piercing Shot.");
        }
        if (!piercingShotReady) {
            throw new AbilityNotReadyException("Piercing Shot is not ready yet!");
        }
        if (target == null) {
            throw new InvalidTargetException("Target for Piercing Shot cannot be null.");
        }
        if (target.isDefeated()) {
            throw new InvalidTargetException(target.getName() + " is already defeated. Piercing Shot cannot be used.");
        }

        System.out.println("\n" + this.name + " uses Piercing Shot on " + target.getName() + "!");
        // Piercing shot ignores a portion of defense or deals true damage
        int piercingDamage = this.attackPower + 5; // Base damage slightly higher
        System.out.println(target.getName() + "'s defense is partially ignored!");

        // Simulate ignoring defense by directly reducing health or applying damage with minimal defense calculation
        int originalTargetHealth = target.healthPoints;
        target.healthPoints -= piercingDamage; // Directly reduce health
        System.out.println(target.name + " takes " + piercingDamage + " piercing damage. Current HP: " + Math.max(0, target.healthPoints));
        if (target.isDefeated() && originalTargetHealth > 0) { // Check if this attack defeated them
            System.out.println(target.name + " has been defeated by the Piercing Shot!");
        }

        this.piercingShotReady = false;
        this.isDefending = false;
    }

    @Override
    public void reset() {
        super.reset();
        this.piercingShotReady = true;
    }

    // In a real game, you'd have a mechanism to recharge abilities
    public void rechargePiercingShot() {
        this.piercingShotReady = true;
        System.out.println(this.name + "'s Piercing Shot has recharged!");
    }
}