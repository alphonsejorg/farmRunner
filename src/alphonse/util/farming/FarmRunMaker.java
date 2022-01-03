package alphonse.util.farming;

import alphonse.scripts.Script;
import alphonse.util.constants.C_Items;
import alphonse.util.items.Item;
import alphonse.util.items.Items;
import alphonse.util.teleport.Teleport;
import alphonse.util.teleport.TeleportOption;
import alphonse.util.teleport.TeleportType;
import org.powbot.api.rt4.Constants;
import org.powbot.api.rt4.Skills;

import java.util.*;
import java.util.logging.Logger;

import static alphonse.util.farming.FarmOptions.farmOptions;
import static alphonse.util.farming.FarmSupplies.farmSupplies;
import static alphonse.util.farming.FarmUtil.farmUtil;
import static alphonse.util.items.Bank.bank;
import static alphonse.util.items.ChargeableItems.chargeableItems;
import static alphonse.util.managers.TeleportManager.teleportManager;

public class FarmRunMaker {
    private final static Logger LOGGER = Logger.getLogger("FarmRunMaker");

    private FarmRun farmRun;
    private Deque<FarmArea> patchOrder;
    private Map<FarmArea, Teleport> farmAreaTeleportMap;

    public FarmRunMaker(FarmRun farmRun) {
        this.farmRun = farmRun;
    }

    Deque<FarmArea> setPatchOrder() {
        ArrayList<FarmArea> farmingSpots = new ArrayList<>();
        switch (farmRun) {
            case HERB_RUN:
                farmingSpots.addAll(Arrays.asList(FarmArea.FALADOR_HAF, FarmArea.ARDOUGNE_HAF,
                        /*FarmArea.HOSIDIUS_HAF, TODO uncomment once webwalker issue is fixed*/ FarmArea.CATHERBY_HAF));
                if (Skills.realLevel(Constants.SKILLS_HITPOINTS) > 70) { // TODO add better condition - have to handle feral vampyre that can kill a low level player
                    farmingSpots.add(FarmArea.CANIFIS_HAF);
                }
                if (Skills.realLevel(Constants.SKILLS_FARMING) >= 65) {
                    farmingSpots.add(FarmArea.FARMING_GUILD_HAF);
                }
                break;

            case HOPS_RUN:
                farmingSpots.addAll(Arrays.asList(FarmArea.CHAMP_GUILD_HOPS, FarmArea.ENTRANA_HOPS,
                        FarmArea.CAMELOT_HOPS, FarmArea.YANILLE_HOPS));
                break;

            case TREE_RUN:
                farmingSpots.addAll(Arrays.asList(FarmArea.FALADOR_TREE, FarmArea.TAVERLEY_TREE,
                        FarmArea.GNOME_STRONGHOLD_TREE, FarmArea.VARROCK_TREE, FarmArea.LUMBRIDGE_TREE));
                if (Skills.realLevel(Constants.SKILLS_FARMING) >= 65) {
                    farmingSpots.add(FarmArea.FARMING_GUILD_TREE);
                }
                break;

            case FRUIT_TREE_RUN:
                farmingSpots.addAll(Arrays.asList(FarmArea.CATHERBY_FT, FarmArea.GNOME_VILLAGE_FT,
                        FarmArea.GNOME_STRONGHOLD_FT));

                if (Skills.realLevel(Constants.SKILLS_HITPOINTS) > 30) { // jungle spider can hit 5, usually only aggros when interacting with farmer watching over patch
                    farmingSpots.add(FarmArea.BRIMHAVEN_FT);
                }
                if (false) { // TODO find out how to determine if u can enter lletya
                    farmingSpots.add(FarmArea.LLETYA_FT);
                }
                if (Skills.realLevel(Constants.SKILLS_FARMING) >= 85) {
                    farmingSpots.add(FarmArea.FARMING_GUILD_FT);
                }
                break;

        }
        Collections.shuffle(farmingSpots);
        return (patchOrder =new ArrayDeque<>(farmingSpots));
    }

    Map<FarmArea, Teleport> setTeleportForFarmArea() {
        teleportManager.setHomeTeleportAssigned(false);
        Map<FarmArea, Teleport> farmAreaTeleportMap = new HashMap<>();
        ArrayList<FarmArea> toRemove = new ArrayList<>();
        for (FarmArea farmArea : patchOrder) {
            boolean assignedTeleport = false;
            LOGGER.info("assigning teleport to " + farmArea + "...");
            for (Teleport teleport : farmArea.getTeleportOptions()) {
                LOGGER.info("checking teleport: " + teleport);
                switch (teleport.getType()) {
                    case SPELL:
                        TeleportOption[] spellSettings = farmOptions.getSpellOptions();
                        for (TeleportOption spellSetting : spellSettings) {
                            LOGGER.info("checking spellSetting: " + spellSetting + "...");
                            if (!teleport.valid(spellSetting)) {
                                LOGGER.warning(teleport + " " + spellSetting + " is not valid");
                                continue;
                            }
                            teleport.setTeleportOption(spellSetting);
                            farmAreaTeleportMap.put(farmArea, teleport);
                            assignedTeleport = true;
                            break;
                        }
                        break;

                    case JEWELLERY:
                        if (teleport.valid(TeleportOption.JEWELLERY)) {
                            teleport.setTeleportOption(TeleportOption.JEWELLERY);
                            farmAreaTeleportMap.put(farmArea, teleport);
                            assignedTeleport = true;

                        } else {
                            LOGGER.warning(teleport + " " + teleport.getTeleportOption() + " is not valid");
                        }
                        break;

                    case HOME_TELEPORT:
                        if (teleportManager.getNextHomeTeleport() < System.currentTimeMillis() &&
                                !teleportManager.isHomeTeleportAssigned()) {
                            teleportManager.setHomeTeleportAssigned(true);
                            teleport.setTeleportOption(TeleportOption.NONE);
                            farmAreaTeleportMap.put(farmArea, teleport);
                            assignedTeleport = true;
                        }
                        break;

                    case ITEM:
                        teleport.setTeleportOption(TeleportOption.NONE);
                        break;

                    default:
                        Script.stopScript("runTracker#setTraverseOptionsForFarmAreas did not recognize teleport type: " + teleport.getType());
                        return farmAreaTeleportMap;

                }
                if (assignedTeleport) {
                    LOGGER.info("assigned " + teleport + " (" + teleport.getTeleportOption() + ") to " + farmArea);
                    break;
                }
            }
            if (!assignedTeleport) {
                LOGGER.warning("failed to assign any traverse option to " + farmArea);
                LOGGER.info("removing " + farmArea + " from patch order, since it can't be traversed");
                toRemove.add(farmArea);
            }
        }
        patchOrder.removeAll(toRemove);
        return (this.farmAreaTeleportMap = farmAreaTeleportMap);
    }

    Items setRequiredItems() {
        LOGGER.info("setting required items for " + farmRun + "...");
        Items requiredItems = new Items("required items for " + farmRun);
        Item item;

        Map<Teleport, Integer> map = new HashMap<>();
        for (Teleport teleport : farmAreaTeleportMap.values()) {
            LOGGER.info("setting required item for " + teleport);
            if (teleport.getType().equals(TeleportType.JEWELLERY)) {
                if (map.containsKey(teleport)) {
                    map.replace(teleport, map.get(teleport) + 1);
                } else {
                    map.put(teleport, 1);
                }
            } else {
                LOGGER.info("adding " + teleport.getRequiredItems().getName());
                requiredItems.addItems(teleport.getRequiredItems());
            }
        }

        for (Map.Entry<Teleport, Integer> set : map.entrySet()) {
            requiredItems.addItems(chargeableItems.toOwnedItems(set.getKey(), set.getValue()));
        }

        requiredItems.addItems(chargeableItems.toOwnedItems(Teleport.GRAND_EXCHANGE, 1));

        if (farmRun.equals(FarmRun.TREE_RUN) || farmRun.equals(FarmRun.FRUIT_TREE_RUN)) {
            requiredItems.addItem(new Item(C_Items.COINS, "Coins", 5000));
            if (farmSupplies.getSpade() < 1) {
                item = new Item(C_Items.SPADE, "Spade", 1);

                requiredItems.addItem(item);
            }
            if (farmSupplies.getRake() < 1) {
                item = new Item(C_Items.RAKE, "Rake", 1);

                requiredItems.addItem(item);
            }

        } else {
            if (farmSupplies.getSpade() < 1) {
                item = new Item(C_Items.SPADE, "Spade", 1);

                requiredItems.addItem(item);
            }
            if (farmSupplies.getRake() < 1) {
                item = new Item(C_Items.RAKE, "Rake", 1);

                requiredItems.addItem(item);
            }
            if (farmSupplies.getSeedDibber() < 1) {
                item = new Item(C_Items.SEED_DIBBER, "Seed dibber", 1);

                requiredItems.addItem(item);
            }
        }

        switch (farmRun) {
            case HERB_RUN:
                item = new Item(farmUtil.getSeedId(Patch.HERB, farmOptions.getPatchProduceMap().get(Patch.HERB)),
                        farmOptions.getPatchProduceMap().get(Patch.HERB),
                        farmUtil.getSeedsPerPatch(Patch.HERB, farmOptions.getPatchProduceMap().get(Patch.HERB)) * patchOrder.size());

                requiredItems.addItem(item);

                item = new Item(farmUtil.getSeedId(Patch.ALLOTMENT, farmOptions.getPatchProduceMap().get(Patch.ALLOTMENT)),
                        farmOptions.getPatchProduceMap().get(Patch.ALLOTMENT),
                        farmUtil.getSeedsPerPatch(Patch.ALLOTMENT, farmOptions.getPatchProduceMap().get(Patch.ALLOTMENT)) * patchOrder.size() * 2);

                requiredItems.addItem(item);

                item = new Item(farmUtil.getSeedId(Patch.FLOWER, farmOptions.getPatchProduceMap().get(Patch.FLOWER)),
                        farmOptions.getPatchProduceMap().get(Patch.FLOWER),
                        farmUtil.getSeedsPerPatch(Patch.FLOWER, farmOptions.getPatchProduceMap().get(Patch.FLOWER)) * patchOrder.size());

                requiredItems.addItem(item);
                break;

            case TREE_RUN:
                item = new Item(farmUtil.getSeedId(Patch.TREE, farmOptions.getPatchProduceMap().get(Patch.TREE)),
                        farmOptions.getPatchProduceMap().get(Patch.TREE),
                        farmUtil.getSeedsPerPatch(Patch.TREE, farmOptions.getPatchProduceMap().get(Patch.TREE)) * patchOrder.size());
                requiredItems.addItem(item);

                item = farmUtil.getProtectMap().get(farmUtil.getSeedId(Patch.TREE, farmOptions.getPatchProduceMap().get(Patch.TREE)));
                item.setAmount(item.getAmount() * patchOrder.size());
                item = item.toNoted();
                requiredItems.addItem(item);
                break;

            case HOPS_RUN:
                item = new Item(farmUtil.getSeedId(Patch.HOPS, farmOptions.getPatchProduceMap().get(Patch.HOPS)),
                        farmOptions.getPatchProduceMap().get(Patch.HOPS),
                        farmUtil.getSeedsPerPatch(Patch.HOPS, farmOptions.getPatchProduceMap().get(Patch.HOPS)) * patchOrder.size());

                requiredItems.addItem(item);
                break;

            case FRUIT_TREE_RUN:
                item = new Item(farmUtil.getSeedId(Patch.FRUIT_TREE, farmOptions.getPatchProduceMap().get(Patch.FRUIT_TREE)),
                        farmOptions.getPatchProduceMap().get(Patch.FRUIT_TREE),
                        farmUtil.getSeedsPerPatch(Patch.FRUIT_TREE, farmOptions.getPatchProduceMap().get(Patch.FRUIT_TREE)) * patchOrder.size());
                requiredItems.addItem(item);

                item = farmUtil.getProtectMap().get(farmUtil.getSeedId(Patch.FRUIT_TREE, farmOptions.getPatchProduceMap().get(Patch.FRUIT_TREE)));
                item.setAmount(item.getAmount() * patchOrder.size());
                item = item.toNoted();
                requiredItems.addItem(item);
                break;
        }



        if (!farmRun.equals(FarmRun.FRUIT_TREE_RUN) && !farmRun.equals(FarmRun.TREE_RUN)) {
            if (farmSupplies.getNormalCompost() < getCompostNeeded("Compost")) {
                item = new Item((C_Items.COMPOST), "Compost", Math.min(1000 - farmSupplies.getNormalCompost(),
                        bank.getItems().getItem(C_Items.COMPOST).getAmount())).toNoted();
                requiredItems.addItem(item);
            }
            if (farmSupplies.getSuperCompost() < getCompostNeeded("Supercompost")) {
                item = new Item((C_Items.SUPERCOMPOST), "Supercompost", Math.min(1000 - farmSupplies.getSuperCompost(),
                        bank.getItems().getItem(C_Items.SUPERCOMPOST).getAmount())).toNoted();
                requiredItems.addItem(item);
            }
            if (farmSupplies.getUltraCompost() < getCompostNeeded("Ultracompost")) {
                item = new Item((C_Items.ULTRACOMPOST),"Ultracompost", Math.min(1000 - farmSupplies.getUltraCompost(),
                        bank.getItems().getItem(C_Items.ULTRACOMPOST).getAmount())).toNoted();
                requiredItems.addItem(item);
            }
            if (farmOptions.isUsingBLB() && farmSupplies.getBLBAmount() < 1) {
                item = new Item((C_Items.BOTTOMLESS_BUCKET));
                requiredItems.addItem(item);
            }
        }
        requiredItems.print();
        return requiredItems;
    }

    private int getCompostNeeded(String compostName) {
        int compostNeeded = 0;
        if (farmRun.equals(FarmRun.HERB_RUN)) {
            if (farmOptions.getPatchActiveMap().get(Patch.ALLOTMENT) &&
                    farmOptions.getPatchCompostMap().get(Patch.ALLOTMENT).equals(compostName)) {
                compostNeeded += patchOrder.size() * 2;
            }
            if (farmOptions.getPatchActiveMap().get(Patch.FLOWER) &&
                    farmOptions.getPatchCompostMap().get(Patch.FLOWER).equals(compostName)) {
                compostNeeded += patchOrder.size();
            }
            if (farmOptions.getPatchActiveMap().get(Patch.HERB) &&
                    farmOptions.getPatchCompostMap().get(Patch.HERB).equals(compostName)) {
                compostNeeded += patchOrder.size();
            }

        } else if (farmRun.equals(FarmRun.HOPS_RUN)) {
            if (farmOptions.getPatchActiveMap().get(Patch.HOPS) &&
                    farmOptions.getPatchCompostMap().get(Patch.HOPS).equals(compostName)) {
                compostNeeded += patchOrder.size();
            }
        }
        return compostNeeded;
    }
}
