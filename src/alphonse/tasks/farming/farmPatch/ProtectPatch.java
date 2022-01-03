package alphonse.tasks.farming.farmPatch;

import alphonse.tasks.Task;
import alphonse.util.farming.FarmRun;
import alphonse.util.farming.PatchStage;
import org.powbot.api.Condition;
import org.powbot.api.Input;
import org.powbot.api.rt4.*;

import static alphonse.util.Interact.interact;
import static alphonse.util.farming.FarmOptions.farmOptions;
import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmUtil.farmUtil;

public class ProtectPatch extends Task {

    @Override
    public void execute() {
        GameObject object = farmRunTracker.getCurrentPatch().getObject();
        LOGGER.info("protecting " + object.name() + "...");

        if (farmRunTracker.getCurrentRun().equals(FarmRun.FRUIT_TREE_RUN) || farmRunTracker.getCurrentRun().equals(FarmRun.TREE_RUN)) {
            Npc npc = Npcs.stream().action("Pay").nearest().first();

            if (interact.interact(npc,"Pay")) Condition.wait(() ->
                    Widgets.component(219,1,0).text().contains("Pay"),1000,6);

            if (Widgets.component(219,1,0).text().contains("Pay")) {
                Input.send("1");
                Condition.wait(() -> farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.FINISHED), 600, 15);
            }
        } else {
            interact.use(Inventory.stream().id(farmUtil.getCompostId(farmRunTracker.getCurrentPatch().getPatch())).first());
            if (interact.interact(object, "Use")) {
                Condition.wait(() -> farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.FINISHED), 600, 15);
            }
        }
        if (farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.FINISHED)) {
            LOGGER.info("successfully protected " + object.name());
            farmRunTracker.updateCurrentPatch();
        } else {
            LOGGER.info("failed to protect " + object.name());
        }
    }

    @Override
    public boolean activate() {
        return farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.PROTECT);
    }
}
