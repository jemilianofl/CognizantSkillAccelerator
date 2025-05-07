import java.util.*;

public class Goblin extends Combatant {
    public static final String GOBLIN_REPRESENTATION = "👺"; // Goblin UTF character

    public Goblin(int x, int y) {
        super(x, y, GOBLIN_REPRESENTATION, 30, 6); // Goblins are weaker: 30 HP, 6 Strength
    }

    @Override
    public int attack(Combatant target) {
        Random random = new Random();
        int damageDealt = Math.max(1, this.strength + random.nextInt(3) - 1); // Damage between str-1 and str+1, min 1
        System.out.println(this.representation + " attacks " + target.getRepresentation() + " for " + damageDealt + " damage!");
        target.takeDamage(damageDealt);
        return damageDealt;
    }

    // Method for extras:
    // public Item dropItem() { /* logic to decide what item drops */ return null; }
}
