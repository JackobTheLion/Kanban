package ru.yandex.yakovlev.schedule.manager.manager.taskManager.HTTP;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.yakovlev.schedule.http.HttpTaskManager;
import ru.yandex.yakovlev.schedule.http.HttpTaskServer;
import ru.yandex.yakovlev.schedule.http.KVServer;
import ru.yandex.yakovlev.schedule.manager.manager.taskManager.TaskManagerTest;
import ru.yandex.yakovlev.schedule.tasks.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    @BeforeEach
    public void beforeEach() throws URISyntaxException, IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();

        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        taskManager = new HttpTaskManager("http://localhost:", KVServer.PORT);
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
        httpTaskServer.stop();

    }
    @Test
    public void saveAndLoadTest() {
        try {
            taskManager.setApiTokenDebug();

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

            taskManager.getTaskById(task2.getId());
            taskManager.getSubTaskById(subTask2.getId());
            taskManager.getSubTaskById(subTask1.getId());
            taskManager.getEpicById(epic1.getId());
            taskManager.getTaskById(task1.getId());

            HttpTaskManager taskManager2 = new HttpTaskManager("http://localhost:", 8078);
            taskManager2.setApiTokenDebug();
            taskManager2.load();

            List<Task> expectedTasks = taskManager.getAllTasks();
            List<Task> savedTasks = taskManager2.getAllTasks();

            List<SubTask> expectedSubTasks = taskManager.getAllSubTasks();
            List<SubTask> savedSubTasks = taskManager2.getAllSubTasks();

            List<Epic> expectedEpic = taskManager.getAllEpicTasks();
            List<Epic> savedEpic = taskManager2.getAllEpicTasks();

            List<Task> expectedHistory = taskManager.getHistory();
            List<Task> savedHistory = taskManager2.getHistory();

            assertEquals(expectedTasks,savedTasks);
            assertEquals(expectedSubTasks,savedSubTasks);
            assertEquals(expectedEpic,savedEpic);
            assertEquals(expectedHistory,savedHistory);


        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
