package alphonse.tasks.farming.farmPatch;

import alphonse.tasks.Task;
import alphonse.util.farming.PatchStage;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Inventory;

import static alphonse.util.Interact.interact;
import static alphonse.util.farming.FarmRunTracker.farmRunTracker;

public class RakePatch extends Task {

    @Override
    public void execute() {
        GameObject object = farmRunTracker.getCurrentPatch().getObject();
        LOGGER.info("raking " + object.name() + "...");

        if (interact.interact(object, "Rake")) {
            Condition.wait(() -> !farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.WEED) || Inventory.isFull(), 600, 30);
        }
        if (!farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.WEED)) {
            LOGGER.info("successfully raked " + object.name());

        } else if (!Inventory.isFull()) {
            LOGGER.info("failed to rake " + object.name());
        }
    }

    @Override
    public boolean activate() {
        return farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.WEED);
    }
}
