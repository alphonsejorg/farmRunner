package alphonse.tasks;

import alphonse.scripts.Script;

import java.util.ArrayList;
import java.util.List;

import static alphonse.tasks.TaskChanger.taskChanger;

public abstract class SuperTask extends Task {

    public abstract List<Task> getTasks();

    private TaskInfo taskInfo;
    private boolean finishAtEnd;

    public SuperTask(TaskInfo taskInfo, boolean finishAtEnd) {
        this.taskInfo = taskInfo;
        this.finishAtEnd = finishAtEnd;
    }

    private Task finish = new Task() {
        @Override
        public void execute() {
            LOGGER.info("finishing macro task: " + taskInfo.name);
            taskChanger.setTasksPrevious();
            taskInfo.finish();
        }

        @Override
        public boolean activate() {
            return taskInfo.finish.activate();
        }
    };

    @Override
    public void execute() {
        taskInfo.start();
        ArrayList<Task> tasks = new ArrayList<>(getTasks());
        LOGGER.info("starting macro task: " + taskInfo.name);
        if (finishAtEnd) {
            tasks.add(finish);
        } else {
            tasks.add(0, finish);
        }
        taskChanger.setTasks(tasks);
        Script.setBreakOnActivate(taskInfo.breakOnActivate);
    }

    @Override
    public boolean activate() {
        return taskInfo.start.activate();
    }
}
