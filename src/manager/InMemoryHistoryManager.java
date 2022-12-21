package manager;

import tasks.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int MAX_HISTORY_SIZE = 10;

    private static final LinkedList<Task> history = new LinkedList<>();

    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }

    @Override
    public void addToHistory(Task task) {
        if (history.size() < MAX_HISTORY_SIZE) {
            history.add(task);
        } else {
            history.removeFirst();
            history.add(task);
        }
    }
}
