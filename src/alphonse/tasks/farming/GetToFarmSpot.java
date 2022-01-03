package alphonse.tasks.farming;

import alphonse.tasks.SuperTask;
import alphonse.tasks.Task;
import alphonse.tasks.TaskInfo;
import alphonse.tasks.traversing.Teleport;
import alphonse.util.farming.FarmRun;

import java.util.Arrays;
import java.util.List;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmUtil.farmUtil;
import static alphonse.util.managers.TeleportManager.teleportManager;

public class GetToFarmSpot extends SuperTask {

    public GetToFarmSpot() {
        super(new TaskInfo(
                () -> {
                    teleportManager.setHaveTeleported(false);
                    if (farmRunTracker.haveGottenItems() &&
                            (farmRunTracker.getCurrentFarmArea() == null || farmRunTracker.getCurrentFarmArea().getUnfinishedPatch() == null) &&
                        !farmRunTracker.getCurrentRun().equals(FarmRun.NIL) && farmUtil.canDoRun(farmRunTracker.getCurrentRun())) {
                        return  (!farmRunTracker.getPatchOrder().isEmpty());
                    }
                    return false;
                },
                () -> {
                    if (farmRunTracker.getPatchOrder().getFirst().equals(farmUtil.getFarmAreaYouAreAt())) {
                        farmRunTracker.setCurrentFarmArea(farmRunTracker.getPatchOrder().getFirst());
                        farmRunTracker.removeFirstFromPatchOrder();
                        farmRunTracker.updateCurrentPatch();
                        return true;
                    }
                    return false;
                },
                "GetToFarmSpot",
                false
        ), false);
    }


    @Override
    public List<Task> getTasks() {
        return Arrays.asList(
                new Teleport(farmRunTracker.getFarmAreaTeleportMap().get(farmRunTracker.getPatchOrder().getFirst())),
                new WalkToFarmArea()
        );
    }
}
