package alphonse.tasks.farming.farmPatch;

import alphonse.tasks.Task;
import alphonse.util.farming.PatchStage;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Inventory;

import static alphonse.util.Interact.interact;
import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmUtil.farmUtil;

public class HarvestPatch extends Task {

    @Override
    public void execute() {
        GameObject object = farmRunTracker.getCurrentPatch().getObject();
        LOGGER.info("harvesting " + object.name() + "...");

        if (interact.interact(object, farmUtil.getHarvestAction(farmRunTracker.getCurrentPatch()))) {
            Condition.wait(() -> !farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.HARVEST) || Inventory.isFull(), 600, 35);
            //Condition.wait(() -> farmRunTracker.getCurrentPatch().isHarvested() || Inventory.isFull(), 600, 35);
        }
        if (!farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.HARVEST)) {
            LOGGER.info("successfully harvested patch");
        } else if (!Inventory.isFull()) {
            LOGGER.info("failed to harvest " + object.name());
        }
    }

    @Override
    public boolean activate() {
        if (Inventory.isFull()) {
            return false;
        }

        return farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.HARVEST);
    }
}
