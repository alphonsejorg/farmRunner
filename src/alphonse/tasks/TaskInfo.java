package alphonse.tasks;

public class TaskInfo {
    private static String activeTaskName = "none";

    boolean breakOnActivate;
    protected String name;
    protected Activatable start;
    protected Activatable finish;
    private static long startTime;

    public TaskInfo(Activatable start, Activatable finish, String name, boolean breakOnActivate) {
        this.start = start;
        this.finish = finish;
        this.name = name;
        this.breakOnActivate = breakOnActivate;
    }

    protected void start() {
        activeTaskName = this.name;
        startTime = System.currentTimeMillis();
    }

    protected void finish() {
        System.out.println(name + " took " + (System.currentTimeMillis() - startTime) + " ms to complete.");
    }

    public static String getActiveTaskName() {
        return activeTaskName;
    }

    public static long getStartTime() {
        return startTime;
    }
}