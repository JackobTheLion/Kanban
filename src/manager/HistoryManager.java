package manager;

import tasks.Task;

import java.util.LinkedList;

public interface HistoryManager {
    void addToHistory(Task task);
    LinkedList<Task> getHistory();
}
