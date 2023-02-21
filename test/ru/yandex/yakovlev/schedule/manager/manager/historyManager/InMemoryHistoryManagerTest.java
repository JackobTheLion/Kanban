package ru.yandex.yakovlev.schedule.manager.manager.historyManager;

import ru.yandex.yakovlev.schedule.manager.HistoryManager;
import ru.yandex.yakovlev.schedule.manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.yakovlev.schedule.tasks.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void addToHistoryWithNoduplicateTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        epic.setId(1);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                1, Status.NEW);
        subTask.setId(2);

        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        task.setId(3);

        historyManager.add(epic);
        historyManager.add(subTask);
        historyManager.add(task);
        historyManager.add(subTask);

        List<Task> savedHistory = historyManager.getHistory();

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(epic);
        expectedHistory.add(task);
        expectedHistory.add(subTask);

        assertEquals(expectedHistory, savedHistory, "Истории не равны");
    }

    @Test
    public void addNullToHistory() {
        Task task = null;
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        task1.setId(1);

        historyManager.add(task);
        historyManager.add(task1);

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);

        assertEquals(expectedHistory, historyManager.getHistory(), "Истории не равны");
    }

    @Test
    public void removeFromHistoryStartTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        epic.setId(1);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                1, Status.NEW);
        subTask.setId(2);

        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        task.setId(3);

        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subTask);

        historyManager.remove(epic.getId());

        List<Task> savedHistory = historyManager.getHistory();

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task);
        expectedHistory.add(subTask);

        assertEquals(expectedHistory, savedHistory, "Истории не равны");
    }

    @Test
    public void removeFromHistoryEndTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        epic.setId(1);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                1, Status.NEW);
        subTask.setId(2);

        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        task.setId(3);

        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subTask);

        historyManager.remove(subTask.getId());

        List<Task> savedHistory = historyManager.getHistory();

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(epic);
        expectedHistory.add(task);

        assertEquals(expectedHistory, savedHistory, "Истории не равны");
    }

    @Test
    public void removeFromHistoryMiddleTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        epic.setId(1);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                1, Status.NEW);
        subTask.setId(2);

        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        task.setId(3);

        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subTask);

        historyManager.remove(task.getId());

        List<Task> savedHistory = historyManager.getHistory();

        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(epic);
        expectedHistory.add(subTask);

        assertEquals(expectedHistory, savedHistory, "Истории не равны");
    }

    @Test
    public void clearHistoryTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        epic.setId(1);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                1, Status.NEW);
        subTask.setId(2);

        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        task.setId(3);

        historyManager.add(epic);
        historyManager.add(task);
        historyManager.add(subTask);

        historyManager.clearHistory();

        List<Task> savedHistory = historyManager.getHistory();

        assertTrue(savedHistory.isEmpty(), "История не пуста");
    }
}
