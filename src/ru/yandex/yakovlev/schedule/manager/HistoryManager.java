package ru.yandex.yakovlev.schedule.manager;

import ru.yandex.yakovlev.schedule.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
    void clearHistory();
}
