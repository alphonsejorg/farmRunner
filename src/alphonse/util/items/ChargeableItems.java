package alphonse.util.items;

import alphonse.scripts.Script;
import alphonse.util.teleport.Teleport;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static alphonse.util.items.Bank.bank;

public class ChargeableItems {
    private final static Logger LOGGER = Logger.getLogger("ChargeableItems");
    public final static ChargeableItems chargeableItems = new ChargeableItems();

    private Map<String, Integer> items = new HashMap<>();

    public void addOwnedJewellery(ChargeableItem chargeableItem, int id, int amount) {
        amount *= chargeableItem.getChargesMap().get(id);
        if (items.containsKey(chargeableItem.name())) {
            amount += items.get(chargeableItem.name());
            items.replace(chargeableItem.name(), amount);

        } else {
            items.put(chargeableItem.name(), amount);
        }
    }

    public boolean haveItem(String chargeableName) {
        LOGGER.info("checking if have item: " + chargeableName);
        if (items.containsKey(chargeableName)) {
            if (items.get(chargeableName) <= 0) {
                items.remove(chargeableName);
                return false;
            }
            return true;
        }
        return false;
    }

    public Items toOwnedItems(Teleport teleport, int charges) {
        return toOwnedItems(teleport.getChargeableItem(), charges);
    }

    public Items toOwnedItems(ChargeableItem chargeableItem, int charges) {
        if (!bank.isCached()) {
            Script.stopScript("tried to get owned items of " + this + " without having cached bank items...");
        }
        if (getCharges(chargeableItem.toString()) < charges) {
            LOGGER.info("you only own " + getCharges(chargeableItem.toString()) + " out of the required " + charges + " charges...");
            return Items.NIL;
        }
        LOGGER.info("finding owned items for " + chargeableItem + " with at least " + charges + " charges");

        Items items = new Items("owned jewellery");
        Items potentialItems = chargeableItem.getItemsWithAtLeastXCharges(charges);
        potentialItems.setName("potential items for " + chargeableItem);
        potentialItems.print();
        for (Item item : potentialItems.getItems()) {
            Item bankItem = bank.getItems().getItem(item);
            if (bankItem.getId() != -1) {
                bankItem.setAmount(1);
                LOGGER.info("found item: " + bankItem.toString());
                items.addItem(bankItem);
                return items;
            }
        }
        LOGGER.info("failed to get single item for " + chargeableItem);

        int chargesFound = 0;
        for (int newCharges = charges - 1; newCharges > 0; newCharges--) {
            Item potentialItem = chargeableItem.getItemWithCharges(newCharges);
            Item bankItem = bank.getItems().getItem(potentialItem);

            if (bankItem.getId() != -1) {
                LOGGER.info("found item: " + bankItem.toString());
                int amount = 0;
                do {
                    amount++;
                } while ((amount * newCharges) >= charges);
                LOGGER.info("need " + amount + " of item with " + newCharges + " charges to reach required " + charges + " charges");
                bankItem.setAmount(Math.min(bankItem.getAmount(), (amount)));
                items.addItem(bankItem);
                chargesFound += bankItem.getAmount() * newCharges;
                LOGGER.info("adding " + bankItem.toString() + " to " + items.getName());
                LOGGER.info(chargesFound + " out of " + charges + " found");
            }
            if (chargesFound >= charges) {
                return items;
            }
        }
        LOGGER.info("failed to get owned items for " + chargeableItem);
        return Items.NIL;
    }

    public int getCharges(String chargeableName) {
        try {
            return items.get(chargeableName);
        } catch (NullPointerException e) {
            LOGGER.info("don't own any charges for item: " + chargeableName);
        }
        return -1;
    }

    public Map<String, Integer> getItems() {
        return items;
    }
}
