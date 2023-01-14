package manager;

import tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addToHistory(Task task);
    void remove(int id);
    ArrayList<Task> getHistory();
}
