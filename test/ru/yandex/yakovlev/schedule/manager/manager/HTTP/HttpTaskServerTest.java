package ru.yandex.yakovlev.schedule.manager.manager.HTTP;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.yakovlev.schedule.HTTP.HttpTaskServer;
import ru.yandex.yakovlev.schedule.tasks.Epic;
import ru.yandex.yakovlev.schedule.tasks.Status;
import ru.yandex.yakovlev.schedule.tasks.SubTask;
import ru.yandex.yakovlev.schedule.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    HttpTaskServer httpTaskServer;
    Task task;
    SubTask subTask;
    Epic epic;
    HttpClient httpClient;
    Gson gson = new Gson();
    String server = "http://localhost:8000";

    @BeforeEach
    public void beforeEach() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpClient = HttpClient.newHttpClient();
        httpTaskServer.start();
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
    }

    public void initTasks() {
        task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        httpTaskServer.taskManager.createTask(task);

        epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        httpTaskServer.taskManager.createEpic(epic);

        subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epic.getId(),Status.NEW);
        httpTaskServer.taskManager.createSubTask(subTask);
    }

    @Test
    public void getAllTasksNormal() {
        initTasks();
        URI uri =URI.create(server + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<Task> tasksSaved = gson.fromJson(response.body(), List.class);

            List<Task> tasksExpected = httpTaskServer.taskManager.getAllTasks();

            assertEquals(tasksExpected, tasksSaved);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllTasksEmpty() {
        URI uri =URI.create(server + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<Task> tasksSaved = gson.fromJson(response.body(), List.class);

            List<Task> tasksExpected = httpTaskServer.taskManager.getAllTasks();

            assertEquals(tasksExpected, tasksSaved);
            assertEquals(0, tasksSaved.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllSubTasksNormal() {
        initTasks();
        URI uri =URI.create(server + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<SubTask> tasksSaved = gson.fromJson(response.body(), List.class);

            List<SubTask> tasksExpected = httpTaskServer.taskManager.getAllSubTasks();

            assertEquals(tasksExpected, tasksSaved);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllSubTasksEmpty() {
        URI uri =URI.create(server + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<SubTask> tasksSaved = gson.fromJson(response.body(), List.class);

            List<SubTask> tasksExpected = httpTaskServer.taskManager.getAllSubTasks();

            assertEquals(tasksExpected, tasksSaved);
            assertEquals(0, tasksSaved.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllEpicsNormal() {
        initTasks();
        URI uri =URI.create(server + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<Epic> tasksSaved = gson.fromJson(response.body(), List.class);

            List<Epic> tasksExpected = httpTaskServer.taskManager.getAllEpicTasks();

            assertEquals(tasksExpected, tasksSaved);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllEpicsEmpty() {
        URI uri = URI.create(server + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<Epic> tasksSaved = gson.fromJson(response.body(), List.class);

            List<Epic> tasksExpected = httpTaskServer.taskManager.getAllEpicTasks();

            assertEquals(tasksExpected, tasksSaved);
            assertEquals(0, tasksSaved.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getTaskById() {
        initTasks();
        int id = task.getId();
        URI uri = URI.create(server + "/task/tasks/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Task savedTask = gson.fromJson(response.body(), Task.class);

            assertEquals(task, savedTask);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
