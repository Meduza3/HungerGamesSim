public enum Item {
    FRUIT("Fruit"),
    WATER("Water"),
    SWORD("Sword"),
    SHIELD("Shield"),
    ARMOR("Armor");

    private String name;

    Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
