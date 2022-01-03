package alphonse.tasks.farming;

import alphonse.tasks.Task;
import alphonse.util.Interact;
import alphonse.util.constants.C_Items;
import alphonse.util.constants.C_Widgets;
import alphonse.util.farming.FarmArea;
import alphonse.util.farming.FarmRun;
import org.powbot.api.Condition;
import org.powbot.api.Input;
import org.powbot.api.rt4.*;

import java.util.ArrayList;
import java.util.List;

import static alphonse.util.Interact.interact;
import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmUtil.farmUtil;
import static alphonse.util.managers.ChatManager.chatManager;

public class FreeInventorySpace extends Task {

    private int[] array = null;

    @Override
    public void execute() {
        LOGGER.info("freeing inventory space...");
        if (array == null) {
            initArray();
        }

        // falador patch leprechaun wont note cabbages
        if (farmRunTracker.getCurrentRun().equals(FarmRun.HERB_RUN) && farmUtil.getFarmAreaYouAreAt().equals(FarmArea.FALADOR_HAF) &&
                !Inventory.stream().id(C_Items.CABBAGE).isEmpty()) {
            LOGGER.info("dropping cabbages at falador patch because leprechaun won't note them");
            Inventory.drop(Inventory.stream().id(C_Items.CABBAGE).list());
        }

        if (!Inventory.stream().id(array).isEmpty()) {
            Npc leprechaun = Npcs.stream().name("Tool Leprechaun").first();
            List<Integer> itemsNoted = new ArrayList<>();

            for (Item item : Inventory.stream().id(array).list()) {
                if (item.id() == -1 || itemsNoted.contains(item.id()) || item.stackable()) {
                    continue;
                }

                interact.use(item);
                if (interact.interact(leprechaun,"Use")) {
                    itemsNoted.add(item.id());
                    Condition.wait(() -> chatManager.chatMessagesContains("leprechaun exchanges your items", 1000), 600, 6);
                }
            }
            if (Chat.canContinue()) {
                Input.send(" ");
            }
        }

        if (!Inventory.stream().id(C_Items.WEEDS).isEmpty()) {
            Inventory.drop(Inventory.stream().id(C_Items.WEEDS).list());
        }
    }

    private void initArray() {
        array = new int[C_Items.ALLOTMENT_PRODUCE.length + C_Items.GRIMY_HERB_PRODUCE.length + C_Items.FLOWER_PRODUCE.length + C_Items.HOPS_PRODUCE.length + 1];
        int j = 1;
        for (Integer i : C_Items.ALLOTMENT_PRODUCE) {
            array[j++] = i;
        }
        for (Integer i : C_Items.GRIMY_HERB_PRODUCE) {
            array[j++] = i;
        }
        for (Integer i : C_Items.FLOWER_PRODUCE) {
            array[j++] = i;
        }
        for (Integer i : C_Items.HOPS_PRODUCE) {
            array[j++] = i;
        }
        array[0] = C_Items.WEEDS;
    }

    @Override
    public boolean activate() {
        if (farmUtil.getFarmAreaYouAreAt().equals(FarmArea.NONE)) {
            return false;
        }
        return (Inventory.emptySlotCount() < 8);
    }
}
