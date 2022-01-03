package alphonse.tasks.farming.farmPatch;

import alphonse.tasks.SuperTask;
import alphonse.tasks.Task;
import alphonse.tasks.TaskInfo;
import alphonse.tasks.farming.FreeInventorySpace;
import alphonse.tasks.farming.GetTools;
import alphonse.util.farming.PatchListener;

import java.util.ArrayList;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;

public class FarmPatch extends SuperTask {

    public FarmPatch() {
        super(
                new TaskInfo(
                () -> {
                    if (farmRunTracker.getCurrentFarmArea().getUnfinishedPatch() != null) {
                        new PatchListener().start();
                        return true;
                    }
                    return false;
                },
                () -> farmRunTracker.getCurrentFarmArea().getUnfinishedPatch() == null,
                "FarmPatch",
                true
                )
        , false);
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new FreeInventorySpace());
        tasks.add(new GetTools());
        tasks.add(new CheckHealth());
        tasks.add(new ClearPatch());
        tasks.add(new HarvestPatch());
        tasks.add(new RakePatch());
        tasks.add(new PlantPatch());
        tasks.add(new ProtectPatch());
        return tasks;
    }
}
