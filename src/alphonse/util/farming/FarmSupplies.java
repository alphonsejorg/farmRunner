package alphonse.util.farming;

import org.powbot.api.rt4.Varpbits;

public class FarmSupplies {
    public final static FarmSupplies farmSupplies = new FarmSupplies();

    private int emptyBuckets = -1;
    private int normalCompost = -1;
    private int superCompost = -1;
    private int ultraCompost = -1;
    private int spade = -1;
    private int rake = -1;
    private int seedDibber = -1;

    private String BLBCompostType = "uninitialized";
    private int BLBAmount = -1;

    private final static int FIRST_VAR = 615;
    private final static int SECOND_VAR = 439;
    private final static int THIRD_VAR = 967;
    private final static int FOURTH_VAR = 2084;
    private final static int FIFTH_VAR = 1704;

    private FarmSupplies() {}

    public void updateTools() {
        updateSpade();
        updateRake();
        updateSeedDibber();
        updateEmptyBuckets();
        updateNormalCompost();
        updateSuperCompost();
        updateUltraCompost();
    }

    public int getEmptyBuckets() {
        if (emptyBuckets == -1) {
            updateEmptyBuckets();
        }
        return emptyBuckets;
    }

    public void updateEmptyBuckets() {
        StringBuilder sb = new StringBuilder();
        for (int i = 9; i <= 13; i++) {
            sb.append(Varpbits.varpbit(FIRST_VAR) >> i & 1);
        }
        sb.reverse();
        int firstVar = Integer.parseInt(sb.toString(), 2);

        sb = new StringBuilder();
        for (int i = 10; i <= 12; i++) {
            sb.append(Varpbits.varpbit(SECOND_VAR) >> i & 1);
        }
        sb.reverse();
        int secondVar = Integer.parseInt(sb.toString(), 2);

        sb = new StringBuilder();
        for (int i = 12; i <= 13; i++) {
            sb.append(Varpbits.varpbit(THIRD_VAR) >> i & 1);
        }
        sb.reverse();
        int thirdVar = Integer.parseInt(sb.toString(), 2);

        emptyBuckets = (firstVar + (secondVar * 32) + (thirdVar * 256));
    }

    public int getNormalCompost() {
        if (normalCompost == -1) {
            updateNormalCompost();
        }
        return normalCompost;
    }

    private void updateNormalCompost() {
        StringBuilder sb = new StringBuilder();
        for (int i = 14; i <= 21; i++) {
            sb.append(Varpbits.varpbit(FIRST_VAR) >> i & 1);
        }
        sb.reverse();
        int firstVar = Integer.parseInt(sb.toString(), 2);

        sb = new StringBuilder();
        for (int i = 14; i <= 15; i++) {
            sb.append(Varpbits.varpbit(THIRD_VAR) >> i & 1);
        }
        sb.reverse();
        int thirdVar = Integer.parseInt(sb.toString(), 2);
        normalCompost = (firstVar + (thirdVar * 256));
    }

    public int getSuperCompost() {
        if (superCompost == -1) {
            updateSuperCompost();
        }
        return superCompost;
    }

    private void updateSuperCompost() {
        StringBuilder sb = new StringBuilder();
        for (int i = 22; i <= 29; i++) {
            sb.append(Varpbits.varpbit(FIRST_VAR) >> i & 1);
        }
        sb.reverse();
        int firstVar = Integer.parseInt(sb.toString(), 2);

        sb = new StringBuilder();
        for (int i = 16; i <= 17; i++) {
            sb.append(Varpbits.varpbit(THIRD_VAR) >> i & 1);
        }
        sb.reverse();
        int thirdVar = Integer.parseInt(sb.toString(), 2);

        superCompost = (firstVar + (thirdVar * 256));
    }

    public int getUltraCompost() {
        if (ultraCompost == -1) {
            updateUltraCompost();
        }
        return ultraCompost;
    }

    private void updateUltraCompost() {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i <= 11; i++) {
            sb.append(Varpbits.varpbit(THIRD_VAR) >> i & 1);
        }
        sb.reverse();
        ultraCompost = Integer.parseInt(sb.toString(), 2);
    }

    public int getSeedDibber() {
        if (seedDibber == -1) {
            updateSeedDibber();
        }
        return seedDibber;
    }

    private void updateSeedDibber() {
        StringBuilder sb = new StringBuilder();
        sb.append(Varpbits.varpbit(FIRST_VAR) >> 1 & 1);
        for (int i = 23; i <= 28; i++) {
            sb.append(Varpbits.varpbit(FOURTH_VAR) >> i & 1);
        }
        sb.reverse();
        seedDibber = Integer.parseInt(sb.toString(), 2);
    }

    public int getRake() {
        if (rake == -1) {
            updateRake();
        }
        return rake;
    }

    private void updateRake() {
        StringBuilder sb = new StringBuilder();
        sb.append(Varpbits.varpbit(FIRST_VAR) & 1);
        for (int i = 17; i <= 22; i++) {
            sb.append(Varpbits.varpbit(FOURTH_VAR) >> i & 1);
        }
        sb.reverse();
        rake = Integer.parseInt(sb.toString(), 2);
    }

    public int getSpade() {
        if (spade == -1) {
            updateSpade();
        }
        return spade;
    }

    private void updateSpade() {
        StringBuilder sb = new StringBuilder();
        sb.append(Varpbits.varpbit(FIRST_VAR) >> 2 & 1);
        for (int i = 17; i <= 22; i++) {
            sb.append(Varpbits.varpbit(FIFTH_VAR) >> i & 1);
        }
        sb.reverse();
        spade = Integer.parseInt(sb.toString(), 2);
    }

    public String getBLBCompostType() {
        return BLBCompostType;
    }

    public int getBLBAmount() {
        return BLBAmount;
    }
}
