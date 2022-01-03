package alphonse.tasks.farming.farmPatch;

import alphonse.tasks.Task;
import alphonse.util.farming.PatchStage;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GameObject;

import static alphonse.util.Interact.interact;
import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmUtil.farmUtil;

public class PlantPatch extends Task {

    @Override
    public void execute() {
        GameObject object = farmRunTracker.getCurrentPatch().getObject();
        LOGGER.info("planting " + farmUtil.getSeed(farmRunTracker.getCurrentPatch().getPatch()).name() + "...");

        interact.use(farmUtil.getSeed(farmRunTracker.getCurrentPatch().getPatch()));
        interact.interact(object, "Use");

        Condition.wait(() -> !farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.PLANT), 600, 6);

        if (!farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.PLANT)) {
            LOGGER.info("successfully planted " + farmUtil.getSeed(farmRunTracker.getCurrentPatch().getPatch()).name());
        } else {
            LOGGER.info("failed to plant " + farmUtil.getSeed(farmRunTracker.getCurrentPatch().getPatch()).name());
        }
    }

    @Override
    public boolean activate() {
        return farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.PLANT);
    }
}
