package alphonse.util.farming;

import alphonse.scripts.Script;
import alphonse.util.teleport.TeleportOption;

import java.util.HashMap;
import java.util.Map;

public class FarmOptions {
    public final static FarmOptions farmOptions = new FarmOptions();

    private boolean usingBLB = false;

    private TeleportOption spellOptionPreference = TeleportOption.NONE;

    private Map<Patch, Boolean> patchActiveMap = new HashMap<>();
    private Map<Patch, String> patchCompostMap = new HashMap<>();
    private Map<Patch, String> patchProduceMap = new HashMap<>();

    private Patch herbRunPatch = Patch.HERB;

    private FarmOptions() { }

    public boolean isUsingBLB() {
        return usingBLB;
    }

    public void setUsingBLB(boolean usingBLB) {
        this.usingBLB = usingBLB;
    }

    public void addPatchActive(Patch patch, boolean active) {
        patchActiveMap.put(patch, active);
    }

    public Map<Patch, String> getPatchCompostMap() {
        return patchCompostMap;
    }

    public TeleportOption[] getSpellOptions() {
        switch (spellOptionPreference) {
            case TAB:
                return new TeleportOption[] {TeleportOption.TAB, TeleportOption.SPELL, TeleportOption.NONE};

            case SPELL:
                return new TeleportOption[] {TeleportOption.SPELL, TeleportOption.TAB, TeleportOption.NONE};
        }
        Script.stopScript("FarmOptions#getJewelleryOptions did not recognize jewelleryOption: " + spellOptionPreference);
        return null;
    }

    public void setSpellOptionPreference(TeleportOption spellOptionPreference) {
        this.spellOptionPreference = spellOptionPreference;
    }

    public Map<Patch, Boolean> getPatchActiveMap() {
        return patchActiveMap;
    }

    public Patch getHerbRunPatch() {
        return herbRunPatch;
    }

    public void setHerbRunPatch(Patch herbRunPatch) {
        this.herbRunPatch = herbRunPatch;
    }

    public Map<Patch, String> getPatchProduceMap() {
        return patchProduceMap;
    }
}
