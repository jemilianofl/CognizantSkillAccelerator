public class Land extends GameEntity {
    // Using a simple dot or a more descriptive UTF character for land
    public static final String LAND_REPRESENTATION = "🟫"; // Brown square for land
    // public static final String LAND_REPRESENTATION = "."; // Alternative simple char

    public Land(int x, int y) {
        super(x, y, LAND_REPRESENTATION);
    }
}