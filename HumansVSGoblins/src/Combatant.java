public abstract class Combatant extends GameEntity {
    protected int health;
    protected int maxHealth;
    protected int strength; // Base attack power
    // Add other stats like defense, agility later if needed for extras

    public Combatant(int x, int y, String representation, int maxHealth, int strength) {
        super(x, y, representation);
        this.maxHealth = maxHealth;
        this.health = maxHealth; // Start with full health
        this.strength = strength;
    }

    // Getters
    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getStrength() {
        return strength;
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Combat methods (to be implemented/overridden)
    public abstract int attack(Combatant target); // Returns damage dealt

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        System.out.println(this.representation + " takes " + damage + " damage! Current health: " + this.health + "/" + this.maxHealth);
    }

    // For displaying stats
    public String getStats() {
        return "HP: " + health + "/" + maxHealth + " | Str: " + strength;
    }
}