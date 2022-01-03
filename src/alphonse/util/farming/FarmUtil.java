package alphonse.util.farming;

import alphonse.scripts.Script;
import alphonse.util.AreaUtil;
import alphonse.util.Database;
import alphonse.util.constants.C_Components;
import alphonse.util.constants.C_Items;
import alphonse.util.constants.C_Widgets;
import alphonse.util.items.Item;
import alphonse.util.constants.LevelRequirements;
import org.powbot.api.Preferences;
import org.powbot.api.rt4.*;

import java.util.*;
import java.util.logging.Logger;

import static alphonse.util.farming.FarmOptions.farmOptions;
import static alphonse.util.farming.FarmSupplies.farmSupplies;

public class FarmUtil {
    public final static FarmUtil farmUtil = new FarmUtil();
    private static Logger LOGGER;

    private HashMap<Patch, Integer> patchSuspendedMap = new HashMap<>();
    private HashMap<Patch, Long> patchNextHarvestMap = new HashMap<>();

    private Map<Integer, Integer> herbLevelMap = new HashMap<>();
    private Map<Integer, Integer> hopsLevelMap = new HashMap<>();
    private Map<Integer, Integer> flowerLevelMap = new HashMap<>();
    private Map<Integer, Integer> allotmentLevelMap = new HashMap<>();
    private Map<Integer, Integer> fruitTreeLevelMap = new HashMap<>();
    private Map<Integer, Integer> treeLevelMap = new HashMap<>();
    private HashMap<Integer, Item> protectMap = new HashMap<>();

    private FarmUtil() {
        LOGGER = Logger.getLogger("FarmUtil");
        initMaps();
    }

    private void initMaps() {
        protectMap.put(C_Items.OAK_SAPLING, new Item(C_Items.BASKET_OF_TOMATOES, "Tomatoes(5)", 1));
        protectMap.put(C_Items.WILLOW_SAPLING, new Item(C_Items.BASKET_OF_APPLES, "Apples(5)", 1));
        protectMap.put(C_Items.MAPLE_SAPLING, new Item(C_Items.BASKET_OF_ORANGES, "Oranges(5)", 1));
        protectMap.put(C_Items.YEW_SAPLING, new Item(C_Items.CACTUS_SPINE, "Cactus spine", 10));
        protectMap.put(C_Items.MAGIC_SAPLING, new Item(C_Items.COCONUT, "Coconut", 25));
        protectMap.put(C_Items.APPLE_SAPLING, new Item(C_Items.SWEETCORN, "Sweetcorn", 9));
        protectMap.put(C_Items.BANANA_SAPLING, new Item(C_Items.BASKET_OF_APPLES, "Apples(5)", 4));
        protectMap.put(C_Items.ORANGE_SAPLING, new Item(C_Items.BASKET_OF_STRAWBERRIES, "Strawberries(5)", 3));
        protectMap.put(C_Items.CURRY_SAPLING, new Item(C_Items.BASKET_OF_BANANAS, "Bananas(5)", 5));
        protectMap.put(C_Items.PINEAPPLE_SAPLING, new Item(C_Items.WATERMELON, "watermelon", 10));
        protectMap.put(C_Items.PAPAYA_SAPLING, new Item(C_Items.PINEAPPLE, "pineapple", 10));
        protectMap.put(C_Items.PALM_TREE_SAPLING, new Item(C_Items.PAPAYA_FRUIT, "Papaya fruit", 15));
        protectMap.put(C_Items.DRAGONFRUIT_SAPLING, new Item(C_Items.COCONUT, "Coconut", 15));

        fruitTreeLevelMap.put(C_Items.APPLE_SAPLING, LevelRequirements.APPLE);
        fruitTreeLevelMap.put(C_Items.BANANA_SAPLING, LevelRequirements.BANANA);
        fruitTreeLevelMap.put(C_Items.ORANGE_SAPLING, LevelRequirements.ORANGE);
        fruitTreeLevelMap.put(C_Items.CURRY_SAPLING, LevelRequirements.CURRY);
        fruitTreeLevelMap.put(C_Items.PINEAPPLE_SAPLING, LevelRequirements.PINEAPPLE);
        fruitTreeLevelMap.put(C_Items.PAPAYA_SAPLING, LevelRequirements.PAPAYA);
        fruitTreeLevelMap.put(C_Items.PALM_TREE_SAPLING, LevelRequirements.PALM_TREE);
        fruitTreeLevelMap.put(C_Items.DRAGONFRUIT_SAPLING, LevelRequirements.DRAGONFRUIT);

        treeLevelMap.put(C_Items.OAK_SAPLING, LevelRequirements.OAK_SAPLING);
        treeLevelMap.put(C_Items.WILLOW_SAPLING, LevelRequirements.WILLOW_SAPLING);
        treeLevelMap.put(C_Items.MAPLE_SAPLING, LevelRequirements.MAPLE_SAPLING);
        treeLevelMap.put(C_Items.YEW_SAPLING, LevelRequirements.YEW_SAPLING);
        treeLevelMap.put(C_Items.MAGIC_SAPLING, LevelRequirements.MAGIC_SAPLING);

        hopsLevelMap.put(C_Items.BARLEY_SEED, LevelRequirements.BARLEY);
        hopsLevelMap.put(C_Items.HAMMERSTONE, LevelRequirements.HAMMERSTONE);
        hopsLevelMap.put(C_Items.ASGARNIAN_SEED, LevelRequirements.ASGARNIAN_HOPS);
        hopsLevelMap.put(C_Items.JUTE_SEED, LevelRequirements.JUTE);
        hopsLevelMap.put(C_Items.YANILLIAN_SEED, LevelRequirements.YANILLIAN_HOPS);
        hopsLevelMap.put(C_Items.KRANDORIAN_SEED, LevelRequirements.KRANDORIAN_HOPS);
        hopsLevelMap.put(C_Items.WILDBLOOD_SEED, LevelRequirements.WILDBLOOD);

        herbLevelMap.put(C_Items.GUAM_SEED, LevelRequirements.GUAM);
        herbLevelMap.put(C_Items.MARRENTILL_SEED, LevelRequirements.MARRENTILL);
        herbLevelMap.put(C_Items.TARROMIN_SEED, LevelRequirements.TARROMIN);
        herbLevelMap.put(C_Items.HARRALANDER_SEED, LevelRequirements.HARRALANDER);
        herbLevelMap.put(C_Items.RANARR_SEED, LevelRequirements.RANARR);
        herbLevelMap.put(C_Items.TOADFLAX_SEED, LevelRequirements.TOADFLAX);
        herbLevelMap.put(C_Items.IRIT_SEED, LevelRequirements.IRIT_LEAF);
        herbLevelMap.put(C_Items.AVANTOE_SEED, LevelRequirements.AVANTOE);
        herbLevelMap.put(C_Items.KWUARM_SEED, LevelRequirements.KWUARM);
        herbLevelMap.put(C_Items.SNAPDRAGON_SEED, LevelRequirements.SNAPDRAGON);
        herbLevelMap.put(C_Items.CADANTINE_SEED, LevelRequirements.CADANTINE);
        herbLevelMap.put(C_Items.LANTADYME_SEED, LevelRequirements.LANTADYME);
        herbLevelMap.put(C_Items.DWARF_WEED_SEED, LevelRequirements.DWARF_WEED);
        herbLevelMap.put(C_Items.TORSTOL_SEED, LevelRequirements.TORSTOL);

        flowerLevelMap.put(C_Items.MARIGOLD_SEED, LevelRequirements.MARIGOLD);
        flowerLevelMap.put(C_Items.ROSEMARY_SEED, LevelRequirements.ROSEMARY);
        flowerLevelMap.put(C_Items.NASTURTIUM_SEED, LevelRequirements.NASTURTIUM);
        flowerLevelMap.put(C_Items.WOAD_LEAF_SEED, LevelRequirements.WOAD_LEAF);
        flowerLevelMap.put(C_Items.LIMPWURT_SEED, LevelRequirements.LIMPWURT);
        flowerLevelMap.put(C_Items.WHITE_LILY_SEED, LevelRequirements.WHITE_LILY);

        allotmentLevelMap.put(C_Items.POTATO_SEED, LevelRequirements.POTATO);
        allotmentLevelMap.put(C_Items.ONION_SEED, LevelRequirements.ONION);
        allotmentLevelMap.put(C_Items.CABBAGE_SEED, LevelRequirements.CABBAGE);
        allotmentLevelMap.put(C_Items.TOMATO_SEED, LevelRequirements.TOMATO);
        allotmentLevelMap.put(C_Items.SWEETCORN_SEED, LevelRequirements.SWEETCORN);
        allotmentLevelMap.put(C_Items.STRAWBERRY_SEED, LevelRequirements.STRAWBERRY);
        allotmentLevelMap.put(C_Items.WATERMELON_SEED, LevelRequirements.WATERMELON);
        allotmentLevelMap.put(C_Items.SNAPE_GRASS_SEED, LevelRequirements.SNAPE_GRASS);
    }

    public String getHarvestAction(PatchInfo patchInfo) {
        switch (patchInfo.getPatch()) {
            case FLOWER: case HERB:
                return "Pick";

            case ALLOTMENT: case HOPS:
                return "Harvest";

            case FRUIT_TREE:
                return getFruitTreePickAction(patchInfo);

            case NIL: default:
                return "æøp";
        }
    }

    private String getFruitTreePickAction(PatchInfo patchInfo) {
        for (String action : patchInfo.getObject().actions()) {
            if (action == null) {
                continue;
            }

            if (action.toLowerCase().contains("pick")) {
                return action;
            }
        }
        return "none";
    }

    public org.powbot.api.rt4.Item getSeed(Patch patch) {
        switch (patch) {
            case ALLOTMENT:
                return Inventory.stream().id(C_Items.ALLOTMENT_SEEDS).first();

            case FLOWER:
                return Inventory.stream().id(C_Items.FLOWER_SEEDS).first();

            case HERB:
                return Inventory.stream().id(C_Items.HERB_SEEDS).first();

            case HOPS:
                return Inventory.stream().id(C_Items.HOPS_SEEDS).first();

            case FRUIT_TREE:
                return Inventory.stream().id(C_Items.FRUIT_TREE_SAPLINGS).first();

            case TREE:
                return Inventory.stream().id(C_Items.TREE_SAPLINGS).first();

            default:
                LOGGER.warning("getSeed() did not recognize patch: " + patch);
                return Inventory.nil();
        }
    }

    public int getCompostId(Patch patch) {
        try {
            int compostId;
            String compostName = "";
            switch (patch) {
                case HERB:
                    compostName = farmOptions.getPatchCompostMap().get(Patch.HERB);
                    break;

                case FLOWER:
                    compostName = farmOptions.getPatchCompostMap().get(Patch.FLOWER);
                    break;

                case HOPS:
                    compostName = farmOptions.getPatchCompostMap().get(Patch.HOPS);
                    break;

                case ALLOTMENT:
                    compostName = farmOptions.getPatchCompostMap().get(Patch.ALLOTMENT);
                    break;
            }

            switch (compostName.toLowerCase()) {
                case "compost":
                    compostId = C_Items.COMPOST;
                    break;

                case "supercompost":
                    compostId = C_Items.SUPERCOMPOST;
                    break;

                case "ultracompost":
                    compostId = C_Items.ULTRACOMPOST;
                    break;

                case "none": default:
                    return 0;
            }

            if (farmOptions.isUsingBLB()) {
                switch (compostId) {
                    case C_Items.COMPOST:
                        if (farmSupplies.getBLBCompostType().equals("Compost")) {
                            compostId = C_Items.BOTTOMLESS_BUCKET;
                        }
                        break;

                    case C_Items.SUPERCOMPOST:
                        if (farmSupplies.getBLBCompostType().equals("Supercompost")) {
                            compostId = C_Items.BOTTOMLESS_BUCKET;
                        }
                        break;

                    case C_Items.ULTRACOMPOST:
                        if (farmSupplies.getBLBCompostType().equals("Ultracompost")) {
                            compostId = C_Items.BOTTOMLESS_BUCKET;
                        }
                        break;
                }
            }
            return compostId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean canDoRun(FarmRun farmRun) {
        switch (farmRun) {
            case FRUIT_TREE_RUN:
                return canDoFruitTreeRun();

            case HOPS_RUN:
                return canDoHopsRun();

            case HERB_RUN:
                return canDoHerbRun();

            case TREE_RUN:
                return canDoTreeRun();
        }
        return false;
    }

    public boolean canDoRun(Patch patch) {
        switch (patch) {
            case FRUIT_TREE:
                return canDoFruitTreeRun();

            case HOPS:
                return canDoHopsRun();

            case HERB: case ALLOTMENT: case FLOWER:
                return canDoHerbRun();

            case TREE:
                return canDoTreeRun();
        }
        return false;
    }

    public boolean canDoARun() {
        if (canDoHerbRun()) {
            return true;
        }

        if (canDoFruitTreeRun()) {
            return true;
        }

        if (canDoHopsRun()) {
            return true;
        }

        return canDoTreeRun();
    }

    public boolean isHerbRunSuspended() {
        return(!farmOptions.getPatchActiveMap().get(Patch.HERB) ||
                Skills.realLevel(Constants.SKILLS_FARMING) < 9 ||
                isSuspended(Patch.HERB))
                &&
                (!farmOptions.getPatchActiveMap().get(Patch.ALLOTMENT) ||
                        isSuspended(Patch.ALLOTMENT));
    }

    public boolean canDoHerbRun() {
        if (isHerbRunSuspended()) {
            return false;
        }

        if (farmOptions.getPatchActiveMap().containsKey(Patch.HERB) &&
                Skills.realLevel(Constants.SKILLS_FARMING) >= 9) {
            return true;
        }
        if (farmOptions.getPatchActiveMap().containsKey(Patch.ALLOTMENT)) {
            return true;
        }

        return (farmOptions.getPatchActiveMap().containsKey(Patch.FLOWER) &&
                Skills.realLevel(Constants.SKILLS_FARMING) >= 2);
    }

    public boolean canDoFruitTreeRun() {
        return(farmOptions.getPatchActiveMap().containsKey(Patch.FRUIT_TREE) &&
                !isSuspended(Patch.FRUIT_TREE));
    }

    public boolean canDoTreeRun() {
        return (farmOptions.getPatchActiveMap().containsKey(Patch.TREE) &&
                Skills.realLevel(Constants.SKILLS_FARMING) >= 15 &&
                !isSuspended(Patch.TREE));
    }

    public boolean canDoHopsRun() {
        return (farmOptions.getPatchActiveMap().containsKey(Patch.HOPS) &&
                Skills.realLevel(Constants.SKILLS_FARMING) >= 3 &&
                !isSuspended(Patch.HOPS));
    }

    public boolean isSuspended(Patch patch) {
        return patchSuspendedMap.containsKey(patch);
    }

    public void unSuspend(Patch patch) {
        LOGGER.info("un suspending patch: " + patch);
        patchSuspendedMap.remove(patch);
    }

    public void suspendPatch(Patch patch, int level) {
        if (patchSuspendedMap.containsKey(patch)) {
            patchSuspendedMap.put(patch, level);
        } else {
            patchSuspendedMap.replace(patch, level);
        }
        LOGGER.info("suspended " + patch + " until level " + level + " farming...");
    }

    public void suspendPatchTillNextUnlock(Patch patch) {
        int level = 100;

        int[] items;
        switch (patch) {
            case ALLOTMENT:
                items = C_Items.ALLOTMENT_SEEDS;
                break;

            case FLOWER:
                items = C_Items.FLOWER_SEEDS;
                break;

            case HERB:
                items = C_Items.HERB_SEEDS;
                break;

            case HOPS:
                items = C_Items.HOPS_SEEDS;
                break;

            case FRUIT_TREE:
                items = C_Items.FRUIT_TREE_SAPLINGS;
                break;

            case TREE:
                items = C_Items.TREE_SAPLINGS;
                break;

            default:
                Script.stopScript("runTracker#suspendPatchTillNextUnlock did not recognize patch: " + patch);
                return;
        }


        for (int i : items) {
            if (Skills.realLevel(Constants.SKILLS_FARMING) < getLevelUpMap(patch).get(i)) {
                level = getLevelUpMap(patch).get(i);
                break;
            }
        }
        suspendPatch(patch, level);
    }

    public boolean anyFarmRunReady() {
        for (FarmRun farmRun : FarmRun.values()) {
            if (farmRunReady(farmRun)) {
                return true;
            }
        }
        return false;
    }

    public int getSeedsPerPatch(Patch patch, String produce) {
        switch (patch) {
            case HERB: case FLOWER: case FRUIT_TREE: case TREE:
                return 1;

            case ALLOTMENT:
                return 3;

            case HOPS:
                return (produce.equals("Jute") ? 3 : 4);

            default:
                Script.stopScript("GetFarmingItems#getSeedsPerPatch did not recognize patch: " + patch);
                return -1;
        }
    }

    public int getSeedId(Patch patch, String produce) {
        String[] strings;
        int index = 0;
        switch (patch) {
            case ALLOTMENT:
                strings = new String[] {"Potato", "Onion", "Cabbage", "Tomato", "Sweetcorn", "Strawberry", "Watermelon", "Snape grass"};
                for (int i : C_Items.ALLOTMENT_SEEDS) {
                    if (produce.equals(strings[index++])) {
                        return i;
                    }
                }
                break;

            case HERB:
                strings = new String[] {"Guam","Marrentill","Tarromin","Harralander","Ranarr","Toadflax","Irit","Avantoe","Kwuarm","Snapdragon",
                        "Cadantine","Lantadyme","Dwarf weed","Torstol"};
                for (int i : C_Items.HERB_SEEDS) {
                    if (produce.equals(strings[index++])) {
                        return i;
                    }
                }
                break;

            case FRUIT_TREE:
                strings = new String[] {"Apple tree", "Banana tree", "Orange tree", "Curry tree", "Pineapple tree", "Papaya tree", "Palm tree", "Dragonfruit tree"};
                for (int i : C_Items.FRUIT_TREE_SAPLINGS) {
                    if (produce.equals(strings[index++])) {
                        return i;
                    }
                }
                break;

            case HOPS:
                strings = new String[] {"Barley", "Hammerstone", "Asgarnian", "Jute", "Yanillian", "Krandorian", "Wildblood"};
                for (int i : C_Items.HOPS_SEEDS) {
                    if (produce.equals(strings[index++])) {
                        return i;
                    }
                }
                break;

            case FLOWER:
                strings = new String[] {"Marigold","Rosemary","Nasturtium","Woad leaf","Limpwurt","White lily"};
                for (int i : C_Items.FLOWER_SEEDS) {
                    if (produce.equals(strings[index++])) {
                        return i;
                    }
                }
                break;

            case TREE:
                strings = new String[] {"Oak Tree", "Willow Tree", "Maple Tree", "Yew Tree", "Magic Tree"};
                for (int i : C_Items.TREE_SAPLINGS) {
                    if (produce.equals(strings[index++])) {
                        return i;
                    }
                }
                break;
        }
        return -1;
    }

    public void initDatabaseValues() {
        long time = System.currentTimeMillis();
        String pbName = Preferences.getString("username");

        String[] dbValues;
        Database.getAccountId("farming_script", pbName);

        // produce settings
        dbValues = Database.getValues("farming_script", "herb_settings").split(":");
        patchNextHarvestMap.put(Patch.HERB, Long.parseLong(dbValues[4]));

        dbValues = Database.getValues("farming_script", "flower_settings").split(":");
        patchNextHarvestMap.put(Patch.FLOWER, Long.parseLong(dbValues[4]));

        dbValues = Database.getValues("farming_script", "allotment_settings").split(":");
        patchNextHarvestMap.put(Patch.ALLOTMENT, Long.parseLong(dbValues[4]));

        dbValues = Database.getValues("farming_script", "hop_settings").split(":");
        patchNextHarvestMap.put(Patch.HOPS, Long.parseLong(dbValues[4]));

        dbValues = Database.getValues("farming_script", "fruit_tree_settings").split(":");
        patchNextHarvestMap.put(Patch.FRUIT_TREE, Long.parseLong(dbValues[4]));

        dbValues = Database.getValues("farming_script", "tree_settings").split(":");
        patchNextHarvestMap.put(Patch.TREE, Long.parseLong(dbValues[4]));

        for (Map.Entry<Patch, Long> map : patchNextHarvestMap.entrySet()) {
            LOGGER.info(map.toString());
        }

        LOGGER.info("finished caching settings (" + (System.currentTimeMillis() - time) + " ms)");
    }

    public boolean farmRunReady(FarmRun farmRun) {
        switch (farmRun) {
            case FRUIT_TREE_RUN:
                return System.currentTimeMillis() >= patchNextHarvestMap.get(Patch.FRUIT_TREE);

            case HOPS_RUN:
                return System.currentTimeMillis() >= patchNextHarvestMap.get(Patch.HOPS);

            case TREE_RUN:
                return System.currentTimeMillis() >= patchNextHarvestMap.get(Patch.TREE);

            case HERB_RUN:
                return System.currentTimeMillis() >= patchNextHarvestMap.get(farmOptions.getHerbRunPatch());

            case NIL:
                return false;

            default:
                Script.stopScript("FarmingSettings#farmRunReady did not recognize farm run: " + farmRun);
                return false;
        }
    }

    public long getNextHarvest(FarmRun farmRun) {
        switch (farmRun) {
            case FRUIT_TREE_RUN:
                return patchNextHarvestMap.get(Patch.FRUIT_TREE);

            case HOPS_RUN:
                return patchNextHarvestMap.get(Patch.HOPS);

            case TREE_RUN:
                return patchNextHarvestMap.get(Patch.TREE);

            case HERB_RUN:
                return patchNextHarvestMap.get(farmOptions.getHerbRunPatch());

            case NIL:
                return Long.MAX_VALUE;

            default:
                Script.stopScript("FarmingSettings#getNextHarvest did not recognize farm run: " + farmRun);
                return -1;
        }
    }

    public void setNextHarvest(Patch patch) {
        long nextHarvest = getStages(patch, getSeedId(patch, farmOptions.getPatchProduceMap().get(patch))) * getGrowthTime(patch) + System.currentTimeMillis();
        LOGGER.info("next harvest = " + nextHarvest);
        LOGGER.info(" delta between next harvest and current time: " + (nextHarvest - System.currentTimeMillis()));
        Database.setNextHarvest(patch, Long.toString(nextHarvest));
        patchNextHarvestMap.put(patch, nextHarvest);
    }

    private int getGrowthTime(Patch patch) {
        switch (patch) {
            case HERB:
                return 1200000;

            case FLOWER:
                return 300000;

            case ALLOTMENT: case HOPS:
                return 600000;

            case FRUIT_TREE:
                return 9600000;

            case TREE:
                return 2400000;

            case CACTUS:
                return 4800000;

            default:
                Script.stopScript("FarmUtil#getGrowthTime doesn't recognize patch: " + patch);
                return -1;
        }
    }

    private int getStages(Patch patch, int seed) {
        switch (patch) {
            case ALLOTMENT:
                switch (seed) {
                    case C_Items.WATERMELON_SEED:
                        return 8;
                    case C_Items.SNAPE_GRASS_SEED:
                        return 7;
                    case C_Items.SWEETCORN_SEED: case C_Items.STRAWBERRY_SEED:
                        return 6;
                    default:
                        return 4;
                }

            case HOPS:
                switch (seed) {
                    case C_Items.ASGARNIAN_SEED: case C_Items.JUTE_SEED:
                        return 5;
                    case C_Items.YANILLIAN_SEED:
                        return 6;
                    case C_Items.KRANDORIAN_SEED:
                        return 7;
                    case C_Items.WILDBLOOD_SEED:
                        return 8;
                    default:
                        return 4;
                }

            case FRUIT_TREE:
                return 6;

            case TREE:
                switch (seed) {
                    case C_Items.OAK_SAPLING:
                        return 5;

                    case C_Items.WILLOW_SAPLING:
                        return 7;

                    case C_Items.MAPLE_SAPLING:
                        return 8;

                    case C_Items.YEW_SAPLING:
                        return 10;

                    case C_Items.MAGIC_SAPLING:
                        return 12;
                }

            case HERB: case FLOWER:
                return 4;

            default:
                Script.stopScript("FarmUtil#getStages doesn't recognize patch: " + patch);
                return -1;
        }
    }

    public FarmArea getFarmAreaYouAreAt() {
        for (FarmArea farmArea : FarmArea.values()) {
            if (AreaUtil.contains(farmArea.getArea(), Players.local())) {
                return farmArea;
            }
        }
        return FarmArea.NONE;
    }

    public ArrayList<Component> getComponentsToDeposit() {
        ArrayList<Component> components = new ArrayList<>();

        if (!Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_BUCKET, C_Components.FARMING_SUB_AMOUNT).text().equals("0")) {
            components.add(Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_BUCKET));
        }
        if (!Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_COMPOST, C_Components.FARMING_SUB_AMOUNT).text().equals("0")) {
            components.add(Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_COMPOST));
        }
        if (!Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_SUPER_COMPOST, C_Components.FARMING_SUB_AMOUNT).text().equals("0")) {
            components.add(Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_SUPER_COMPOST));
        }
        if (!Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_ULTRA_COMPOST, C_Components.FARMING_SUB_AMOUNT).text().equals("0")) {
            components.add(Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_ULTRA_COMPOST));
        }
        if (!farmOptions.isUsingBLB() &&
                !Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_BL, C_Components.FARMING_SUB_AMOUNT).text().equals("0")) {
            components.add(Widgets.component(C_Widgets.FARMING_EQUIPMENT_STORE_INVENTORY, C_Components.FARMING_EQUIPMENT_INVENTORY_BL));
        }
        return components;
    }

    public Map<Integer, Integer> getLevelUpMap(Patch patch) {
        switch (patch) {
            case TREE:
                return treeLevelMap;

            case HOPS:
                return hopsLevelMap;

            case HERB:
                return herbLevelMap;

            case FLOWER:
                return flowerLevelMap;

            case ALLOTMENT:
                return allotmentLevelMap;

            case FRUIT_TREE:
                return fruitTreeLevelMap;

            default:
                Script.stopScript("FarmUtil#getLevelUpMap doesn't recognize patch: " + patch);
                return null;
        }
    }

    public HashMap<Integer, Item> getProtectMap() {
        return protectMap;
    }

    public HashMap<Patch, Long> getPatchNextHarvestMap() {
        return patchNextHarvestMap;
    }

    public HashMap<Patch, Integer> getPatchSuspendedMap() {
        return patchSuspendedMap;
    }
}
