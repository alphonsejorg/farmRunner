package alphonse.util.farming;

public enum Patch {
    HERB("Herb patch"),
    FLOWER("Flower Patch"),
    ALLOTMENT("Allotment"),
    HOPS("Hops Patch"),
    FRUIT_TREE("Fruit Tree Patch"),
    TREE("Tree patch"),
    CACTUS("Cactus patch"),
    NIL("nil");

    private final String clearedName;

    Patch(String clearedName) {
        this.clearedName = clearedName;
    }

    public String getClearedName() {
        return clearedName;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase().replaceAll("_", " ");
    }
}