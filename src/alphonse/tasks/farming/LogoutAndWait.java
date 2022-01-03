package alphonse.tasks.farming;

import alphonse.tasks.Task;
import alphonse.util.farming.FarmRun;
import org.powbot.api.Condition;
import org.powbot.api.Preferences;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Game;
import org.powbot.mobile.script.ScriptManager;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmUtil.farmUtil;

public class LogoutAndWait extends Task {

    @Override
    public void execute() {
        LOGGER.info("logging out...");
        Bank.close();
        Preferences.setAutoLoginEnabled(false);
        Game.logout();
        if (!Game.loggedIn()) {
            LOGGER.info("successfully logged out");
            LOGGER.info("waiting " + ((farmUtil.getNextHarvest(farmRunTracker.getNextRun()) - System.currentTimeMillis()) / 1000)  + " seconds for next run: " + farmRunTracker.getNextRun() + "...");
        }
        Condition.wait(() -> Game.loggedIn() || ScriptManager.INSTANCE.isStopping(), (int) (farmUtil.getNextHarvest(farmRunTracker.getNextRun()) / 1000), 1000);
        Preferences.setAutoLoginEnabled(true);
    }

    @Override
    public boolean activate() {
        if (!farmRunTracker.getCurrentRun().equals(FarmRun.NIL)) {
            return false;
        }
        return !farmUtil.anyFarmRunReady();
    }
}
