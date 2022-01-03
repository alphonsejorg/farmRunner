package alphonse.util.items;

import alphonse.util.constants.C_Items;

import static alphonse.util.items.ChargeableItems.chargeableItems;

public class Inventory {
    public final static Inventory inventory = new Inventory();

    private final Items items = new Items("cached inventory items");

    public void cacheItems() {
        for (org.powbot.api.rt4.Item item : org.powbot.api.rt4.Inventory.stream().list()) {
            if (item.id() == -1 || item.id() == C_Items.EMPTY_SLOT) {
                continue;
            }
            items.addItem(new Item(item.id(), item.name(), item.stackSize()));
            for (ChargeableItem chargeableItem : ChargeableItem.values()) {
                if (chargeableItem.getChargesMap().containsKey(item.id())) {
                    chargeableItems.addOwnedJewellery(chargeableItem, item.id(), item.stackSize());
                }
            }
        }
    }

    public Items getItems() {
        return items;
    }
}
