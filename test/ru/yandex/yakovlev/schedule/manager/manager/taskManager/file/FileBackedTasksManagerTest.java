package ru.yandex.yakovlev.schedule.manager.manager.taskManager.file;

import ru.yandex.yakovlev.schedule.manager.FileBackedTasksManager;
import ru.yandex.yakovlev.schedule.manager.ScheduleManager;
import ru.yandex.yakovlev.schedule.manager.exceptions.ManagerSaveException;
import ru.yandex.yakovlev.schedule.manager.manager.taskManager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.yakovlev.schedule.tasks.Epic;
import ru.yandex.yakovlev.schedule.tasks.Status;
import ru.yandex.yakovlev.schedule.tasks.SubTask;
import ru.yandex.yakovlev.schedule.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file = new File("test/ru/yandex/yakovlev/schedule/manager/resources/backupTest.csv");

    @BeforeEach
    public void beforeEach() {
        try (OutputStreamWriter writer = new OutputStreamWriter
                (new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write("");
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName() + " at "
                    + file.getPath(), e);
        }
        taskManager = new FileBackedTasksManager(file.getPath());
    }

    @Test
    public void saveAndLoadNormalTest() {

        Task task1 = new Task("задача 1","описание 1", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        task1.setDuration(60);
        taskManager.createTask(task1);

        Task task2 = new Task("задача 2","описание 2", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2023,3,15,9, 0));
        task2.setDuration(40);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("эпик 1","описание эпика 1", Status.NEW);
        taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Сабтаск 1","описание сабтаска 1",
                epic1.getId(), Status.NEW);
        subTask1.setStartTime(LocalDateTime.of(2023,3,15,12, 0));
        subTask1.setDuration(30);
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Сабтаск 2","описание сабтаска 2",
                epic1.getId(), Status.NEW);
        subTask2.setStartTime(LocalDateTime.of(2023,3,15,12, 30));
        subTask2.setDuration(40);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Сабтаск 3","описание сабтаска 3",
                epic1.getId(), Status.NEW);
        subTask3.setStartTime(LocalDateTime.of(2023,3,15,13, 30));
        subTask3.setDuration(30);
        taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("эпик 2","описание эпика 2", Status.NEW);
        taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Сабтаск 4","описание сабтаска 4",
                epic2.getId(), Status.NEW);
        taskManager.createSubTask(subTask4);

        Epic epic3 = new Epic("эпик 3","описание эпика 3", Status.NEW);
        taskManager.createEpic(epic3);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getSubTaskById(subTask2.getId());
        taskManager.getSubTaskById(subTask3.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubTaskById(subTask4.getId());

        FileBackedTasksManager loadedTaskManager = FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskManager.getHistory(), loadedTaskManager.getHistory()
                , "Истории не равны");
        assertEquals(taskManager.getAllTasks(), loadedTaskManager.getAllTasks()
                , "Списки задач не равны");
        assertEquals(taskManager.getAllEpicTasks(), loadedTaskManager.getAllEpicTasks()
                , "Списки эпиков не равны");
        assertEquals(taskManager.getAllSubTasks(), loadedTaskManager.getAllSubTasks()
                , "Списки эпиков не равны");
        assertEquals(taskManager.getPrioritizedTasks(), loadedTaskManager.getPrioritizedTasks()
                , "Списки эпиков не равны");

        ArrayList<ScheduleManager.TimeSlot> slots = taskManager.scheduleManager.getSchedule();
        ArrayList<ScheduleManager.TimeSlot> loadedSlots = loadedTaskManager.scheduleManager.getSchedule();

        assertEquals(slots, loadedSlots, "Расписания не равны");

    }

    @Test
    public void saveAndLoadEmptyHistoryTest() {

        Task task1 = new Task("задача 1","описание 1", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        task1.setDuration(60);
        taskManager.createTask(task1);

        Task task2 = new Task("задача 2","описание 2", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2023,3,15,9, 0));
        task2.setDuration(40);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("эпик 1","описание эпика 1", Status.NEW);
        taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Сабтаск 1","описание сабтаска 1",
                epic1.getId(), Status.NEW);
        subTask1.setStartTime(LocalDateTime.of(2023,3,15,12, 0));
        subTask1.setDuration(30);
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Сабтаск 2","описание сабтаска 2",
                epic1.getId(), Status.NEW);
        subTask2.setStartTime(LocalDateTime.of(2023,3,15,12, 30));
        subTask2.setDuration(40);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Сабтаск 3","описание сабтаска 3",
                epic1.getId(), Status.NEW);
        subTask3.setStartTime(LocalDateTime.of(2023,3,15,13, 30));
        subTask3.setDuration(30);
        taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("эпик 2","описание эпика 1", Status.NEW);
        taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Сабтаск 4","описание сабтаска 4",
                epic2.getId(), Status.NEW);
        taskManager.createSubTask(subTask4);

        FileBackedTasksManager loadedTaskManager = FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskManager.getHistory(), loadedTaskManager.getHistory()
                , "Истории не равны");
        assertEquals(taskManager.getAllTasks(), loadedTaskManager.getAllTasks()
                , "Списки задач не равны");
        assertEquals(taskManager.getAllEpicTasks(), loadedTaskManager.getAllEpicTasks()
                , "Списки эпиков не равны");
        assertEquals(taskManager.getAllSubTasks(), loadedTaskManager.getAllSubTasks()
                , "Списки эпиков не равны");
        assertEquals(taskManager.getPrioritizedTasks(), loadedTaskManager.getPrioritizedTasks()
                , "Списки эпиков не равны");

        ArrayList<ScheduleManager.TimeSlot> slots = taskManager.scheduleManager.getSchedule();
        ArrayList<ScheduleManager.TimeSlot> loadedSlots = loadedTaskManager.scheduleManager.getSchedule();

        assertEquals(slots, loadedSlots, "Расписания не равны");
    }

    @Test
    public void saveAndLoadNoTasksTest() {
        FileBackedTasksManager loadedTaskManager = FileBackedTasksManager.loadFromFile(file);

        assertEquals(taskManager.getHistory(), loadedTaskManager.getHistory()
                , "Истории не равны");
        assertEquals(taskManager.getAllTasks(), loadedTaskManager.getAllTasks()
                , "Списки задач не равны");
        assertEquals(taskManager.getAllEpicTasks(), loadedTaskManager.getAllEpicTasks()
                , "Списки эпиков не равны");
        assertEquals(taskManager.getAllSubTasks(), loadedTaskManager.getAllSubTasks()
                , "Списки эпиков не равны");
        assertEquals(taskManager.getPrioritizedTasks(), loadedTaskManager.getPrioritizedTasks()
                , "Списки эпиков не равны");

        ArrayList<ScheduleManager.TimeSlot> slots = taskManager.scheduleManager.getSchedule();
        ArrayList<ScheduleManager.TimeSlot> loadedSlots = loadedTaskManager.scheduleManager.getSchedule();

        assertEquals(slots, loadedSlots, "Расписания не равны");
    }
}
