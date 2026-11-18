public class Yarn {
    private final String fiber;
    private final String color;
    private final int meters;

    public Yarn() {
        this("шерсть", "серый", 120);
    }

    public Yarn(String fiber, String color, int meters) {
        this.fiber = fiber;
        this.color = color;
        this.meters = meters;
    }

    public String fiber() {
        return fiber;
    }

    public String color() {
        return color;
    }

    public int meters() {
        return meters;
    }

    @Override
    public String toString() {
        return fiber + ", " + color + ", " + meters + " м";
    }
}
