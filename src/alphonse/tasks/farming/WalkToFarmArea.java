package alphonse.tasks.farming;

import alphonse.tasks.Task;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.managers.TeleportManager.teleportManager;

public class WalkToFarmArea extends Task {

    @Override
    public void execute() {
        LOGGER.info("using web walker to walk to " + farmRunTracker.getPatchOrder().getFirst());
        Movement.walkTo(farmRunTracker.getPatchOrder().getFirst().getArea().getRandomTile());
    }

    @Override
    public boolean activate() {
        return teleportManager.haveTeleported() &&
        !farmRunTracker.getPatchOrder().getFirst().getArea().contains(Players.local());
    }
}
