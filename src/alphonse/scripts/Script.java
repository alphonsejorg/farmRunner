package alphonse.scripts;

import alphonse.tasks.Task;
import alphonse.util.items.Item;
import com.google.common.eventbus.Subscribe;
import org.powbot.api.Notifications;
import org.powbot.api.event.InventoryChangeEvent;
import org.powbot.api.event.MessageEvent;
import org.powbot.api.script.AbstractScript;
import org.powbot.mobile.script.ScriptManager;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static alphonse.util.items.Inventory.inventory;
import static alphonse.util.managers.ChatManager.chatManager;

public abstract class Script extends AbstractScript {
    protected final static Logger LOGGER = Logger.getLogger("Script");

    protected static List<Task> tasks = new ArrayList<>();
    protected static List<Task> newTasks = new ArrayList<>();
    protected static List<Task> standardTasks = new ArrayList<>();

    protected static boolean changingTasks = false;
    private static boolean stopping = false;
    private static boolean breakOnActivate = true;

    protected abstract void initStandardTasks();

    @Override
    public void onStart() {
        initStandardTasks();
        setStandardTasks();
        inventory.cacheItems();
    }

    @Override
    public void poll() {
        if (stopping) {
            ScriptManager.INSTANCE.stop();
            return;
        }
        if (changingTasks) {
            changeTasks();
            changingTasks = false;
        }

        for (Task task : tasks) {
            if (ScriptManager.INSTANCE.isStopping() || changingTasks || stopping) {
                break;
            }
            if (task.activate()) {
                long time = System.currentTimeMillis();
                task.execute();
                System.out.println(task.getClass().getName() + " took " + (System.currentTimeMillis() - time) + " ms to complete.");
                if (breakOnActivate) {
                    break;
                }
            }
        }
    }

    @Override
    public void onStop() {

    }

    public static void setTasks(List<Task> tasks) {
        changingTasks = true;
        newTasks = tasks;
    }

    protected void changeTasks() {
        tasks = newTasks;
        newTasks = new ArrayList<>();
    }

    public static void stopScript(String msg) {
        Notifications.showNotification(msg);
        LOGGER.warning("stopping script: " + msg);
        stopping = true;
    }

    public static List<Task> getTasks() {
        return tasks;
    }

    public static void setStandardTasks() {
        newTasks = standardTasks;
        changingTasks = true;
    }

    public static void setBreakOnActivate(boolean breakOnActivate) {
        Script.breakOnActivate = breakOnActivate;
    }

    @Subscribe
    public void onInventoryChange(InventoryChangeEvent event) {
        if (event.getQuantityChange() > 0) {
            inventory.getItems().addItem(new Item(event.getItemId(), event.getItemName(), event.getQuantityChange()));
        } else {
            inventory.getItems().removeItem(event.getItemId(), -event.getQuantityChange());
        }
    }

    @Subscribe
    public void onMessageReceived(MessageEvent event) {
        chatManager.addMessage(event.getMessage());
    }
}
