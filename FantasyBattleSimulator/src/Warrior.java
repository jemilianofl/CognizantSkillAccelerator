public class Warrior extends GameCharacter {
    private boolean powerStrikeReady = true; // Simple cooldown/resource

    public Warrior(String name) {
        super(name, 150, 25, 15); // Name, HP, ATK, DEF
    }

    @Override
    public void useSpecialAbility(GameCharacter target, GameCharacter self)
            throws InvalidActionException, InvalidTargetException, AbilityNotReadyException {
        if (this.isDefeated()) {
            throw new InvalidActionException(this.name + " is defeated and cannot use Power Strike.");
        }
        if (!powerStrikeReady) {
            throw new AbilityNotReadyException("Power Strike is not ready yet!");
        }
        if (target == null) {
            throw new InvalidTargetException("Target for Power Strike cannot be null.");
        }
        if (target.isDefeated()) {
            throw new InvalidTargetException(target.getName() + " is already defeated. Power Strike cannot be used.");
        }
        // if (this == target) {
        //     throw new InvalidTargetException("Cannot target self with Power Strike.");
        // }


        System.out.println("\n" + this.name + " uses Power Strike on " + target.getName() + "!");
        int powerStrikeDamage = (int) (this.attackPower * 1.8); // 80% more damage
        target.takeDamage(powerStrikeDamage);
        this.powerStrikeReady = false; // Needs to recharge (simple example)
        this.isDefending = false;
    }

    @Override
    public void reset() {
        super.reset();
        this.powerStrikeReady = true;
    }

    // In a real game, you'd have a mechanism to recharge abilities
    public void rechargePowerStrike() {
        this.powerStrikeReady = true;
        System.out.println(this.name + "'s Power Strike has recharged!");
    }
}