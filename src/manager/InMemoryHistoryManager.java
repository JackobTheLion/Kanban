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
        if (task == null) {
            System.out.println("Ошибка в addToHistory. Task == null");
            return;
        }

        history.add(task);

        if (history.size() > MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
    }
}
