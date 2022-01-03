package alphonse.tasks.farming;

import alphonse.scripts.Script;
import alphonse.tasks.Task;
import alphonse.util.farming.FarmRun;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmUtil.farmUtil;

public class GetNewRun extends Task {

    @Override
    public void execute() {
        LOGGER.info("getting new farm runs to do...");
        FarmRun farmRun = FarmRun.NIL;
        FarmRun nextRun = FarmRun.NIL;

        if (farmUtil.canDoFruitTreeRun()) {
            if (farmUtil.farmRunReady(FarmRun.FRUIT_TREE_RUN)) {
                farmRun = FarmRun.FRUIT_TREE_RUN;
            }
            if (!farmRun.equals(FarmRun.FRUIT_TREE_RUN) && farmUtil.getNextHarvest(nextRun) > farmUtil.getNextHarvest(FarmRun.FRUIT_TREE_RUN)) {
                nextRun = FarmRun.FRUIT_TREE_RUN;
            }
        }
        if (farmUtil.canDoTreeRun()) {
            if (farmRun.equals(FarmRun.NIL) && farmUtil.farmRunReady(FarmRun.TREE_RUN)) {
                farmRun = FarmRun.TREE_RUN;
            }
            if (nextRun.equals(FarmRun.NIL) || farmUtil.getNextHarvest(nextRun) > farmUtil.getNextHarvest(FarmRun.TREE_RUN)) {
                nextRun = FarmRun.TREE_RUN;
            }
        }

        if (farmUtil.canDoHerbRun()) {
            if (farmRun.equals(FarmRun.NIL) && farmUtil.farmRunReady(FarmRun.HERB_RUN)) {
                farmRun = FarmRun.HERB_RUN;
            }
            if (nextRun.equals(FarmRun.NIL) || farmUtil.getNextHarvest(nextRun) > farmUtil.getNextHarvest(FarmRun.HERB_RUN)) {
                nextRun = FarmRun.HERB_RUN;
            }
        }
        if (farmUtil.canDoHopsRun()) {
            if (farmRun.equals(FarmRun.NIL) && farmUtil.farmRunReady(FarmRun.HOPS_RUN)) {
                farmRun = FarmRun.HOPS_RUN;
            }
            if (nextRun.equals(FarmRun.NIL) || farmUtil.getNextHarvest(nextRun) > farmUtil.getNextHarvest(FarmRun.HOPS_RUN)) {
                nextRun = FarmRun.HOPS_RUN;
            }
        }

        farmRunTracker.setNextRun(nextRun);
        if (farmRun.equals(FarmRun.NIL)) {
            LOGGER.info("no available farm run to do at this moment");
        }
        if (farmRunTracker.startNewRun(farmRun)) {
            LOGGER.info("can start run: " + farmRun);
        } else {
            LOGGER.warning("failed to start a new run...");
        }
    }

    @Override
    public boolean activate() {
        return (farmRunTracker.getCurrentRun().equals(FarmRun.NIL) && farmUtil.canDoARun() && farmUtil.anyFarmRunReady()) ||
                farmRunTracker.getNextRun().equals(FarmRun.NIL);
    }
}
