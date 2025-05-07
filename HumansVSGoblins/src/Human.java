import java.util.*;

public class Human extends Combatant {
    public static final String HUMAN_REPRESENTATION = "🧑"; // Human UTF character
    // Extras
    // private List<Item> inventory;
    // private Weapon equippedWeapon;
    // private Armor equippedArmor;

    public Human(int x, int y) {
        super(x, y, HUMAN_REPRESENTATION, 100, 10); // Default stats: 100 HP, 10 Strength
        // this.inventory = new ArrayList<>();
    }

    @Override
    public int attack(Combatant target) {
        Random random = new Random();
        // Simple damage calculation: strength +/- some variance
        int damageDealt = Math.max(1, this.strength + random.nextInt(5) - 2); // Damage between str-2 and str+2, min 1
        System.out.println(this.representation + " attacks " + target.getRepresentation() + " for " + damageDealt + " damage!");
        target.takeDamage(damageDealt);
        return damageDealt;
    }

    // Methods for extras can be added here:
    // public void pickUpItem(Item item) { inventory.add(item); }
    // public void equipWeapon(Weapon weapon) { this.equippedWeapon = weapon; }
    // public int getEffectiveStrength() { return strength + (equippedWeapon != null ? equippedWeapon.getDamageBonus() : 0); }
}
