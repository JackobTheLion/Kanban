package manager.taskManager;

import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Epic epic;
    protected Task task;
    protected  SubTask subTask;

    protected void initTasks() {
        task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.createTask(task);

        epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        taskManager.createEpic(epic);

        subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epic.getId(),Status.NEW);
        taskManager.createSubTask(subTask);
    }

    @Test
    public void createTaskTest() {
        initTasks();

        final Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void createSubTaskTest() {
        initTasks();

        final SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertNotNull(subTasks, "Подзадача не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");

        final List<Integer> subTasksOfEpic = epic.getSubTasksOfEpic();

        assertNotNull(subTasksOfEpic, "Не возвращается список подзадач эпика");
        assertEquals(1, subTasksOfEpic.size(), "Неверное количество подзадач в эпике");
        assertEquals(subTask.getId(), subTasksOfEpic.get(0), "Список ID в эпике неверный");
    }

    @Test
    public void createEpicTest() {
        initTasks();

        final Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getAllEpicTasks();

        assertNotNull(epics, "Подзадача на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество подзадач.");
        assertEquals(epic, epics.get(0), "Подзадачи не совпадают.");
    }

    @Test
    public void deleteTaskTest() {
        initTasks();

        taskManager.deleteTaskById(task.getId());

        Task savedTask = taskManager.getEpicById(task.getId());
        assertNull(savedTask, "Таск вернулся по ID");

        final List<Task> emptyTasks = taskManager.getAllTasks();

        assertNotNull(emptyTasks, "Задачи на возвращаются.");
        assertEquals(0, emptyTasks.size(), "Неверное количество задач.");
    }

    @Test
    public void deleteSubTaskTest() {
        initTasks();

        taskManager.deleteSubTaskById(subTask.getId());

        SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());
        assertNull(savedSubTask, "Сабтаск вернулся по ID");

        final List<SubTask> allSubTasks = taskManager.getAllSubTasks();
        assertNotNull(allSubTasks, "Подзадачи не возвращаются");
        assertEquals(0, allSubTasks.size(), "Подзадачи не удалены");

        final ArrayList<Integer> subTasksOfEpic = epic.getSubTasksOfEpic();
        assertNotNull(subTasksOfEpic, "Список подзадач не вернулся из Эпика");
        assertEquals(0, subTasksOfEpic.size(), "Подзадачи не удалены из Эпика");

    }

    @Test
    public void deleteEpicTest() {
        initTasks();

        taskManager.deleteEpicById(epic.getId());

        Epic savedEpic = taskManager.getEpicById(epic.getId());
        assertNull(savedEpic, "Эпик вернулся по ID");

        final List<Epic> epics = taskManager.getAllEpicTasks();
        assertNotNull(epics, "Список эпиков не вернулся");
        assertEquals(0, epics.size(), "Эпик не удален из списка эпиков");

        List<SubTask> subTasks = taskManager.getAllSubTasks();
        assertNotNull(subTasks, "Список подзадач не вернулся");
        assertEquals(0, subTasks.size(), "Подзадачи эпика не удалены из списка подзадач");
    }

    @Test
    public void createNullTaskTest() {
        Task task = null;
        final int taskID = taskManager.createTask(task);
        assertEquals(-1, taskID, "Таску null присвоен ID");

        Task savedTask = taskManager.getTaskById(taskID);
        assertNull(savedTask, "Некоректный возрвт таска из пустого списка");

        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Некорректный возврат списка тасков");
        assertEquals(0, tasks.size(), "Список задач не пуст");
    }

    @Test
    public void createNullSubTask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask = null;
        final int subTaskId = taskManager.createSubTask(subTask);
        assertEquals(-1, subTaskId, "Сабтаску null присвоен ID");

        SubTask savedSubTask = taskManager.getSubTaskById(subTaskId);
        assertNull(savedSubTask, "Некоректный возрвт СабТаска из пустого списка");

        List<SubTask> subTasks = taskManager.getAllSubTasks();
        assertNotNull(subTasks, "Некорректный возврат списка сабтасков");
        assertEquals(0, subTasks.size(), "Список сабтасков не пуст");

        ArrayList<Integer> subTasksOfEpic = epic.getSubTasksOfEpic();
        assertNotNull(subTasksOfEpic, "Список подзадач не вернулся");
        assertEquals(0, subTasksOfEpic.size(), "В подзадачах эпика есть элементы");
    }

    @Test
    public void createSubTaskWhenNoEpic() {
        int epicID = 1;
        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID,Status.NEW);
        final int subTaskId = taskManager.createSubTask(subTask);

        assertEquals(-1, subTaskId, "Сабтаску без эпика присвоен ID");

        List<SubTask> subTasks = taskManager.getAllSubTasks();
        assertNotNull(subTasks, "Некорректный возврат списка сабтасков");
        assertEquals(0, subTasks.size(), "Список сабтасков не пуст");
    }

    @Test
    public void createNullEpicTest() {
        Epic epic = null;
        final int epicID = taskManager.createEpic(epic);
        assertEquals(-1, epicID, "Эпику null присвоен ID");

        Epic savedEpic = taskManager.getEpicById(epicID);
        assertNull(savedEpic, "Некоректный возрвт Эпика из пустого списка");

        List<Epic> epics = taskManager.getAllEpicTasks();
        assertNotNull(epics, "Некорректный возврат списка Эпиков");
        assertEquals(0, epics.size(), "Список эпиков не пуст");
    }

    @Test
    public void deleteTaskWithWrongIdTest() {
        initTasks();
        final int wrongId = task.getId() + 100;

        taskManager.deleteTaskById(wrongId);

        final Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void deleteSubTaskWithWrongIdTest() {
        initTasks();

        final int wrongSubTaskId = subTask.getId() + 100;

        taskManager.deleteSubTaskById(wrongSubTaskId);

        final SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTasks();

        assertNotNull(subTasks, "Подзадача не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");

        final List<Integer> subTasksOfEpic = epic.getSubTasksOfEpic();

        assertNotNull(subTasksOfEpic, "Не возвращается список подзадач эпика");
        assertEquals(1, subTasksOfEpic.size(), "Неверное количество подзадач в эпике");
        assertEquals(subTask.getId(), subTasksOfEpic.get(0), "Список ID в эпике неверный");
    }

    @Test
    public void deleteEpicWithWrongIdTest() {
        initTasks();
        final int wrongEpicID = epic.getId() + 100;

        taskManager.deleteEpicById(wrongEpicID);

        final Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getAllEpicTasks();

        assertNotNull(epics, "Подзадача на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество подзадач.");
        assertEquals(epic, epics.get(0), "Подзадачи не совпадают.");
    }

    @Test
    public void getTaskWithWrongIdTest() {
        initTasks();
        final int taskId = taskManager.createTask(task);
        final int wrongId = taskId + 100;

        Task savedTask = taskManager.getTaskById(wrongId);

        assertNull(savedTask, "Полученный таск не null");
    }

    @Test
    public void getEpicWithWrongIdTest() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", Status.NEW);
        final int taskId = taskManager.createEpic(epic);
        final int wrongId = taskId + 100;

        Task savedTask = taskManager.getEpicById(wrongId);

        assertNull(savedTask, "Полученный эпик не null");
    }

    @Test
    public void getSubTaskWithWrongIdTest() {
        initTasks();

        final int wrongSubTaskId = subTask.getId() + 100;

        SubTask savedSubTask = taskManager.getSubTaskById(wrongSubTaskId);

        assertNull(savedSubTask, "Полученный сабтаск не null");

    }

    @Test
    public void updateTaskTest() {
        initTasks();

        Task updatedTask = new Task("Updated name", "Updated description", Status.DONE);
        updatedTask.setId(task.getId());
        LocalDateTime startTime = LocalDateTime.of(2023,12,15,10,0);
        updatedTask.setStartTime(startTime);
        updatedTask.setDuration(60);

        taskManager.updateTask(updatedTask);

        Task savedTask = taskManager.getTaskById(task.getId());
        assertNotNull(savedTask, "Обновленный таск == null");
        assertEquals(updatedTask, savedTask, "Таск не обновился");
    }

    @Test
    public void updateTaskWithNullTest() {
        initTasks();

        Task updatedTask = null;

        taskManager.updateTask(updatedTask);

        Task savedTask = taskManager.getTaskById(task.getId());
        assertNotNull(savedTask, "Обновленный таск == null");
        assertEquals(task, savedTask, "Таск обновился");
    }

    @Test
    public void updateSubTaskWithNullTest() {
        initTasks();

        SubTask updatedSubTask = null;

        taskManager.updateTask(updatedSubTask);

        Task savedTask = taskManager.getSubTaskById(subTask.getId());
        assertNotNull(savedTask, "Обновленный Сабтаск == null");
        assertEquals(subTask, savedTask, "Сабтаск обновился");
    }

    @Test
    public void updateEpicWithNullTest() {
        initTasks();

        Epic updatedEpic = null;

        taskManager.updateEpic(updatedEpic);

        Task savedEpic = taskManager.getEpicById(epic.getId());
        assertNotNull(savedEpic, "Обновленный Сабтаск == null");
        assertEquals(epic, savedEpic, "Сабтаск обновился");
    }

    @Test
    public void clearTasksTest() {
        initTasks();

        taskManager.clearTasks();

        List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(0, tasks.size(), "Неверное количество задач.");

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertNotNull(prioritizedTasks, "Задачи не возвращаются.");
        assertEquals(1, prioritizedTasks.size(), "Неверное количество задач.");
    }

    @Test
    public void clearSubtasksTest() {
        initTasks();

        taskManager.clearSubTasks();

        List<SubTask> subTasks = taskManager.getAllSubTasks();
        assertNotNull(subTasks, "Подзадачи не возвращаются");
        assertEquals(0, subTasks.size(), "Подзадачи не удалены");

        ArrayList<Integer> subTasksOfEpic = taskManager.getEpicById(epic.getId()).getSubTasksOfEpic();
        assertNotNull(subTasksOfEpic, "Подзадачи эпика не возвращаются");
        assertEquals(0, subTasksOfEpic.size(), "Подзадачи эпика не удалены");

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertNotNull(prioritizedTasks, "Задачи не возвращаются.");
        assertEquals(1, prioritizedTasks.size(), "Неверное количество задач.");
    }

    @Test
    public void clearEpicsTest() {
        initTasks();

        taskManager.clearEpics();

        List<Epic> epics = taskManager.getAllEpicTasks();
        assertNotNull(epics, "Подзадачи не возвращаются");
        assertEquals(0, epics.size(), "Подзадачи не удалены");

        List<SubTask> subTasks = taskManager.getAllSubTasks();
        assertNotNull(subTasks, "Подзадачи не возвращаются");
        assertEquals(0, subTasks.size(), "Подзадачи не удалены");

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertNotNull(prioritizedTasks, "Задачи не возвращаются.");
        assertEquals(1, prioritizedTasks.size(), "Неверное количество задач.");
    }

    @Test
    public void getHistoryTest() {
        initTasks();

        List<Task> history = new ArrayList<>();

        history.add(taskManager.getTaskById(task.getId()));
        history.add(taskManager.getEpicById(epic.getId()));
        history.add(taskManager.getSubTaskById(subTask.getId()));

        List<Task> savedHistory = taskManager.getHistory();

        assertEquals(history, savedHistory, "Истории не равны!");
    }

    @Test
    public void epicStatusNewTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.DONE);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.NEW);
        final int subTaskID = taskManager.createSubTask(subTask);

        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.NEW);
        final int subTask2ID = taskManager.createSubTask(subTask2);

        Status epicStatus = epic.getStatus();
        assertEquals(Status.NEW, epicStatus, "Статус не NEW");
    }

    @Test
    public void epicStatusDoneTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.DONE);
        final int subTaskID = taskManager.createSubTask(subTask);

        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.DONE);
        final int subTask2ID = taskManager.createSubTask(subTask2);

        Status epicStatus = epic.getStatus();
        assertEquals(Status.DONE, epicStatus, "Статус не DONE");
    }

    @Test
    public void epicStatusInProgress1Test() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.NEW);
        taskManager.createSubTask(subTask);

        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.IN_PROGRESS);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.DONE);
        taskManager.createSubTask(subTask3);

        Status epicStatus = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, epicStatus, "Статус не IN_PROGRESS");
    }

    @Test
    public void epicStatusInProgress2Test() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.NEW);
        taskManager.createSubTask(subTask);

        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.NEW);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.DONE);
        taskManager.createSubTask(subTask3);

        Status epicStatus = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, epicStatus, "Статус не IN_PROGRESS");
    }

    @Test
    public void epicStatusInProgress3Test() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.IN_PROGRESS);
        taskManager.createSubTask(subTask);

        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.IN_PROGRESS);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.DONE);
        taskManager.createSubTask(subTask3);

        Status epicStatus = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, epicStatus, "Статус не IN_PROGRESS");
    }

    @Test
    public void epicStatusInProgress4Test() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.IN_PROGRESS);
        taskManager.createSubTask(subTask);

        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.IN_PROGRESS);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.NEW);
        taskManager.createSubTask(subTask3);

        Status epicStatus = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, epicStatus, "Статус не IN_PROGRESS");
    }

    @Test
    public void epicStatusInProgress5Test() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.DONE);
        taskManager.createSubTask(subTask);

        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.DONE);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.NEW);
        taskManager.createSubTask(subTask3);

        Status epicStatus = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, epicStatus, "Статус не IN_PROGRESS");
    }

    @Test
    public void epicStatusInProgress6Test() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.DONE);
        taskManager.createSubTask(subTask);

        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.DONE);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test addNewTask", "Test addNewTask description",
                epicID, Status.IN_PROGRESS);
        taskManager.createSubTask(subTask3);

        Status epicStatus = epic.getStatus();
        assertEquals(Status.IN_PROGRESS, epicStatus, "Статус не IN_PROGRESS");
    }

    @Test
    public void getSubTasksByEpicIdTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        SubTask subTask1 = new SubTask("Test addNewTask1", "Test addNewTask description1",
                epicID,Status.NEW);
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Test addNewTask2", "Test addNewTask description2",
                epicID,Status.NEW);
        taskManager.createSubTask(subTask2);

        List<SubTask> savedSubTasks = taskManager.getSubTasksByEpicId(epicID);

        List<SubTask> expectedSubTasks = new ArrayList<>();
        expectedSubTasks.add(subTask1);
        expectedSubTasks.add(subTask2);

        assertEquals(expectedSubTasks, savedSubTasks, "Списки сабтасков не одинаковые");
    }

    @Test
    public void getSubTasksByEpicIdWithWrongIdTest() {
        initTasks();

        int wrongId = epic.getId() + 100;

        List<SubTask> savedSubTasks = taskManager.getSubTasksByEpicId(wrongId);

        assertNull(savedSubTasks, "Вернулся список сабтасков");
    }

    @Test
    public void getSubTasksByEpicIdWhenEmptyTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        final int epicID = taskManager.createEpic(epic);

        List<SubTask> savedSubTasks = taskManager.getSubTasksByEpicId(epicID);

        assertTrue(savedSubTasks.isEmpty(), "Список сабтасков не пуст");
    }

    @Test
    public void updateSubTaskTest() {
        initTasks();

        SubTask updatedSubTask = new SubTask("updatedSubTask", "updatedSubTask description",
                epic.getId(), Status.NEW);
        updatedSubTask.setId(subTask.getId());

        taskManager.updateSubtask(updatedSubTask);

        SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertEquals(updatedSubTask, savedSubTask, "Сабтаск не равен обновленному");
    }

    @Test
    public void updateSubTaskWithWrongIDTest() {
        initTasks();

        int wrongId = subTask.getId() + 100;

        SubTask updatedSubTask = new SubTask("updatedSubTask", "updatedSubTask description",
                epic.getId(), Status.NEW);
        updatedSubTask.setId(wrongId);

        taskManager.updateSubtask(updatedSubTask);

        SubTask savedSubTask = taskManager.getSubTaskById(wrongId);

        assertNull(savedSubTask, "Сабтаск не равен обновленному");
    }

    @Test
    public void updateSubTaskWithWrongEpicIdTest() {
        initTasks();

        int wrongEpicId = epic.getId() + 100;

        SubTask updatedSubTask = new SubTask("updatedSubTask", "updatedSubTask description",
                wrongEpicId, Status.NEW);
        updatedSubTask.setId(subTask.getId());

        taskManager.updateSubtask(updatedSubTask);

        SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertEquals(subTask, savedSubTask, "Сабтаск не равен обновленному");
    }
}
