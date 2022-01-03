package alphonse.scripts.farm.runner;

import alphonse.scripts.Script;
import alphonse.tasks.CacheBank;
import alphonse.tasks.farming.*;
import alphonse.util.Drawer;
import alphonse.util.constants.C_Items;
import alphonse.util.debug.Debugger;
import alphonse.util.debug.FarmRunnerDebugger;
import alphonse.util.farming.FarmArea;
import alphonse.util.farming.FarmRun;
import alphonse.util.farming.Patch;
import alphonse.util.items.Items;
import alphonse.util.teleport.TeleportOption;
import com.google.common.eventbus.Subscribe;
import org.powbot.api.*;
import org.powbot.api.event.RenderEvent;
import org.powbot.api.event.SkillLevelUpEvent;
import org.powbot.api.rt4.Chat;
import org.powbot.api.rt4.Constants;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Skills;
import org.powbot.api.rt4.walking.model.Skill;
import org.powbot.api.script.*;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.mobile.drawing.Graphics;
import org.powbot.mobile.service.ScriptUploader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;
import static alphonse.util.farming.FarmSupplies.farmSupplies;
import static alphonse.util.farming.FarmOptions.farmOptions;
import static alphonse.util.farming.FarmUtil.farmUtil;

@ScriptManifest(
        name = "Alphonso's FarmRunner",
        category = ScriptCategory.Farming,
        description = "does farm runs",
        version = "0.0.1")

@ScriptConfiguration.List(
        {
                @ScriptConfiguration(
                        name = "HerbRun",
                        description = "do herb runs? includes (optionally) allotments and flowers",
                        optionType = OptionType.BOOLEAN,
                        defaultValue = "true"
                ),
                    @ScriptConfiguration(
                            name = "Patch To Wait For",
                            optionType = OptionType.STRING,
                            description = "which produce should be ready before starting new herb run?",
                            allowedValues = {"Herbs (80 min)", "Allotments (40-80 min)", "Flowers (20 min)"},
                            defaultValue = "Herbs (80 min)",
                            visible = false
                    ),
                    @ScriptConfiguration(
                            name = "Herbs",
                            optionType = OptionType.STRING,
                            description = "what herbs do you want to plant?",
                            allowedValues = {"None","Guam","Marrentill","Tarromin","Harralander","Ranarr","Toadflax","Irit","Avantoe","Kwuarm","Snapdragon",
                                    "Cadantine","Lantadyme","Dwarf weed","Torstol"},
                            defaultValue = "Toadflax",
                            visible = false
                    ),
                        @ScriptConfiguration(
                                name = "Herb Compost",
                                optionType = OptionType.STRING,
                                description = "what compost to use for herbs?",
                                allowedValues = {"None", "Compost", "Supercompost", "Ultracompost"},
                                defaultValue = "Ultracompost",
                                visible = false
                        ),
                    @ScriptConfiguration(
                            name = "Flowers",
                            optionType = OptionType.STRING,
                            description = "what flowers do you want to plant?",
                            allowedValues = {"None","Marigold","Rosemary","Nasturtium","Woad leaf","Limpwurt","White lily"},
                            defaultValue = "Limpwurt",
                            visible = false
                    ),
                        @ScriptConfiguration(
                                name = "Flower Compost",
                                optionType = OptionType.STRING,
                                description = "what compost to use for flowers?",
                                allowedValues = {"None", "Compost", "Supercompost", "Ultracompost"},
                                defaultValue = "Ultracompost",
                                visible = false
                        ),
                    @ScriptConfiguration(
                            name = "Allotments",
                            optionType = OptionType.STRING,
                            description = "what allotments do you want to plant?",
                            allowedValues = {"None","Potato", "Onion", "Cabbage", "Tomato", "Sweetcorn", "Strawberry", "Watermelon", "Snape grass"},
                            defaultValue = "Strawberry",
                            visible = false
                    ),
                        @ScriptConfiguration(
                                name = "Allotment Compost",
                                optionType = OptionType.STRING,
                                description = "what compost to use for allotments?",
                                allowedValues = {"None", "Compost", "Supercompost", "Ultracompost"},
                                defaultValue = "Ultracompost",
                                visible = false
                        ),
                @ScriptConfiguration(
                        name = "HopsRun",
                        description = "do hops run?",
                        optionType = OptionType.BOOLEAN,
                        defaultValue = "true"
                ),
                    @ScriptConfiguration(
                            name = "Hops",
                            optionType = OptionType.STRING,
                            description = "what hops do you want to plant?",
                            allowedValues = {"None","Barley", "Hammerstone", "Asgarnian", "Jute", "Yanillian", "Krandorian", "Wildblood"},
                            defaultValue = "Yanillian",
                            visible = false
                    ),
                        @ScriptConfiguration(
                                name = "Hops Compost",
                                optionType = OptionType.STRING,
                                description = "what compost to use for hops?",
                                allowedValues = {"None", "Compost", "Supercompost", "Ultracompost"},
                                defaultValue = "Ultracompost",
                                visible = false
                        ),
                @ScriptConfiguration(
                        name = "FruitTreeRun",
                        description = "do fruit tree runs?",
                        optionType = OptionType.BOOLEAN,
                        defaultValue = "true"
                ),
                    @ScriptConfiguration(
                            name = "FruitTrees",
                            optionType = OptionType.STRING,
                            description = "what fruit trees do you want to plant?",
                            allowedValues = {"None","Apple tree", "Banana tree", "Orange tree", "Curry tree", "Pineapple tree", "Papaya tree", "Palm tree", "Dragonfruit tree"},
                            defaultValue = "Apple tree",
                            visible = false
                    ),
                @ScriptConfiguration(
                        name = "TreeRun",
                        description = "do tree runs?",
                        optionType = OptionType.BOOLEAN,
                        defaultValue = "true"
                ),
                    @ScriptConfiguration(
                            name = "Trees",
                            optionType = OptionType.STRING,
                            description = "what trees do you want to plant?",
                            allowedValues = {"None","Oak Tree", "Willow Tree", "Maple Tree", "Yew Tree", "Magic Tree"},
                            defaultValue = "Oak Tree",
                            visible = false
                    ),
                @ScriptConfiguration(
                        name = "Spell Option",
                        optionType = OptionType.STRING,
                        description = "how would you like to teleport",
                        allowedValues = {"Use Tabs", "Use Spells"},
                        defaultValue = "Use Tabs"
                ),

        }
)

public class FarmRunner extends Script {

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart(
                "FarmRunner", "gutizia@outlook.com", "emulator-5554", true, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        initFarmOptions();
        farmUtil.initDatabaseValues();

        if (Skills.realLevel(Constants.SKILLS_FARMING) < 2) {
            farmUtil.suspendPatchTillNextUnlock(Patch.FLOWER);
        }
        if (Skills.realLevel(Constants.SKILLS_FARMING) < 3) {
            farmUtil.suspendPatchTillNextUnlock(Patch.HOPS);
        }
        if (Skills.realLevel(Constants.SKILLS_FARMING) < 9) {
            farmUtil.suspendPatchTillNextUnlock(Patch.HERB);
        }
        if (Skills.realLevel(Constants.SKILLS_FARMING) < 15) {
            farmUtil.suspendPatchTillNextUnlock(Patch.TREE);
        }
        if (Skills.realLevel(Constants.SKILLS_FARMING) < 27) {
            farmUtil.suspendPatchTillNextUnlock(Patch.FRUIT_TREE);
        }
        Debugger.setDebugging(true);

        Paint p = new PaintBuilder()
                .trackSkill(Skill.Farming)
                .trackInventoryItem(C_Items.GRIMY_TOADFLAX)
                .trackInventoryItem(C_Items.YANILLIAN)
                .build();
        addPaint(p);
    }

    @Override
    public void poll() {
        super.poll();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void initStandardTasks() {
        standardTasks = new ArrayList<>();

        standardTasks.add(new CacheBank());
        standardTasks.add(new GetNewRun());

        standardTasks.add(new HerbRun());
        standardTasks.add(new HopsRun());
        standardTasks.add(new FruitTreeRun());
        standardTasks.add(new TreeRun());

        standardTasks.add(new LogoutAndWait());
    }

    @Subscribe
    public void onRender(RenderEvent r) {
        Graphics g = r.getGraphics();
        g.setScale(1.0f);
        g.setColor(Color.getGREEN());

        // as to not produce NPE
        if (!farmUtil.getPatchNextHarvestMap().containsKey(Patch.TREE)) {
            return;
        }
        if (Game.loggedIn()) {
            Drawer.drawStrings(g, new Point(380, 160), Arrays.asList(
                    "current run: " + farmRunTracker.getCurrentRun(),
                    "next run: " + farmRunTracker.getNextRun()
            ));

            if (!farmRunTracker.getRequiredItems().equals(Items.NIL)) {
                Drawer.drawStrings(g, new Point(650, 20), farmRunTracker.getRequiredItems().toStrings());
            }

            if (!farmRunTracker.getPatchOrder().isEmpty()) {
                Drawer.drawStrings(g, new Point(52, 250), Arrays.asList(
                        "patch order: ",
                        farmRunTracker.getPatchOrder().toString()
                ));
            }
            /*
            int i = 0;
            for (FarmRun farmRun : FarmRun.values()) {
                if (farmRun.equals(FarmRun.NIL)) continue;
                FarmRunnerDebugger.drawFarmRunInfo(g, new Point(400, 100 + (i++ * 90)), farmRun);
            }*/

            if (farmRunTracker.getCurrentPatch() != null && !farmUtil.getFarmAreaYouAreAt().equals(FarmArea.NONE)) {
                farmUtil.getFarmAreaYouAreAt().draw(g);
            }
        } else {
            FarmRunnerDebugger.drawFarmRunInfo(g, new Point(340, 140), farmRunTracker.getNextRun());
        }
    }

    public static Point getActualBasePoint(Tile tile) {
        return new Point(tile.matrix().bounds().getBounds().getX(),
                tile.matrix().bounds().getBounds().getY() + tile.matrix().bounds().getBounds().getHeight());
    }

    private void initFarmOptions() {
        if (getOption("HerbRun")) {
            if (((String)getOption("Patch To Wait For")).toLowerCase().contains("herb")) {
                farmOptions.setHerbRunPatch(Patch.HERB);

            } else if (((String)getOption("Patch To Wait For")).toLowerCase().contains("flower")) {
                farmOptions.setHerbRunPatch(Patch.FLOWER);

            } else {
                farmOptions.setHerbRunPatch(Patch.ALLOTMENT);
            }

            farmOptions.addPatchActive(Patch.HERB, !getOption("Herbs").equals("None"));
            farmOptions.getPatchProduceMap().put(Patch.HERB, getOption("Herbs"));
            farmOptions.getPatchCompostMap().put(Patch.HERB, getOption("Herb Compost"));

            farmOptions.addPatchActive(Patch.FLOWER, !getOption("Flowers").equals("None"));
            farmOptions.getPatchProduceMap().put(Patch.FLOWER, getOption("Flowers"));
            farmOptions.getPatchCompostMap().put(Patch.FLOWER, getOption("Flower Compost"));

            farmOptions.addPatchActive(Patch.ALLOTMENT, !getOption("Allotments").equals("None"));
            farmOptions.getPatchProduceMap().put(Patch.ALLOTMENT, getOption("Allotments"));
            farmOptions.getPatchCompostMap().put(Patch.ALLOTMENT, getOption("Allotment Compost"));

            if (((String)getOption("Spell Option")).toLowerCase().contains("tabs")) {
                farmOptions.setSpellOptionPreference(TeleportOption.TAB);
            } else {
                farmOptions.setSpellOptionPreference(TeleportOption.SPELL);
            }
        }
        if (getOption("HopsRun")) {
            LOGGER.info("hops run");
            farmOptions.addPatchActive(Patch.HOPS, !getOption("Hops").equals("None"));
            farmOptions.getPatchProduceMap().put(Patch.HOPS, getOption("Hops"));
            farmOptions.getPatchCompostMap().put(Patch.HOPS, getOption("Hops Compost"));
        }
        if (getOption("FruitTreeRun")) {
            LOGGER.info("fruit tree run");
            farmOptions.addPatchActive(Patch.FRUIT_TREE, !getOption("FruitTrees").equals("None"));
            farmOptions.getPatchProduceMap().put(Patch.FRUIT_TREE, getOption("FruitTrees"));
        }
        if (getOption("TreeRun")) {
            LOGGER.info("tree run");
            farmOptions.addPatchActive(Patch.TREE, !getOption("Trees").equals("None"));
            farmOptions.getPatchProduceMap().put(Patch.TREE, getOption("Trees"));
        }
    }

    @Subscribe
    public void onLevelUp(SkillLevelUpEvent event) {
        LOGGER.info("leveled up");
        if (event.getSkill().equals(Skill.Farming)) {
            ArrayList<Patch> toUnsuspend = new ArrayList<>();
            for (Map.Entry<Patch, Integer> set : farmUtil.getPatchSuspendedMap().entrySet()) {
                if (event.getNewLevel() >= set.getValue()) {
                    toUnsuspend.add(set.getKey());
                }
            }
            for (Patch p : toUnsuspend) {
                farmUtil.unSuspend(p);
            }
        }
    }

    @ValueChanged(keyName = "HerbRun")
    public void herbRunUpdated(boolean value) {
        updateVisibility("Herbs", value);
        updateVisibility("Flowers", value);
        updateVisibility("Allotments", value);
        updateVisibility("Patch To Wait For", value);
    }

    @ValueChanged(keyName = "Herbs")
    public void herbsUpdated(String updatedValue) {
        updateVisibility("Herb Compost", !updatedValue.equals("None"));
    }

    @ValueChanged(keyName = "Flowers")
    public void flowersUpdated(String updatedValue) {
        updateVisibility("Flower Compost", !updatedValue.equals("None"));
    }

    @ValueChanged(keyName = "Allotments")
    public void allotmentsUpdated(String updatedValue) {
        updateVisibility("Allotment Compost", !updatedValue.equals("None"));
    }

    @ValueChanged(keyName = "HopsRun")
    public void hopsRunUpdated(boolean value) {
        updateVisibility("Hops", value);
    }

    @ValueChanged(keyName = "Hops")
    public void hopsUpdated(String updatedValue) {
        updateVisibility("Hops Compost", !updatedValue.equals("None"));
    }

    @ValueChanged(keyName = "FruitTreeRun")
    public void fruitTreeRunUpdated(boolean value) {
        updateVisibility("FruitTrees", value);
    }

    @ValueChanged(keyName = "TreeRun")
    public void treeRunUpdated(boolean value) {
        updateVisibility("Trees", value);
    }
}
