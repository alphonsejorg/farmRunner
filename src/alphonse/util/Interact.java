package alphonse.util;

import org.powbot.api.*;
import org.powbot.api.rt4.*;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class Interact {
    private final static Logger LOGGER = Logger.getLogger("Interact");
    public final static Interact interact = new Interact();

    public static final int INTERACT_RANGE = 8;

    private Interact() {
    }

    public void use(Item item) {
        if (!Game.tab().equals(Game.Tab.INVENTORY)) {
            Game.tab(Game.Tab.INVENTORY);
        }
        if (Inventory.selectedItem().id() != item.id() && item.valid()) {
            removeSelectedItem();

            item.interact("Use", item.name());
            Condition.sleep(Random.nextInt(340,600));
        }
    }

    public boolean interact(final GameObject object, String action) {
        if (!action.equals("Use")) {
            removeSelectedItem();
        }
        if (!object.inViewport()) {
            Camera.turnTo(object);
        }

        if (object.tile().distanceTo(Players.local()) > INTERACT_RANGE) {
            Movement.step(object);
            Condition.wait((Callable<Boolean>) () -> object.tile().distanceTo(Players.local()) < 4);
        }

        return object.interact(action);
    }

    public boolean interact(final Npc npc, String action) {
        if (!action.equals("Use")) {
            removeSelectedItem();
        }
        if (!npc.inViewport()) {
            Camera.turnTo(npc);
            Movement.step(npc);
        }
        return npc.interact(action, npc.name());
    }

    public void removeSelectedItem() {
        if (Inventory.selectedItem().id() != -1) {
            if (!Game.tab().equals(Game.Tab.INVENTORY)) {
                Game.tab(Game.Tab.INVENTORY);
            }
            Inventory.selectedItem().interact("Cancel");
        }
    }
}
