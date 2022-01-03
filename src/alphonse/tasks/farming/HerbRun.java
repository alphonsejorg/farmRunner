package alphonse.tasks.farming;

import alphonse.tasks.GetItemsFromBank;
import alphonse.tasks.SuperTask;
import alphonse.tasks.Task;
import alphonse.tasks.TaskInfo;
import alphonse.tasks.farming.farmPatch.FarmPatch;
import alphonse.util.farming.FarmRun;

import java.util.Arrays;
import java.util.List;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;

public class HerbRun extends SuperTask {

    public HerbRun() {
        super(
                new TaskInfo(
                () -> farmRunTracker.getCurrentRun().equals(FarmRun.HERB_RUN),
                () -> !farmRunTracker.getCurrentRun().equals(FarmRun.HERB_RUN),
                "HerbRun",
                true
                ),
        true);
    }

    @Override
    public List<Task> getTasks() {
        return Arrays.asList(
                new GetItemsFromBank(farmRunTracker.getRequiredItems(), () -> !farmRunTracker.haveGottenItems()),
                new GetToFarmSpot(),
                new GetTools(),
                new FreeInventorySpace(),
                new FarmPatch(),
                new FinishFarmRun(() -> (farmRunTracker.getPatchOrder().isEmpty() && farmRunTracker.getCurrentFarmArea().getUnfinishedPatch() == null))
        );
    }
}
