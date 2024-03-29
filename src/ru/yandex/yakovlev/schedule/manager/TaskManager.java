package ru.yandex.yakovlev.schedule.manager;

import ru.yandex.yakovlev.schedule.tasks.Epic;
import ru.yandex.yakovlev.schedule.tasks.SubTask;
import ru.yandex.yakovlev.schedule.tasks.Task;

import java.util.List;

public interface TaskManager  {

    List<Task> getAllTasks();

    List<Epic> getAllEpicTasks();

    List<SubTask> getAllSubTasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubTaskById(int id);

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    void clearAllTasks();

    void updateTask(Task updatedTask);

    void updateEpic(Epic updatedEpic);

    void updateSubtask(SubTask updatedSubTask);

    List<SubTask> getSubTasksByEpicId(int id);

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubTask(SubTask subTask);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    boolean doesTaskExist(int id);
}
