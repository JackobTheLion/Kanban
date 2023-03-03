package ru.yandex.yakovlev.schedule.manager.manager.http;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.yakovlev.schedule.http.HttpTaskServer;
import ru.yandex.yakovlev.schedule.http.KVServer;
import ru.yandex.yakovlev.schedule.http.adapter.DurationConverter;
import ru.yandex.yakovlev.schedule.http.adapter.LocalDateTimeConverter;
import ru.yandex.yakovlev.schedule.manager.TaskManager;
import ru.yandex.yakovlev.schedule.tasks.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    HttpTaskServer httpTaskServer;
    KVServer kvServer;
    Task task;
    SubTask subTask;
    SubTask subTask2;
    Epic epic;
    HttpClient httpClient;
    TaskManager taskManager;
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();
    String server = "http://localhost:8000";

    Type taskListType = new TypeToken<List<Task>>() {}.getType();
    Type subTaskListType = new TypeToken<List<SubTask>>() {}.getType();
    Type epicListType = new TypeToken<List<Epic>>() {}.getType();


    @BeforeEach
    public void beforeEach() throws IOException, URISyntaxException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpClient = HttpClient.newHttpClient();
        httpTaskServer.start();
        taskManager = httpTaskServer.getTaskManager();
    }

    @AfterEach
    public void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    public void initTasks() {
        task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        task.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        task.setDuration(60);
        taskManager.createTask(task);

        epic = new Epic("Test addNewEpic", "Test addNewEpic description", Status.NEW);
        taskManager.createEpic(epic);

        subTask = new SubTask("Test addNewTask", "Test addNewTask description",
                epic.getId(),Status.NEW);
        subTask.setStartTime(LocalDateTime.of(2023,4,15,8, 0));
        subTask.setDuration(60);
        taskManager.createSubTask(subTask);

        subTask2 = new SubTask("Test addNewTask", "Test addNewTask description",
                epic.getId(),Status.NEW);
        subTask2.setStartTime(LocalDateTime.of(2023,5,15,8, 0));
        subTask2.setDuration(60);
        taskManager.createSubTask(subTask2);
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
            List<Task> tasksSaved = gson.fromJson(response.body(), taskListType);

            List<Task> tasksExpected = taskManager.getAllTasks();

            assertEquals(tasksExpected, tasksSaved);
        } catch (IOException | InterruptedException e) {
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

            List<Task> tasksSaved = gson.fromJson(response.body(), taskListType);

            List<Task> tasksExpected = taskManager.getAllTasks();

            assertEquals(tasksExpected, tasksSaved);
            assertEquals(0, tasksSaved.size());
        } catch (IOException | InterruptedException e) {
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
            List<SubTask> tasksSaved = gson.fromJson(response.body(), subTaskListType);

            List<SubTask> tasksExpected = taskManager.getAllSubTasks();

            assertEquals(tasksExpected, tasksSaved);
        } catch (IOException | InterruptedException e) {
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
            List<SubTask> tasksSaved = gson.fromJson(response.body(), subTaskListType);

            List<SubTask> tasksExpected = taskManager.getAllSubTasks();

            assertEquals(tasksExpected, tasksSaved);
            assertEquals(0, tasksSaved.size());
        } catch (IOException | InterruptedException e) {
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
            List<Epic> tasksSaved = gson.fromJson(response.body(), epicListType);

            List<Epic> tasksExpected = taskManager.getAllEpicTasks();

            assertEquals(tasksExpected, tasksSaved);
        } catch (IOException | InterruptedException e) {
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
            List<Epic> tasksSaved = gson.fromJson(response.body(), epicListType);

            List<Epic> tasksExpected = taskManager.getAllEpicTasks();

            assertEquals(tasksExpected, tasksSaved);
            assertEquals(0, tasksSaved.size());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getTaskById() {
        initTasks();
        int id = task.getId();
        URI uri = URI.create(server + "/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Task savedTask = gson.fromJson(response.body(), Task.class);

            assertEquals(task, savedTask);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getTaskByNotExistingId() {
        initTasks();
        int id = task.getId() + 100;
        URI uri = URI.create(server + "/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(404, response.statusCode());
            assertEquals("Таска с ID " + id + " не существует", response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getTaskByWrongId() {
        initTasks();
        URI uri = URI.create(server + "/tasks/task/?id=ThisIsNotId");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(400, response.statusCode());
            assertEquals("Неверный ID", response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getSubTaskById() {
        initTasks();
        int id = subTask.getId();
        URI uri = URI.create(server + "/tasks/subtask/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            SubTask savedSubTask = gson.fromJson(response.body(), SubTask.class);

            assertEquals(subTask, savedSubTask);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getSubTaskByNotExistingId() {
        initTasks();
        int id = subTask.getId() + 100;
        URI uri = URI.create(server + "/tasks/subtask/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(404, response.statusCode());
            assertEquals("Сабтаска с ID " + id + " не существует", response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getSubTaskByWrongId() {
        initTasks();
        URI uri = URI.create(server + "/tasks/subtask/?id=ThisIsNotId");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(400, response.statusCode());
            assertEquals("Неверный ID", response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getEpicById() {
        initTasks();
        int id = epic.getId();
        URI uri = URI.create(server + "/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Epic savedSubTask = gson.fromJson(response.body(), Epic.class);

            assertEquals(epic, savedSubTask);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getEpicByNotExistingId() {
        initTasks();
        int id = epic.getId() + 100;
        URI uri = URI.create(server + "/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(404, response.statusCode());
            assertEquals("Эпика с ID " + id + " не существует", response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getEpicByWrongId() {
        initTasks();
        URI uri = URI.create(server + "/tasks/epic/?id=ThisIsNotId");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(400, response.statusCode());
            assertEquals("Неверный ID", response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getHistoryNormal() {
        initTasks();
        taskManager.getEpicById(epic.getId());
        taskManager.getTaskById(task.getId());
        taskManager.getSubTaskById(subTask.getId());

        List<Task> expectedHistory = taskManager.getHistory();

        URI uri = URI.create(server + "/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonElement jsonElement = JsonParser.parseString(response.body());
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<Task> historySaved = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                String taskType = element.getAsJsonObject().get("taskType").getAsString();
                switch (taskType) {
                    case "TASK":
                        historySaved.add(gson.fromJson(element, Task.class));
                        break;
                    case "EPIC":
                        historySaved.add(gson.fromJson(element, Epic.class));
                        break;
                    case "SUBTASK":
                        historySaved.add(gson.fromJson(element, SubTask.class));
                        break;
                }
            }

            assertEquals(expectedHistory, historySaved);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getHistoryEmpty() {
        URI uri = URI.create(server + "/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            assertEquals("История пуста", response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getPrioritizedNormal() {
        initTasks();
        URI uri = URI.create(server + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonElement jsonElement = JsonParser.parseString(response.body());
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            List<Task> prioritizedSaved = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                String taskType = element.getAsJsonObject().get("taskType").getAsString();
                switch (taskType) {
                    case "TASK":
                        prioritizedSaved.add(gson.fromJson(element, Task.class));
                        break;
                    case "EPIC":
                        prioritizedSaved.add(gson.fromJson(element, Epic.class));
                        break;
                    case "SUBTASK":
                        prioritizedSaved.add(gson.fromJson(element, SubTask.class));
                        break;
                }
            }

            List<Task> prioritizedExpected = taskManager.getPrioritizedTasks();

            assertEquals(prioritizedExpected, prioritizedSaved);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getPrioritizedEmpty() {
        URI uri = URI.create(server + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            assertEquals("Не создано ни одной задачи", response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getSubsOfEpicNormal() {
        initTasks();
        int idOfEpic = epic.getId();

        URI uri = URI.create(server + "/tasks/subtask/epic/?id=" + idOfEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            List<SubTask> savedSubs = gson.fromJson(response.body(), subTaskListType);

            List<SubTask> expectedSubs = taskManager.getSubTasksByEpicId(idOfEpic);

            assertEquals(expectedSubs, savedSubs);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getSubsOfEpicNonExistingId() {
        initTasks();
        int idOfEpic = epic.getId() + 100;

        URI uri = URI.create(server + "/tasks/subtask/epic/?id=" + idOfEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
            assertEquals("Эпика с ID " + idOfEpic + " не существует", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getSubsOfEpicWrongId() {
        initTasks();

        URI uri = URI.create(server + "/tasks/subtask/epic/?id=thisIsNotId");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(400, response.statusCode());
            assertEquals("Неверный ID", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postTaskNormal() {
        Task task1 = new Task("Name", "Description", Status.NEW);

        String taskJson = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());
            assertEquals("Задача успешно создана. ID 1", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postTaskTimeBooked() {
        initTasks();

        Task task1 = new Task("Name", "Description", Status.NEW);
        task1.setStartTime(task.getStartTime());
        task1.setDuration(60);

        String taskJson = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(400, response.statusCode());
            assertEquals("Не удалось создать задачу. Слот занят.", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postTaskNonExistingId() {
        initTasks();

        Task task1 = new Task("Name", "Description", Status.NEW);
        task1.setId(task.getId() + 100);

        String taskJson = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(404, response.statusCode());
            assertEquals("Невозможно обновить задачу неверный ID", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postTaskTimeExistingId() {
        initTasks();

        Task task1 = new Task("Name", "Description", Status.NEW);
        task1.setId(task.getId());

        String taskJson = gson.toJson(task1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());
            assertEquals("Задача успешно обновлена.", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postTaskWrongJson() {
        String wrongJson = "I am wrong Json";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(wrongJson);
        URI uri = URI.create(server + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(400, response.statusCode());
            assertEquals("Получен некорректный JSON", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postSubTaskNormal() {
        initTasks();
        SubTask subTask1 = new SubTask("Name", "Description", epic.getId(), Status.NEW);

        String taskJson = gson.toJson(subTask1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());
            assertEquals("Сабтаск успешно создан. ID 5", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postSubTaskTimeBooked() {
        initTasks();
        SubTask subTask1 = new SubTask("Name", "Description", epic.getId(), Status.NEW);
        subTask1.setStartTime(subTask.getStartTime());
        subTask1.setDuration(60);

        String taskJson = gson.toJson(subTask1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(400, response.statusCode());
            assertEquals("Не удалось создать сабтаск. Слот занят.", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postSubTaskNonExistingId() {
        initTasks();
        SubTask subTask1 = new SubTask("Name", "Description", epic.getId(), Status.NEW);
        subTask1.setId(subTask.getId() + 1000);

        String taskJson = gson.toJson(subTask1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(404, response.statusCode());
            assertEquals("Невозможно обновить сабтаск неверный ID", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postSubTaskExistingId() {
        initTasks();
        SubTask subTask1 = new SubTask("Name", "Description", epic.getId(), Status.NEW);
        subTask1.setId(subTask.getId());

        String taskJson = gson.toJson(subTask1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());
            assertEquals("Сабтаск успешно обновлен.", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postSubTaskWrongJson() {
        String wrongJson = "I am wrong Json";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(wrongJson);
        URI uri = URI.create(server + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(400, response.statusCode());
            assertEquals("Получен некорректный JSON", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postEpicNormal() {
        Epic epic1 = new Epic("Name", "Description", Status.NEW);

        String taskJson = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());
            assertEquals("Эпик успешно создан. ID 1", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postEpicNonExistingId() {
        initTasks();

        Epic epic1 = new Epic("Name", "Description", Status.NEW);
        epic1.setId(epic.getId() + 100);

        String taskJson = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(404, response.statusCode());
            assertEquals("Невозможно обновить эпик неверный ID", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postEpicExistingId() {
        initTasks();

        Epic epic1 = new Epic("Name", "Description", Status.NEW);
        epic1.setId(epic.getId());

        String taskJson = gson.toJson(epic1);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        URI uri = URI.create(server + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(201, response.statusCode());
            assertEquals("Эпик успешно обновлен.", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void postEpicWrongJson() {
        String wrongJson = "I am wrong Json";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(wrongJson);
        URI uri = URI.create(server + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(400, response.statusCode());
            assertEquals("Получен некорректный JSON", response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteAllTasks() {
        initTasks();
        URI uri = URI.create(server + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Все задачи успешно удалены", response.body());
            assertEquals(201, response.statusCode());
            assertTrue(taskManager.getAllTasks().isEmpty());
            assertFalse(taskManager.getAllSubTasks().isEmpty());
            assertFalse(taskManager.getAllEpicTasks().isEmpty());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void deleteAllSubTask() {
        initTasks();
        URI uri = URI.create(server + "/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Все сабтаски успешно удалены", response.body());
            assertEquals(201, response.statusCode());
            assertTrue(taskManager.getAllSubTasks().isEmpty());
            assertFalse(taskManager.getAllTasks().isEmpty());
            assertFalse(taskManager.getAllEpicTasks().isEmpty());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteAllEpic() {
        initTasks();
        URI uri = URI.create(server + "/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Все эпики успешно удалены", response.body());
            assertEquals(201, response.statusCode());
            assertTrue(taskManager.getAllEpicTasks().isEmpty());
            assertTrue(taskManager.getAllSubTasks().isEmpty());
            assertFalse(taskManager.getAllTasks().isEmpty());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteTaskById() {
        initTasks();
        int id = task.getId();
        URI uri = URI.create(server + "/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Задача с ID " + id + " удалена.", response.body());
            assertEquals(201, response.statusCode());
            assertNull(taskManager.getTaskById(id));

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteTaskByNonExistingId() {
        initTasks();
        int id = task.getId() + 100;
        URI uri = URI.create(server + "/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Задача с ID " + id + " не существует.", response.body());
            assertEquals(404, response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteTaskByWrongId() {
        URI uri = URI.create(server + "/tasks/task/?id=IAmWrongId");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Неверный ID", response.body());
            assertEquals(400, response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteSubTaskById() {
        initTasks();
        int id = subTask.getId();
        URI uri = URI.create(server + "/tasks/subtask/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Сабтаск с ID " + id + " удален.", response.body());
            assertEquals(201, response.statusCode());
            assertNull(taskManager.getSubTaskById(id));

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteSubTaskByNonExistingId() {
        initTasks();
        int id = subTask.getId() + 100;
        URI uri = URI.create(server + "/tasks/subtask/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Сабтаск с ID " + id + " не существует.", response.body());
            assertEquals(404, response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteSubTaskByWrongId() {
        URI uri = URI.create(server + "/tasks/subtask/?id=IAmWrongId");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Неверный ID", response.body());
            assertEquals(400, response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteEpicById() {
        initTasks();
        int id = epic.getId();
        URI uri = URI.create(server + "/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Эпик с ID " + id + " удален.", response.body());
            assertEquals(201, response.statusCode());
            assertNull(taskManager.getEpicById(id));

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteEpicByNonExistingId() {
        initTasks();
        int id = epic.getId() + 100;
        URI uri = URI.create(server + "/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Эпик с ID " + id + " не существует.", response.body());
            assertEquals(404, response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteEpicByWrongId() {
        URI uri = URI.create(server + "/tasks/epic/?id=IAmWrongId");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Неверный ID", response.body());
            assertEquals(400, response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void unknownEndpoint() {
        URI uri = URI.create(server + "/tasks/someRandomEndpoint/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals("Такого эндпоинта не существует", response.body());
            assertEquals(404, response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
