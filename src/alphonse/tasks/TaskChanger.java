package alphonse.tasks;

import alphonse.scripts.Script;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Logger;

public class TaskChanger {
    public final static TaskChanger taskChanger = new TaskChanger();
    private final static Logger LOGGER = Logger.getLogger("taskChanger");

    private Deque<ArrayList<Task>> previousTasks = new ArrayDeque<>();

    public void setTasks(List<Task> tasks) {
        setTasks(tasks, true);
    }

    private void setTasks(List<Task> tasks, boolean saveTasks) {
        if (saveTasks) {
            savePreviousTasks();
        }
        Script.setTasks(tasks);
    }

    private void savePreviousTasks() {
        try {
            LOGGER.info("saving previous tasks");
            previousTasks.addLast((ArrayList<Task>) Script.getTasks());
            LOGGER.info("previous Task list new size:" + previousTasks.size());
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    public void setTasksPrevious() {
        try {
            if (previousTasks.isEmpty()) {
                LOGGER.info("previousTasks is empty, setting standard tasks");
                LOGGER.info("no previous tasks, setting standard tasks");
                Script.setStandardTasks();

            } else {
                LOGGER.info("setting tasks to previous");
                if (previousTasks.getLast().equals(Script.getTasks())) {
                    LOGGER.info("previousTasks.getLast is the same as current tasks, removing last then setting new tasks");
                    previousTasks.removeLast();
                    setTasks(previousTasks.getLast(), false);
                    LOGGER.info("previousTasks new size: " + previousTasks.size());

                } else {
                    LOGGER.info("previousTasks.getLast is not the same as new tasks, setting new tasks then removing from list");
                    setTasks(previousTasks.getLast(), false);
                    previousTasks.removeLast();
                    LOGGER.info("previousTasks new size: " + previousTasks.size());
                }
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }
}

