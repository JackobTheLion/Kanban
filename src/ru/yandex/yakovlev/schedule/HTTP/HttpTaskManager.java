package ru.yandex.yakovlev.schedule.HTTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.yakovlev.schedule.HTTP.adapter.DurationConverter;
import ru.yandex.yakovlev.schedule.HTTP.adapter.LocalDateTimeConverter;
import ru.yandex.yakovlev.schedule.manager.CSVTaskFormat;
import ru.yandex.yakovlev.schedule.manager.FileBackedTasksManager;
import ru.yandex.yakovlev.schedule.tasks.Task;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();
    public KVClient kvClient = new KVClient(path);

    public HttpTaskManager(String path) throws URISyntaxException, IOException, InterruptedException {
        super(path);
    }

    @Override
    public void save() {
        List<Task> allTasks = getPrioritizedTasks();
        String allTasksJson = gson.toJson(allTasks);
        kvClient.saveAtKVServer("tasks", allTasksJson);

        String historyJson = gson.toJson(CSVTaskFormat.historyToString(inMemoryHistoryManager));
        kvClient.saveAtKVServer("history", historyJson);

    }

    public void load() {
        String tasksJson = kvClient.loadFromServer("tasks");
        if (tasksJson == null) {
            System.out.println("Ошибка получения списка задач с сервера");
        } else {
            List<Task> tasksFromJson = gson.fromJson(tasksJson, List.class);
            for (Task task : tasksFromJson) {
                addAnyTask(task);
            }
        }

        String historyString = kvClient.loadFromServer("history");
        if (historyString == null) {
            System.out.println("Ошибка получения истории с сервера");
        } else {
            List<Integer> history = CSVTaskFormat.historyFromString(historyString);
            for (Integer taskId : history) { //добавляем восстановленную историю в менеджер
                inMemoryHistoryManager.add(findTask(taskId));
            }
        }
    }
}
