package alphonse.tasks;

import org.powbot.api.Locatable;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;

import static alphonse.util.items.Bank.bank;

public class CacheBank extends Task {

    @Override
    public void execute() {
        LOGGER.info("getting to bank to cache items...");
        Locatable nearestBank = Bank.nearest();
        if (nearestBank.tile().distanceTo(Players.local()) > 10) {
            Movement.walkTo(nearestBank);
        }
        if (Bank.open()) {
            bank.cacheItems();
        }
    }

    @Override
    public boolean activate() {
        return !bank.isCached();
    }
}
