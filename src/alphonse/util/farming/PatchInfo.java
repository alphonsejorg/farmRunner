package alphonse.util.farming;
import alphonse.util.Drawer;
import org.powbot.api.Area;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Objects;
import org.powbot.mobile.drawing.Graphics;

import java.util.Arrays;

import static alphonse.util.farming.FarmRunTracker.farmRunTracker;

public class PatchInfo {
    private final Patch patch;
    private final Area area;
    private PatchStage patchStage = PatchStage.NONE;

    public PatchInfo(Patch patch, Area area) {
        this.area = area;
        this.patch = patch;
    }

    public void draw(Graphics g) {
        if (farmRunTracker.getCurrentPatch().equals(this)) {
            Drawer.drawStrings(g, getArea().getCentralTile().matrix().centerPoint(), Arrays.asList(
                    "Patch: " + patch,
                    "Stage: " + patchStage
            ));
        }
    }

    public Patch getPatch() {
        return patch;
    }

    public GameObject getObject() {
        return Objects.stream().within(area).action("Guide").nearest().first();
    }

    public void setPatchStage(PatchStage patchStage) {
        this.patchStage = patchStage;
    }

    public PatchStage getPatchStage() {
        return patchStage;
    }

    public Area getArea() {
        return area;
    }
}
