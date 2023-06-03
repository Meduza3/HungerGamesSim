import javafx.scene.paint.Color;

public enum Item {
    FRUIT("Fruit", Color.GREEN),
    WATER("Water", Color.BLUE),
    SWORD("Sword", Color.RED),
    SHIELD("Shield", Color.GRAY),
    ARMOR("Armor", Color.DARKGRAY),
    MEDICINE("Medicine", Color.PINK);

    public final String name;
    public final Color color;

    Item(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
