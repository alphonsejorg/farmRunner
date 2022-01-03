package alphonse.util.items;

import alphonse.scripts.Script;
import alphonse.util.constants.C_Items;

import java.util.Map;
import java.util.logging.Logger;

import static alphonse.util.items.ChargeableItems.chargeableItems;

public class Bank {
    private final static Logger LOGGER = Logger.getLogger("Bank");

    public final static Bank bank = new Bank();

    private final Items items = new Items("cached bank items");

    private boolean cached = false;

    public void cacheItems() {
        if (cached) {
            return;
        }
        if (!org.powbot.api.rt4.Bank.opened()) {
            Script.stopScript("trying to cache owned items without having bank open");
            return;
        }
        LOGGER.info("caching bank items...");
        long startTime = System.currentTimeMillis();

        for (org.powbot.api.rt4.Item item : org.powbot.api.rt4.Bank.stream().list()) {
            if (item.id() == C_Items.EMPTY_SLOT) {
                continue;
            }
            items.addItem(new Item(item.id(), item.name(), item.stackSize()));
            for (ChargeableItem chargeableItem : ChargeableItem.values()) {
                if (chargeableItem.getChargesMap().containsKey(item.id())) {
                    chargeableItems.addOwnedJewellery(chargeableItem, item.id(), item.stackSize());
                }
            }
        }

        LOGGER.info("* MAP OF ITEMS AND CHARGES *");
        for (Map.Entry<String, Integer> set : chargeableItems.getItems().entrySet()) {
            LOGGER.info(set.toString());
        }

        LOGGER.info("time spent caching items: " + (System.currentTimeMillis() - startTime) + " ms");
        cached = true;
    }

    public boolean isCached() {
        return cached;
    }

    public Items getItems() {
        return items;
    }
}
