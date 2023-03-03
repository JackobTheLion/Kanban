package ru.yandex.yakovlev.schedule.http;

import com.google.gson.*;
import ru.yandex.yakovlev.schedule.http.adapter.DurationConverter;
import ru.yandex.yakovlev.schedule.http.adapter.LocalDateTimeConverter;
import ru.yandex.yakovlev.schedule.manager.CSVTaskFormat;
import ru.yandex.yakovlev.schedule.manager.FileBackedTasksManager;
import ru.yandex.yakovlev.schedule.tasks.Epic;
import ru.yandex.yakovlev.schedule.tasks.SubTask;
import ru.yandex.yakovlev.schedule.tasks.Task;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();
    private final KVClient kvClient;

    public HttpTaskManager(String path, int port) throws URISyntaxException, IOException, InterruptedException {
        super(path);
        kvClient = new KVClient(port);
    }

    @Override
    public void save() {
        List<Task> allTasks = getAllTasks();
        allTasks.addAll(getAllSubTasks());
        allTasks.addAll(getAllEpicTasks());
        String allTasksJson = gson.toJson(allTasks);
        kvClient.put("tasks", allTasksJson);

        kvClient.put("history", CSVTaskFormat.historyToString(inMemoryHistoryManager));
    }

    public void load() {
        String tasksJson = kvClient.load("tasks");
        if (tasksJson == null) {
            System.out.println("Ошибка получения списка задач с сервера");
        } else {
            JsonElement jsonElement = JsonParser.parseString(tasksJson);
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<Task> savedTasks = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                String taskType = element.getAsJsonObject().get("taskType").getAsString();
                switch (taskType) {
                    case "TASK":
                        savedTasks.add(gson.fromJson(element, Task.class));
                        break;
                    case "EPIC":
                        savedTasks.add(gson.fromJson(element, Epic.class));
                        break;
                    case "SUBTASK":
                        savedTasks.add(gson.fromJson(element, SubTask.class));
                        break;
                }
            }
            for (Task task : savedTasks) {
                addAnyTask(task);
                if (task.getId() > generatorId) {
                    generatorId = task.getId();
                }
            }
        }

        String historyString = kvClient.load("history");
        if (historyString == null) {
            System.out.println("Ошибка получения истории с сервера");
        } else {
            List<Integer> history = CSVTaskFormat.historyFromString(historyString);
            for (Integer taskId : history) { //добавляем восстановленную историю в менеджер
                inMemoryHistoryManager.add(findTask(taskId));
            }
        }
    }

    public void setApiTokenDebug() {
        kvClient.setApiTokenDebug();
    }
}
