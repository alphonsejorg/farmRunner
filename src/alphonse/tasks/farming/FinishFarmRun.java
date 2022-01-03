package alphonse.tasks.farming;


import alphonse.tasks.Activatable;
import alphonse.tasks.Task;
import alphonse.tasks.traversing.Teleport;
import alphonse.util.farming.FarmRun;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;

public class FinishFarmRun extends Task {

    private Activatable activatable;

    public FinishFarmRun(Activatable activatable) {
        this.activatable = activatable;
    }

    @Override
    public void execute() {
        LOGGER.info("finishing farm run...");
        farmRunTracker.endCurrentRun();
        new Teleport(alphonse.util.teleport.Teleport.GRAND_EXCHANGE).execute();
    }

    @Override
    public boolean activate() {
        if (farmRunTracker.getCurrentRun().equals(FarmRun.NIL)) {
            return false;
        }

        return activatable.activate();
    }
}
