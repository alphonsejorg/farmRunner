package alphonse.util.items;

public class Item {
    private int id;
    private String name;
    private int amount;
    private int sharedId;

    private boolean noted;

    public Item(int id, String name, int amount, boolean noted) {
        this.sharedId = id;
        this.id = (noted ? id + 1 : id);
        this.name = name;
        this.amount = amount;
        this.noted = noted;
    }

    public Item(int id) {
        this(id, "", (id == -1 ? -1 : 1), false);
    }

    public Item(int id, String name, int amount) {
        this(id, name, amount, false);
    }

    public Item(int id, int amount) {
        this(id, "", amount, false);
    }

    public Item(int id, String name) {
        this(id, name, 1, false);
    }

    public int getAmount() {
        return amount;
    }

    public int getSharedId() {
        return sharedId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isNoted() {
        return noted;
    }

    public Item toNoted() {
        Item item = new Item(id, name, amount, true);
        item.noted = true;
        return item;
    }

    @Override
    public String toString() {
        return id +
                " '" + name + '\'' +
                " [" + amount +
                ']' +
                (noted ? "noted" : "");
    }
}
