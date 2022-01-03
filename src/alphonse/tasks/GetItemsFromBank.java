package alphonse.tasks;

import alphonse.util.items.Item;
import alphonse.util.items.Items;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;
import org.powbot.mobile.script.ScriptManager;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.items.Inventory.inventory;

public class GetItemsFromBank extends Task {

    private Items items;

    private Activatable activatable;

    public GetItemsFromBank(Items items) {
        this(items, null);
    }

    public GetItemsFromBank(Items items, Activatable activatable) {
        this.items = items;
        this.activatable = activatable;
    }

    @Override
    public void execute() {
        LOGGER.info("getting items from bank...");
        if (Bank.nearest().tile().distanceTo(Players.local()) > 15) {
            if (!Movement.walkTo(Bank.nearest())) {
                return;
            }
        }

        if (Bank.open()) {
            Bank.depositInventory();
            Items notedItems = new Items("noted items");
            Bank.withdrawModeNoted(false);
            for (Item item : items.getItems()) {
                if (item.isNoted()) {
                    notedItems.addItem(item);
                    continue;
                }
                Bank.withdraw(item.getSharedId(), item.getAmount());
            }
            Bank.withdrawModeNoted(true);
            for (Item item : notedItems.getItems()) {
                Bank.withdraw(item.getSharedId(), item.getAmount());
            }
        }

        if (inventory.getItems().containsAll(items)) {
            LOGGER.info("successfully got all items");
            Bank.close();
        }
    }

    @Override
    public boolean activate() {
        if (activatable != null && !activatable.activate()) {
            return false;
        }

        return !inventory.getItems().containsAll(items);
    }
}
