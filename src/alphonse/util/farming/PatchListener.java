package alphonse.util.farming;

import alphonse.tasks.TaskInfo;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Chat;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Objects;
import org.powbot.mobile.script.ScriptManager;

import java.util.logging.Logger;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmUtil.farmUtil;
import static alphonse.util.managers.ChatManager.chatManager;

public class PatchListener implements Runnable {
    private final static Logger LOGGER = Logger.getLogger("PatchListener");
    private Thread t;

    private static boolean running = false;

    @Override
    public void run() {
        running = true;
        try {
            long startTime = System.currentTimeMillis();
            LOGGER.info("running " + t.getName() + " thread...");
            while (!ScriptManager.INSTANCE.isStopping() && TaskInfo.getActiveTaskName().equals("FarmPatch") && Game.loggedIn()) {
                switch (farmRunTracker.getCurrentPatch().getPatchStage()) {
                    case NONE:
                        if (checkCheckHealth()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.CHECK_HEALTH);
                            break;
                        }

                        if (checkHarvest()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.HARVEST);
                            break;
                        }

                        if (checkClear()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.CLEAR);
                            break;
                        }

                        if (checkWeed()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.WEED);
                            break;
                        }

                        if (checkPlant()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.PLANT);
                            break;
                        }
                        farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.PROTECT);
                        break;

                    case CLEAR:
                        if (checkWeed()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.WEED);
                        } else if (!checkClear() && checkPlant()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.PLANT);
                        }
                        break;

                    case HARVEST:
                        if (checkWeed()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.WEED);
                        } else if (!checkHarvest()) {
                            if (farmRunTracker.getCurrentRun().equals(FarmRun.FRUIT_TREE_RUN) && checkClear()) {
                                farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.CLEAR);

                            } else if (checkPlant()) {
                                farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.PLANT);
                            }
                        }
                        break;

                    case CHECK_HEALTH:
                        if (checkWeed()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.WEED);
                        } else if (!checkCheckHealth()) {
                            if (farmRunTracker.getCurrentRun().equals(FarmRun.TREE_RUN) && checkClear()) {
                                farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.CLEAR);
                            }
                            if (farmRunTracker.getCurrentRun().equals(FarmRun.FRUIT_TREE_RUN) && checkHarvest()) {
                                farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.HARVEST);
                            }
                        }
                        break;

                    case WEED:
                        if (!checkWeed()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.PLANT);
                        }
                        break;

                    case PLANT:
                        if (checkWeed()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.WEED);
                        } else if (!checkPlant()) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.PROTECT);
                        }
                        break;

                    case PROTECT:
                        if (farmRunTracker.getCurrentRun().equals(FarmRun.FRUIT_TREE_RUN) ||
                                farmRunTracker.getCurrentRun().equals(FarmRun.TREE_RUN) &&
                                        (chatManager.checkContainsAndRemove("ou pay the gardener", 3000) ||
                                                Chat.getChatMessage().contains("already looking after that patch"))) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.FINISHED);

                        } else if (chatManager.checkContainsAndRemove("already been treated", 2000) ||
                                chatManager.checkContainsAndRemove("You treat", 2000)) {
                            farmRunTracker.getCurrentPatch().setPatchStage(PatchStage.FINISHED);
                        }
                        break;
                }
                Condition.sleep(600);
            }
            LOGGER.info("ending " + t.getName() + " [" + (System.currentTimeMillis() - startTime) + " ms]");
        } catch (Exception e) {
            LOGGER.warning("thread stopped because of error");
            e.printStackTrace();
        }
        running = false;
    }

    private boolean checkWeed() {
        return (Objects.stream().within(farmRunTracker.getCurrentPatch().getArea()).action("Rake").isNotEmpty());
    }

    private boolean checkClear() {
        return (Objects.stream().within(farmRunTracker.getCurrentPatch().getArea()).action("Clear", "Chop-down", "Chop", "Chop down", "chop-down").isNotEmpty());
    }

    private boolean checkHarvest() {
        return (Objects.stream().within(farmRunTracker.getCurrentPatch().getArea()).action("Harvest", farmUtil.getHarvestAction(farmRunTracker.getCurrentPatch())).isNotEmpty());
    }

    private boolean checkCheckHealth() {
        return (Objects.stream().within(farmRunTracker.getCurrentPatch().getArea()).action("Check-health").isNotEmpty());
    }

    private boolean checkPlant() {
        return (Objects.stream().within(farmRunTracker.getCurrentPatch().getArea()).name(farmRunTracker.getCurrentPatch().getPatch().getClearedName()).isNotEmpty());
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, "PatchListener");
            LOGGER.info("Starting " + t.getName() + " Thread");
        }
        if (!running) {
            t.start();
        }
    }
}
