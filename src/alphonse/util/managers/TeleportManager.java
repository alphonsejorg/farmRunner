package alphonse.util.managers;

public class TeleportManager {
    private long nextHomeTeleport = System.currentTimeMillis();
    private boolean homeTeleportAssigned = false;

    private boolean haveTeleported = false;

    public final static TeleportManager teleportManager = new TeleportManager();

    private TeleportManager() {}

    public long getNextHomeTeleport() {
        return nextHomeTeleport;
    }

    public void setNextHomeTeleport(long nextHomeTeleport) {
        this.nextHomeTeleport = nextHomeTeleport;
    }

    public void setHaveTeleported(boolean haveTeleported) {
        this.haveTeleported = haveTeleported;
    }

    public boolean haveTeleported() {
        return haveTeleported;
    }

    public void setHomeTeleportAssigned(boolean homeTeleportAssigned) {
        this.homeTeleportAssigned = homeTeleportAssigned;
    }

    public boolean isHomeTeleportAssigned() {
        return homeTeleportAssigned;
    }
}
