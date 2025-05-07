public abstract class GameEntity {
    protected int x;
    protected int y;
    protected String representation; // UTF character or string for display

    public GameEntity(int x, int y, String representation) {
        this.x = x;
        this.y = y;
        this.representation = representation;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getRepresentation() {
        return representation;
    }

    // Setters (if needed for movement)
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Override toString to return the visual representation
    @Override
    public String toString() {
        return representation;
    }
}