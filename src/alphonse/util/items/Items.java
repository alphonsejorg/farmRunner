package alphonse.util.items;

import alphonse.scripts.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static alphonse.util.items.Bank.bank;
import static alphonse.util.items.Inventory.inventory;

public class Items {
    private final static Logger LOGGER = Logger.getLogger("Items");

    private ArrayList<Item> items;
    private String name;

    public final static Items NIL = new Items("");

    public Items(String name) {
        this.name = name;
        items = new ArrayList<>();
    }

    public void addItems(Items items) {
        for (Item item : items.items) {
            addItem(item);
        }
    }

    public void addItem(Item item) {
        if (contains(item.getSharedId()) && item.getSharedId() != -1) {
            Item toRemove = null;
            for (Item i : items) {
                if (i.getSharedId() == item.getSharedId()) {
                    toRemove = i;
                    item.setAmount(item.getAmount() + i.getAmount());
                    break;
                }
            }
            items.remove(toRemove);
        }
        items.add(item);
    }

    public Item getItem(Item item) {
        if (item.isNoted()) {
            for (Item i : items) {
                if (i.getSharedId() == item.getSharedId() ||
                        i.getId() == item.getSharedId()) {
                    return i;
                } else if (i.getSharedId() == item.getId() ||
                        i.getId() == item.getId()) {
                    return i;
                }
            }
        } else {
            for (Item i : items) {
                if (i.getSharedId() == item.getSharedId() ||
                i.getId() == item.getSharedId()) {
                    return i;
                }
            }
        }
        return new Item(-1, -1);
    }

    public Item getItem(int itemId) {
        for (Item item : items) {
            if (item.getSharedId() == itemId ||
            item.getId() == itemId) {
                return item;
            }
        }
        return new Item(-1, -1);
    }

    public boolean nameContains(String... itemNames) {
        for (Item item : items) {
            for (String itemName : itemNames) {
                if (item.getName().toLowerCase().contains(itemName.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeItem(int itemId, int amount) {
        Item toRemove = null;
        for (Item item : items) {
            if (item.getSharedId() == itemId) {
                if (item.getAmount() <= amount) {
                    toRemove = item;
                } else {
                    item.setAmount(item.getAmount() - amount);
                }
                break;
            }
        }
        if (toRemove != null) {
            items.remove(toRemove);
        }
    }

    public void removeItem(int itemId) {
        removeItem(itemId, Integer.MAX_VALUE);
    }

    public boolean contains(Item item) {
        for (Item item1 : items) {
            if (item1.getId() == item.getId()) {
                return (item1.getAmount() >= item.getAmount());
            }
        }
        return false;
    }

    public boolean contains(String... itemNames) {
        for (Item item : items) {
            for (String itemName : itemNames) {
                if (item.getName().equals(itemName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean contains(int... itemIds) {
        for (Item item : items) {
            for (int itemId : itemIds) {
                if (item.getSharedId() == itemId) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> toStrings() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("ITEMS IN " + name.toUpperCase());
        for (Item item : items) {
            strings.add(item.toString());
        }
        return strings;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ITEMS IN ");
        sb.append(name.toUpperCase());
        for (Item item : items) {
            sb.append("\n");
            sb.append(item.toString());
        }
        return sb.toString();
    }

    public void print() {
        LOGGER.info("items in " + name + ":");
        for (Item item : items) {
            LOGGER.info(item.toString());
        }
    }

    public Items getMissingItems(Items items) {
        Items missingItems = new Items("missing items");
        for (Item item : getItems()) {
            Item i = items.getItem(item);
            if (item.isNoted() || i.isNoted()) {
                if (i.getSharedId() != item.getId() &&
                        i.getSharedId() != item.getSharedId()) {
                    missingItems.addItem(item);
                    continue;
                }
            } else {
                if (i.getSharedId() != item.getSharedId()) {
                    missingItems.addItem(item);
                    continue;
                }
            }

            if (i.getAmount() < item.getAmount()) {
                missingItems.addItem(new Item(item.getSharedId(), item.getName(), (item.getAmount() - i.getAmount())));
            }
        }
        return missingItems;
    }

    public boolean containsAll(Items items) {
        LOGGER.info("checking if " + name + " contains all items in " + items.name);
        if (!this.equals(bank.getItems())) {
            print();
        }
        items.print();
        if (this.getItems().isEmpty()) {
            if (items.getItems().isEmpty()) {
                LOGGER.info("both empty, returning true");
                return true;
            }
        }
        Items missingItems = items.getMissingItems(this);
        if (!missingItems.getItems().isEmpty()) {
            LOGGER.info("missing items found. " + name + " does not contain all items in " + items.name);
            missingItems.print();
            return false;
        }
        LOGGER.info("no missing items detected. " + name + " must contain all items in " + items.name);
        return true;
    }

    public boolean containsAll(int... ids) {
        for (int id : ids) {
            boolean foundMatch = false;
            for (Item item : items) {
                if (item.getSharedId() == id) {
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch) {
                return false;
            }
        }
        return true;
    }

    public boolean ownAll() {
        LOGGER.info("checking if owning all items " + name);
        if (!bank.isCached()) {
            Script.stopScript("can't check if own items " + name + ", because bank has not been cached");
            return false;
        }

        Items missingItems = getMissingItems(inventory.getItems());
        missingItems = missingItems.getMissingItems(bank.getItems());

        if (missingItems.getItems().isEmpty()) {
            LOGGER.info("all items found");
            return true;

        } else {
            missingItems.print();
            return false;
        }
    }

    public static boolean ownItem(int id) {
        LOGGER.info("checking if owning item: " + id);
        if (inventory.getItems().contains(id)) {
            LOGGER.info("item found in inventory");
            return true;
        }

        if (bank.getItems().contains(id)) {
            LOGGER.info("item found in bank");
            return true;
        }

        return false;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
