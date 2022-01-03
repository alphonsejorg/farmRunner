package alphonse.util.debug;

import alphonse.scripts.Script;
import alphonse.util.Drawer;
import alphonse.util.farming.FarmRun;
import alphonse.util.farming.Patch;
import org.powbot.api.Point;
import org.powbot.mobile.drawing.Graphics;

import java.util.Arrays;

import static alphonse.util.farming.FarmOptions.farmOptions;
import static alphonse.util.farming.FarmSupplies.farmSupplies;
import static alphonse.util.farming.FarmUtil.farmUtil;

public class FarmRunnerDebugger extends Debugger {



    @Override
    public void draw(Graphics g) {

    }

    public static void drawLeprechaunTools(Graphics g, Point point) {
        Drawer.drawStrings(g, point, Arrays.asList(
                "TOOLS AT LEPRECHAUN",
                "buckets: " + farmSupplies.getEmptyBuckets(),
                "normal compost: " + farmSupplies.getNormalCompost(),
                "super compost: " + farmSupplies.getSuperCompost(),
                "ultra compost: " + farmSupplies.getUltraCompost(),
                "spade: " + farmSupplies.getSpade(),
                "rake: " + farmSupplies.getRake(),
                "seed dibber: " + farmSupplies.getSeedDibber()
        ));
    }

    public static void drawFarmRunInfo(Graphics g, Point point, FarmRun farmRun) {
        Patch patch;
        switch (farmRun) {
            case HERB_RUN:
                patch = farmOptions.getHerbRunPatch();
                break;

            case TREE_RUN:
                patch = Patch.TREE;
                break;

            case HOPS_RUN:
                patch = Patch.HOPS;
                break;

            case FRUIT_TREE_RUN:
                patch = Patch.FRUIT_TREE;
                break;

                default:
                Script.stopScript("FarmRunnerDebugger did not recognize farmRun: " + farmRun);
                return;
        }

        if (farmOptions.getPatchActiveMap().containsKey(patch) &&
                farmOptions.getPatchActiveMap().get(patch))
        Drawer.drawStrings(g, point, Arrays.asList(
                farmRun.toString(),
                "can do run: " + farmUtil.canDoRun(patch),
                "is suspended: " + (farmUtil.isSuspended(patch) ? "true (until lvl " + farmUtil.getPatchSuspendedMap().get(patch) + ")" : "false"),
                "is ready: " + (farmUtil.getPatchNextHarvestMap().get(patch) < System.currentTimeMillis() ?
                        "true" : "false (ready in " + ((farmUtil.getPatchNextHarvestMap().get(patch) - System.currentTimeMillis()) / 1000) + " seconds)")
        ));
    }
}
