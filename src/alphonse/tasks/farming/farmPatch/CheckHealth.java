package alphonse.tasks.farming.farmPatch;

import alphonse.tasks.Task;
import alphonse.util.farming.PatchStage;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Constants;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Skills;

import static alphonse.util.Interact.interact;
import static alphonse.util.farming.FarmRunTracker.farmRunTracker;

public class CheckHealth extends Task {

    @Override
    public void execute() {
        GameObject object = farmRunTracker.getCurrentPatch().getObject();
        LOGGER.info("checking health of " + object.name() + "...");
        int initialExp = Skills.experience(Constants.SKILLS_FARMING);
        if (interact.interact(object, "Check-health")) {
            if (Condition.wait(() -> Skills.experience(Constants.SKILLS_FARMING) > initialExp,1000,5)) {
                LOGGER.info("successfully checked health of patch");
            }
        }
    }

    @Override
    public boolean activate() {
        return farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.CHECK_HEALTH);
    }
}
