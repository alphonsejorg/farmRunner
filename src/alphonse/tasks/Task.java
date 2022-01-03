package alphonse.tasks;


import java.util.logging.Logger;

public abstract class Task {
    protected final static Logger LOGGER = Logger.getLogger("TaskLogger");

    abstract public void execute();
    abstract public boolean activate();
}
