package alphonse.tasks.farming;

import alphonse.scripts.Script;
import alphonse.tasks.Task;
import alphonse.util.constants.C_Components;
import alphonse.util.constants.C_Items;
import alphonse.util.constants.C_Widgets;
import alphonse.util.farming.FarmArea;
import alphonse.util.farming.FarmRun;
import alphonse.util.farming.Patch;
import alphonse.util.items.Item;
import alphonse.util.items.Items;
import org.powbot.api.Condition;
import org.powbot.api.Input;
import org.powbot.api.Random;
import org.powbot.api.rt4.Component;
import org.powbot.api.rt4.Components;
import org.powbot.api.rt4.Npcs;
import org.powbot.api.rt4.Widgets;

import java.util.ArrayList;

import static alphonse.util.Interact.interact;
import static alphonse.util.farming.FarmOptions.farmOptions;
import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmSupplies.farmSupplies;
import static alphonse.util.farming.FarmUtil.farmUtil;
import static alphonse.util.items.Inventory.inventory;

public class GetTools extends Task {

    @Override
    public void execute() {
        if (!Widgets.widget(C_Widgets.FARMING_EQUIPMENT_STORE).valid()) {
            LOGGER.info("opening leprechaun tool storage");
            interact.interact(Npcs.stream().name("Tool Leprechaun").first(), "Exchange");
            if (!Condition.wait(() -> Widgets.widget(C_Widgets.FARMING_EQUIPMENT_STORE).valid(),1000,6)) {
                LOGGER.info("failed to open leprechaun tool storage");
                return;
            }
        }

        ArrayList<Component> components = farmUtil.getComponentsToDeposit();
        for (Component component : components) {
            component.interact("Store-All");
            Condition.wait(() -> component.component(C_Components.FARMING_SUB_AMOUNT).text().equals("0"), 400, 4);
        }

        if (farmOptions.isUsingBLB() && Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY,
                C_Components.FARMING_EQUIPMENT_INVENTORY_BL, C_Components.FARMING_EQUIPMENT_ITEM).itemStackSize() == 0) {
            LOGGER.info("getting bottomless bucket...");
            getBottomlessBucket();
        }

        Items itemsToWithdraw = getItemsToWithdraw();

        for (Item item : itemsToWithdraw.getItems()) {
            if (item.getSharedId() == C_Items.BOTTOMLESS_BUCKET) continue;
            LOGGER.info("attempting to withdraw " + item.getName() + "...");


            Component c = Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE, getComponent(item.getId()));
            LOGGER.info("component parent id: " + c.parentId() + ", index" + c.index());
            if (item.getId() == C_Items.SPADE || item.getId() == C_Items.SEED_DIBBER || item.getId() == C_Items.RAKE) {
                LOGGER.info("is rake, spade or seed dibber");
                c.interact("Remove-1");
                Condition.sleep(Random.nextInt(600, 1200));

            } else if (c.interact("Remove-X")) {
                if (Condition.wait(() -> Widgets.component(C_Widgets.CHAT_BOX, C_Components.CHAT_BOX_ENTER_AMOUNT).visible(), 600, 5)) {
                    Input.sendln(Integer.toString(item.getAmount()));
                }
            }

            Condition.sleep(Random.nextInt(300, 500));
        }

        LOGGER.info("closing leprechaun tool storage");
        if (Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE,1,11).click()) {
            Condition.sleep(Random.nextInt(700,1000));
        }
        farmSupplies.updateTools();
    }

    private Items getItemsToWithdraw() {
        Items items = new Items("items from leprechaun tool storage");

        if (farmRunTracker.getCurrentRun().equals(FarmRun.HERB_RUN)) {
            if (farmOptions.getPatchActiveMap().get(Patch.HERB)) {
                items.addItem(new Item(farmUtil.getCompostId(Patch.HERB), farmOptions.getPatchCompostMap().get(Patch.HERB)));
            }
            if (farmOptions.getPatchActiveMap().get(Patch.ALLOTMENT)) {
                items.addItem(new Item(farmUtil.getCompostId(Patch.ALLOTMENT), farmOptions.getPatchCompostMap().get(Patch.ALLOTMENT), 2));
            }
            if (farmOptions.getPatchActiveMap().get(Patch.FLOWER)) {
                items.addItem(new Item(farmUtil.getCompostId(Patch.FLOWER), farmOptions.getPatchCompostMap().get(Patch.FLOWER)));
            }
        } else if (farmRunTracker.getCurrentRun().equals(FarmRun.HOPS_RUN)) {
            if (farmOptions.getPatchActiveMap().get(Patch.HOPS)) {
                items.addItem(new Item(farmUtil.getCompostId(Patch.HOPS), farmOptions.getPatchCompostMap().get(Patch.HOPS)));
            }
        }

        if (farmOptions.isUsingBLB()) {
            switch (farmSupplies.getBLBCompostType()) {
                case "Compost":
                    items.removeItem(C_Items.COMPOST);
                    break;

                case "Supercompost":
                    items.removeItem(C_Items.SUPERCOMPOST);
                    break;

                case "Ultracompost":
                    items.removeItem(C_Items.ULTRACOMPOST);
                    break;
            }
        }

        if (!inventory.getItems().contains(C_Items.RAKE)) {
            items.addItem(new Item(C_Items.RAKE, "Rake"));
        }
        if (!inventory.getItems().contains(C_Items.SPADE)) {
            items.addItem(new Item(C_Items.SPADE, "Spade"));
        }
        if (!(farmRunTracker.getCurrentRun().equals(FarmRun.FRUIT_TREE_RUN) ||
                farmRunTracker.getCurrentRun().equals(FarmRun.TREE_RUN)) && !inventory.getItems().contains(C_Items.SEED_DIBBER)) {
            items.addItem(new Item(C_Items.SEED_DIBBER, "Seed dibber"));
        }

        items.print();
        return items;
    }

    private int getComponent(int itemId) {
        switch (itemId) {
            case C_Items.COMPOST:
                return C_Components.FARMING_EQUIPMENT_STORE_COMPOST;

            case C_Items.SUPERCOMPOST:
                return C_Components.FARMING_EQUIPMENT_STORE_SUPER_COMPOST;

            case C_Items.ULTRACOMPOST:
                return C_Components.FARMING_EQUIPMENT_STORE_ULTRA_COMPOST;

            case C_Items.BOTTOMLESS_BUCKET:
                return C_Components.FARMING_EQUIPMENT_STORE_BL;

            case C_Items.RAKE:
                return C_Components.FARMING_EQUIPMENT_STORE_RAKE;

            case C_Items.SPADE:
                return C_Components.FARMING_EQUIPMENT_STORE_SPADE;

            case C_Items.SEED_DIBBER:
                return C_Components.FARMING_EQUIPMENT_STORE_SEED_DIBBER;

            default:
                Script.stopScript("GetStoredCompost#GetComponent did not recognize item Id of item: " + itemId);
                return -1;
        }
    }

    private void getBottomlessBucket() {
        if (Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE, 0).visible()) {
            if (Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE, C_Components.FARMING_EQUIPMENT_STORE_BL,
                    C_Components.FARMING_EQUIPMENT_ITEM).itemStackSize() == 0) {
                LOGGER.info("no bottomless bucket in farming equipment store");
                farmOptions.setUsingBLB(false);
                return;
            }

            if (Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE, C_Components.FARMING_EQUIPMENT_STORE_BL,
                    C_Components.FARMING_EQUIPMENT_ITEM).itemStackSize() > 0) {
                Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE, C_Components.FARMING_EQUIPMENT_STORE_BL,
                        C_Components.FARMING_EQUIPMENT_ITEM).click();
            }
        }
    }

    @Override
    public boolean activate() {
        if (farmRunTracker.getCurrentRun().equals(FarmRun.NIL) || farmUtil.getFarmAreaYouAreAt().equals(FarmArea.NONE)) {
            return false;
        }

        if (farmRunTracker.getCurrentRun().equals(FarmRun.FRUIT_TREE_RUN) ||
                farmRunTracker.getCurrentRun().equals(FarmRun.TREE_RUN)) {
            return !inventory.getItems().containsAll(C_Items.SPADE, C_Items.RAKE);
        }

        int compostId = farmUtil.getCompostId(farmRunTracker.getCurrentPatch().getPatch());
        return !inventory.getItems().containsAll(compostId, C_Items.SPADE, C_Items.RAKE, C_Items.SEED_DIBBER);
    }
}
