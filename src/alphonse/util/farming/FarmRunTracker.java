package alphonse.util.farming;

import alphonse.scripts.Script;
import alphonse.util.teleport.Teleport;
import alphonse.util.items.Items;
import org.powbot.api.rt4.Bank;

import java.util.*;
import java.util.logging.Logger;

import static alphonse.util.farming.FarmOptions.farmOptions;
import static alphonse.util.farming.FarmUtil.farmUtil;
import static alphonse.util.items.Bank.bank;
import static alphonse.util.items.Inventory.inventory;

public class FarmRunTracker {
    private final static Logger LOGGER = Logger.getLogger("FarmRunTracker");
    public final static FarmRunTracker farmRunTracker = new FarmRunTracker();

    private Items requiredItems = new Items("FarmRun items");

    private Deque<FarmArea> patchOrder = new ArrayDeque<>();
    private Map<FarmArea, Teleport> farmAreaTeleportMap = new HashMap<>();

    private FarmRun currentRun = FarmRun.NIL;
    private FarmRun nextRun = FarmRun.NIL;

    private PatchInfo currentPatch = null;
    private FarmArea currentFarmArea = FarmArea.NONE;

    private boolean gottenItems = false;

    private long runTime = 0;

    private FarmRunTracker() {
        initMaps();
    }

    private void initMaps() {
    }

    public void updateCurrentPatch() {
        PatchInfo patchInfo = currentFarmArea.getUnfinishedPatch();
        if (patchInfo != null) {
            currentPatch = patchInfo;
            LOGGER.info("updated current patch to: " + currentPatch.getPatch());
        }
    }

    public void endCurrentRun() {
        long runTime = System.currentTimeMillis() - this.runTime;
        long seconds = (runTime / 1000) % 60;
        long minutes = (runTime / 60000) % 60;
        long hours = (runTime / (3600000)) % 24;
        if (hours > 0) {
            LOGGER.info("finished " + this.currentRun + " in " + String.format("%02d:%02d:%02d", hours, minutes, seconds));
        } else {
            LOGGER.info("finished " + this.currentRun + " in " + String.format("%02d:%02d", minutes, seconds));
        }

        switch (currentRun) {
            case FRUIT_TREE_RUN:
                farmUtil.setNextHarvest(Patch.FRUIT_TREE);
                break;

            case HOPS_RUN:
                farmUtil.setNextHarvest(Patch.HOPS);
                break;

            case TREE_RUN:
                farmUtil.setNextHarvest(Patch.TREE);
                break;

            case HERB_RUN:
                if (farmOptions.getPatchActiveMap().get(Patch.HERB)) {
                    farmUtil.setNextHarvest(Patch.HERB);
                }
                if (farmOptions.getPatchActiveMap().get(Patch.ALLOTMENT)) {
                    farmUtil.setNextHarvest(Patch.ALLOTMENT);
                }
                if (farmOptions.getPatchActiveMap().get(Patch.FLOWER)) {
                    farmUtil.setNextHarvest(Patch.FLOWER);
                }
                break;
        }
        currentRun = FarmRun.NIL;
        requiredItems = Items.NIL;
    }

    public boolean startNewRun(FarmRun newRun) {
        LOGGER.info("trying to start new run: " + newRun + "...");
        runTime = System.currentTimeMillis();
        this.currentRun = newRun;

        FarmRunMaker farmRunMaker = new FarmRunMaker(newRun);
        patchOrder = farmRunMaker.setPatchOrder();
        farmAreaTeleportMap = farmRunMaker.setTeleportForFarmArea();
        requiredItems = farmRunMaker.setRequiredItems();
        gottenItems = checkIfHaveGottenItems();

        if (patchOrder.isEmpty()) {
            LOGGER.warning("was not able to assign any farm areas for " + newRun);
            return false;
        }

        Items missingItems = requiredItems.getMissingItems(bank.getItems());
        if (!missingItems.getItems().isEmpty()) {
            Script.stopScript("missing one or more items: " +
                    missingItems.toString());
            return false;
        }

        if (gottenItems && Bank.opened()) {
            Bank.close();
        }
        return true;
    }

    private boolean checkIfHaveGottenItems() {
        return inventory.getItems().containsAll(requiredItems);
    }

    public void removeFirstFromPatchOrder() {
        patchOrder.removeFirst();
    }

    public FarmRun getCurrentRun() {
        return currentRun;
    }

    public FarmRun getNextRun() {
        return nextRun;
    }

    public FarmArea getCurrentFarmArea() {
        return currentFarmArea;
    }

    public PatchInfo getCurrentPatch() {
        if (currentPatch == null) {
            updateCurrentPatch();
        }
        return currentPatch;
    }

    public Deque<FarmArea> getPatchOrder() {
        return patchOrder;
    }

    public Items getRequiredItems() {
        return requiredItems;
    }

    public Map<FarmArea, Teleport> getFarmAreaTeleportMap() {
        return farmAreaTeleportMap;
    }

    public void setCurrentFarmArea(FarmArea currentFarmArea) {
        this.currentFarmArea = currentFarmArea;
    }

    public void setNextRun(FarmRun nextRun) {
        this.nextRun = nextRun;
    }

    public boolean haveGottenItems() {
        if (!gottenItems) {
            return (gottenItems = checkIfHaveGottenItems());
        }
        return true;
    }
}
