package alphonse.tasks.farming.farmPatch;

import alphonse.tasks.Task;
import alphonse.util.farming.FarmRun;
import alphonse.util.farming.PatchStage;
import org.powbot.api.Condition;
import org.powbot.api.Input;
import org.powbot.api.rt4.*;

import static alphonse.util.Interact.interact;
import static alphonse.util.farming.FarmRunTracker.farmRunTracker;

public class ClearPatch extends Task {

    @Override
    public void execute() {
        GameObject object = farmRunTracker.getCurrentPatch().getObject();
        LOGGER.info("clearing " + object.name() + "...");
        if (farmRunTracker.getCurrentRun().equals(FarmRun.FRUIT_TREE_RUN) || farmRunTracker.getCurrentRun().equals(FarmRun.TREE_RUN)) {
            Npc npc = Npcs.stream().action("Pay").nearest().first();

            if (interact.interact(npc,"Pay")) Condition.wait(() ->
                    Widgets.component(219,1,0).text().contains("have your tree chopped down?"),1000,6);

            if (Widgets.component(219,1,0).text().contains("have your tree chopped down?")) {
                Input.send("1");
                Condition.wait(() -> !farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.CLEAR), 600, 15);
                //Condition.wait(() -> farmRunTracker.getCurrentPatch().isCleared(), 600, 15);
            }
        } else {
            if (interact.interact(object, "Clear")) {
                //Condition.wait(() -> farmRunTracker.getCurrentPatch().isCleared(), 600, 15);
                Condition.wait(() -> !farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.CLEAR), 600, 15);
            }

        }
        if (!farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.CLEAR)) {
            LOGGER.info("successfully cleared patch");
        } else {
            LOGGER.info("failed to clear " + object.name());
        }
    }

    @Override
    public boolean activate() {
        return farmRunTracker.getCurrentPatch().getPatchStage().equals(PatchStage.CLEAR);
    }
}
