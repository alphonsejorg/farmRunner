package alphonse.tasks.traversing;

import alphonse.scripts.Script;
import alphonse.tasks.Activatable;
import alphonse.tasks.Task;
import alphonse.util.teleport.TeleportOption;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

import static alphonse.util.managers.TeleportManager.teleportManager;

public class Teleport extends Task {

    private alphonse.util.teleport.Teleport teleport;
    private Activatable activatable;

    public Teleport(Activatable activatable, alphonse.util.teleport.Teleport teleport) {
        this.activatable = activatable;
        this.teleport = teleport;
    }

    public Teleport(alphonse.util.teleport.Teleport teleport) {
        this.teleport = teleport;
    }

    @Override
    public void execute() {
        LOGGER.info("teleporting to " + teleport.parseIngameName() + "...");
        if (!teleport.valid()) {
            LOGGER.warning("teleport " + teleport + " is not valid");
            return;
        }
        switch (teleport.getType()) {
            case SPELL:
                if (teleport.getTeleportOption().equals(TeleportOption.SPELL)) {
                    LOGGER.info("casting spell: " + teleport.getSpell() + "...");
                    teleport.getSpell().cast();
                    Condition.wait(() -> teleport.getArea().contains(Players.local()), 500, 12);
                    Game.tab(Game.Tab.INVENTORY);
                } else if (teleport.getTeleportOption().equals(TeleportOption.TAB)) {
                    LOGGER.info("breaking tab: " + teleport.getTab().getName() + "...");
                    if (Inventory.stream().id(teleport.getTab().getSharedId()).first().interact("Break")) {
                        Condition.wait(() -> teleport.getArea().contains(Players.local()), 500, 12);
                    }
                }

            case JEWELLERY:
                if (teleport.getTeleportOption().equals(TeleportOption.JEWELLERY)) {
                    LOGGER.info("using jewellery: " + teleport.getChargeableItem() + "...");
                    teleport.useJewellery();
                }
                break;

            case HOME_TELEPORT:
                LOGGER.info("casting spell: " + teleport.getSpell() + "...");
                teleport.getSpell().cast();
                Game.tab(Game.Tab.INVENTORY);
                if (Condition.wait(() -> Players.local().animation() != -1, 300, 5)) {
                    if (Condition.wait(() -> teleport.getArea().contains(Players.local()), 600, 17)) {
                        LOGGER.info("successfully cast home teleport, setting timer for next home teleport to 30 minutes from now");
                        teleportManager.setNextHomeTeleport(System.currentTimeMillis() + 1800000);
                    }
                } else if (!teleport.getArea().contains(Players.local())) {
                    LOGGER.info("failed to teleport with home teleport");

                }
                break;

            case ITEM:
                teleport.useItem(teleport.getChargeableItem());
                break;

            default:
                Script.stopScript("teleport task have not yet implemented teleport type: " + teleport.getType());
                return;
        }

        if (Condition.wait(() -> teleport.getArea().contains(Players.local()), 600, 10)) {
            LOGGER.info("successfully teleported to " + teleport);
            teleportManager.setHaveTeleported(true);
        } else {
            LOGGER.info("failed to teleport");
        }
    }

    @Override
    public boolean activate() {
        if (activatable != null) {
            return activatable.activate();
        }

        return !teleportManager.haveTeleported();
    }
}
