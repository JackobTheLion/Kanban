package manager;

public class Managers {

    private final TaskManager taskManager = new InMemoryTaskManager();
    static private final HistoryManager historyManager = new InMemoryHistoryManager();

    public TaskManager getDefault() {
        return taskManager;
    }

    static public HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
