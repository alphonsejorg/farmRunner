package alphonse.util.farming;

import alphonse.util.teleport.Teleport;
import org.powbot.api.Area;
import org.powbot.api.Tile;
import org.powbot.mobile.drawing.Graphics;

import java.util.*;
import java.util.List;


public enum FarmArea {
    FALADOR_HAF(new Area(new Tile(3046,3300,0),new Tile(3063,3316,0)),
            Arrays.asList(new PatchInfo(Patch.HERB, new Area(new Tile(3058,3311,0),new Tile(3060,3313,0))),
                    new PatchInfo(Patch.FLOWER, new Area(new Tile(3054,3307,0),new Tile(3056,3309,0))),
                    new PatchInfo(Patch.ALLOTMENT, new Area(new Tile(3050, 3307), new Tile(3052, 3311))),
                    new PatchInfo(Patch.ALLOTMENT, new Area(new Tile(3055, 3303), new Tile(3058, 3305)))),
            Arrays.asList(Teleport.FALADOR)),

    CATHERBY_HAF(new Area(new Tile(2802,3457,0),new Tile(2820,3475,0)),
            Arrays.asList(new PatchInfo(Patch.HERB, new Area(new Tile(2813,3463,0),new Tile(2815,3465,0))),
                    new PatchInfo(Patch.FLOWER, new Area(new Tile(2809,3463,0),new Tile(2811,3465,0))),
                    new PatchInfo(Patch.ALLOTMENT, new Area(new Tile(2807, 3467), new Tile(2815, 3469))),
                    new PatchInfo(Patch.ALLOTMENT, new Area(new Tile(2807, 3459), new Tile(2815, 3461)))),
            Arrays.asList(Teleport.CAMELOT)),

    ARDOUGNE_HAF(new Area(new Tile(2659,3367,0),new Tile(2675,3385,0)),
            Arrays.asList(new PatchInfo(Patch.HERB, new Area(new Tile(2670,3374,0),new Tile(2672,3376,0))),
                    new PatchInfo(Patch.FLOWER, new Area(new Tile(2666,3374,0),new Tile(2668,3376 ,0))),
                    new PatchInfo(Patch.ALLOTMENT, new Area( new Tile(2664, 3378), new Tile(2672, 3380))),
                    new PatchInfo(Patch.ALLOTMENT, new Area(new Tile(2664, 3370), new Tile(2672, 3372)))),
            Arrays.asList(Teleport.ARDOUGNE, Teleport.FISHING_GUILD, Teleport.RANGING_GUILD, Teleport.CAMELOT)),

    CANIFIS_HAF(new Area(new Tile(3594,3518,0),new Tile(3609,3534,0)),
            Arrays.asList(new PatchInfo(Patch.HERB, new Area(new Tile(3605,3529,0),new Tile(3607,3531,0))),
                    new PatchInfo(Patch.FLOWER, new Area(new Tile(3601,3525,0),new Tile(3603,3527,0))),
                    new PatchInfo(Patch.ALLOTMENT, new Area(new Tile(3605, 3523), new Tile(3607, 3527))),
                    new PatchInfo(Patch.ALLOTMENT, new Area(new Tile(3597, 3525), new Tile(3599, 3531)))),
            Arrays.asList(Teleport.ECTOPHIAL)),

    HOSIDIUS_HAF(new Area(new Tile(1727,3547,0),new Tile(1743,3561,0)),
            Arrays.asList(new PatchInfo(Patch.HERB, new Area(new Tile(1738,3550,0),new Tile(1740,3552,0))),
                    new PatchInfo(Patch.FLOWER, new Area(new Tile(1734,3554,0),new Tile(1736,3556,0))),
                    new PatchInfo(Patch.ALLOTMENT, new Area(new Tile(1732, 3550), new Tile(1736, 3552))),
                    new PatchInfo(Patch.ALLOTMENT, new Area(new Tile(1738, 3554), new Tile(1740, 3558)))),
            Arrays.asList(Teleport.HOSIDIUS,Teleport.WOODCUTTING_GUILD, Teleport.POH_RIMMINGTON, Teleport.FALADOR, Teleport.LUMBRIDGE)),
    FARMING_GUILD_HAF(new Area(new Tile(1223, 3719), new Tile(1241, 3739)),
            Collections.singletonList(new PatchInfo(Patch.HERB, new Area(new Tile(1238, 3726), new Tile(1239, 3727)))),
            Arrays.asList(Teleport.FARMING_GUILD)),

    CHAMP_GUILD_HOPS(new Area(new Tile(3222,3304,0),new Tile(3239,3322,0)),
            Collections.singletonList(new PatchInfo(Patch.HOPS, new Area(new Tile(3227, 3313, 0), new Tile(3232, 3318, 0)))),
            Arrays.asList(Teleport.CHAMPION_GUILD, Teleport.LUMBRIDGE, Teleport.VARROCK)),

    CAMELOT_HOPS(new Area(new Tile(2657,3514,0),new Tile(2685,3536,0)),
            Collections.singletonList(new PatchInfo(Patch.HOPS, new Area(new Tile(2664, 3523, 0), new Tile(2670, 3529, 0)))),
            Arrays.asList(Teleport.CAMELOT, Teleport.RANGING_GUILD, Teleport.ARDOUGNE)),

    YANILLE_HOPS(new Area(new Tile(2568,3095,0),new Tile(2586,3109,0)),
            Collections.singletonList(new PatchInfo(Patch.HOPS, new Area(new Tile(2574, 3103, 0), new Tile(2578, 3107, 0)))),
            Arrays.asList(Teleport.WATCHTOWER, Teleport.CASTLE_WARS, Teleport.ARDOUGNE_MONESTERY, Teleport.ARDOUGNE)),

    ENTRANA_HOPS(new Area(new Tile(2807,3332,0),new Tile(2822,3347,0)),
            Collections.singletonList(new PatchInfo(Patch.HOPS, new Area(new Tile(2809,3335),new Tile(2813,3339,0)))),
            Arrays.asList(Teleport.POH_RIMMINGTON, Teleport.FALADOR, Teleport.LUMBRIDGE)),



    GNOME_STRONGHOLD_FT(new Area(new Tile(2466,3441,0), new Tile(2482,3454,0)),
            Collections.singletonList(new PatchInfo(Patch.FRUIT_TREE, new Area(new Tile(2475, 3445, 0), new Tile(2477, 3447, 0)))),
            Arrays.asList(Teleport.OUTPOST, Teleport.FISHING_GUILD, Teleport.ARDOUGNE)),

    CATHERBY_FT(new Area(new Tile(2842,3424,0), new Tile(2866,3443,0)),
            Collections.singletonList(new PatchInfo(Patch.FRUIT_TREE, new Area(new Tile(2860,3433,0),new Tile(2862,3435,0)))),
            Arrays.asList(Teleport.CAMELOT)),

    GNOME_VILLAGE_FT(new Area(new Tile(2483,3171,0), new Tile(2498,3192,0)),
            Collections.singletonList(new PatchInfo(Patch.FRUIT_TREE, new Area(new Tile(2489,3179,0),new Tile(2491,3181,0)))),
            Arrays.asList(Teleport.ARDOUGNE_MONESTERY, Teleport.CASTLE_WARS, Teleport.ARDOUGNE)),

    BRIMHAVEN_FT(new Area(new Tile(2758,3204,0), new Tile(2775,3221,0)),
            Collections.singletonList(new PatchInfo(Patch.FRUIT_TREE, new Area(new Tile(2764,3212,0),new Tile(2766,3214,0)))),
            Arrays.asList(Teleport.ARDOUGNE)),

    LLETYA_FT(new Area(new Tile(2339, 3159, 0), new Tile(2350, 3172, 0)),
            Collections.singletonList(new PatchInfo(Patch.FRUIT_TREE, new Area(new Tile(2346, 3161, 0),new Tile(2348, 3163, 0)))),
            Arrays.asList(Teleport.TELEPORT_CRYSTAL_LLETYA)),

    FARMING_GUILD_FT(new Area(new Tile(1240, 3747, 0), new Tile(1257, 3762,0)),
            Collections.singletonList(new PatchInfo(Patch.FRUIT_TREE, new Area(new Tile(1242, 3758, 0),new Tile(1244, 3760, 0)))),
            Arrays.asList(Teleport.FARMING_GUILD)),

    LUMBRIDGE_TREE(new Area(new Tile(3187, 3221, 0), new Tile(3199, 3239, 0)),
            Collections.singletonList(new PatchInfo(Patch.TREE, new Area(new Tile(3192, 3230, 0), new Tile(3195, 3233, 0)))),
            Arrays.asList(Teleport.LUMBRIDGE, Teleport.HOME_TELEPORT)),

    VARROCK_TREE(new Area(new Tile(3224, 3452, 0), new Tile(3233, 3465, 0)),
            Collections.singletonList(new PatchInfo(Patch.TREE, new Area(new Tile(3228, 3458, 0), new Tile(3231, 3461, 0)))),
            Arrays.asList(Teleport.VARROCK)),

    FALADOR_TREE(new Area(new Tile(2997, 3369, 0), new Tile(3011, 3380, 0)),
            Collections.singletonList(new PatchInfo(Patch.TREE, new Area(new Tile(3003, 3372, 0), new Tile(3006, 3375, 0)))),
            Arrays.asList(Teleport.FALADOR)),

    TAVERLEY_TREE(new Area(new Tile(2927, 3431, 0), new Tile(2943, 3448, 0)),
            Collections.singletonList(new PatchInfo(Patch.TREE, new Area(new Tile(2935, 3437, 0), new Tile(2938, 3440, 0)))),
            Arrays.asList(Teleport.FALADOR)),

    GNOME_STRONGHOLD_TREE(new Area(new Tile(2430, 3408, 0), new Tile(2444, 3422, 0)),
            Collections.singletonList(new PatchInfo(Patch.TREE, new Area(new Tile(2435, 3414, 0), new Tile(2438, 3417, 0)))),
            Arrays.asList(Teleport.OUTPOST, Teleport.FISHING_GUILD, Teleport.ARDOUGNE)),

    FARMING_GUILD_TREE(new Area(new Tile(1228, 3721, 0), new Tile(1241, 3738, 0)),
            Collections.singletonList(new PatchInfo(Patch.TREE, new Area(new Tile(1231, 3735, 0), new Tile(1234, 3738, 0)))),
            Arrays.asList(Teleport.FARMING_GUILD)),



    NONE(new Area(Tile.getNil(), Tile.getNil()), Collections.emptyList(), Collections.emptyList());

    private Area area;
    private List<PatchInfo> patches;
    private List<Teleport> teleportOptions;

    FarmArea(Area area, List<PatchInfo> patches, List<Teleport> teleportOptions) {
        this.area = area;
        this.patches = patches;
        this.teleportOptions = teleportOptions;
    }

    @Override
    public String toString() {
        if (this.equals(NONE)) {
            return super.toString().toLowerCase();
        }

        return super.toString().substring(0, super.toString().lastIndexOf("_")).replaceAll("_", " ").toLowerCase();
    }

    public Area getArea() {
        return area;
    }

    public PatchInfo getUnfinishedPatch() {
        if (this.equals(NONE)) {
            return null;
        }
        for (PatchInfo patchInfo : patches) {
            if (!patchInfo.getPatchStage().equals(PatchStage.FINISHED)) {
                return patchInfo;
            }
        }
        return null;
    }

    public void draw(Graphics g) {
        if (this.equals(NONE)) {
            return;
        }
        for (PatchInfo patchInfo : patches) {
            patchInfo.draw(g);
        }
    }

    public List<Teleport> getTeleportOptions() {
        return teleportOptions;
    }
}