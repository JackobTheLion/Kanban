package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

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
}
