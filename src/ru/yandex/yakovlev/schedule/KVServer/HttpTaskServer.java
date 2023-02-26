package ru.yandex.yakovlev.schedule.KVServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.yakovlev.schedule.adapter.DurationConverter;
import ru.yandex.yakovlev.schedule.adapter.LocalDateTimeConverter;
import ru.yandex.yakovlev.schedule.manager.Managers;
import ru.yandex.yakovlev.schedule.manager.TaskManager;
import ru.yandex.yakovlev.schedule.tasks.Epic;
import ru.yandex.yakovlev.schedule.tasks.SubTask;
import ru.yandex.yakovlev.schedule.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8000;
    private HttpServer httpServer;
    public TaskManager taskManager;

    private Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime .class, new LocalDateTimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();

    public HttpTaskServer() {
        this.taskManager = Managers.getDefault();
    }

    public void start() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/", new taskHandler());
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks/");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    private class taskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange);

            switch (endpoint) {
                case GET_TASKS:
                    getAllTasks(exchange);
                    break;
                case GET_SUBTASKS:
                    getAllSubTasks(exchange);
                    break;
                case GET_EPICS:
                    getAllEpics(exchange);
                    break;
                case GET_TASK_BY_ID:
                    getTaskByID(exchange);
                    break;
                case ENDPOINT_UNKNOWN:
                    sendText(exchange, "Такого эндпоинта не существует", 404);
                    break;
            }
        }

        private void getAllTasks(HttpExchange exchange) throws IOException {
            List<Task> tasks = taskManager.getAllTasks();
            String tasksJson = gson.toJson(tasks);
            sendText(exchange,tasksJson, 200);
        }

        private void getAllSubTasks(HttpExchange exchange) throws IOException {
            List<SubTask> subTasks = taskManager.getAllSubTasks();
            String subTasksJson = gson.toJson(subTasks);
            sendText(exchange,subTasksJson, 200);
        }

        private void getAllEpics(HttpExchange exchange) throws IOException {
            List<Epic> epics = taskManager.getAllEpicTasks();
            String epicJson = gson.toJson(epics);
            sendText(exchange,epicJson, 200);
        }

        private void getTaskByID(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange,"Неверный ID", 400);
            }

            int id  = idOpt.get();
            Task task = taskManager.getTaskById(id);
            String taskJson = gson.toJson(task);
            sendText(exchange, taskJson, 200);
        }

        private Optional<Integer> getTaskId(HttpExchange exchange) {
            String pathId = exchange.getRequestURI().getQuery().split("=")[1];
            try {
                return Optional.of(Integer.parseInt(pathId));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }

        private Endpoint getEndpoint(HttpExchange exchange) {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();

            if (method.equals("GET")) {
                if (Pattern.matches("^/tasks/$", path)) {
                    return Endpoint.GET_PRIORITIZED;
                } else if (Pattern.matches("^/tasks/task/", path) && query == null) {
                    return Endpoint.GET_TASKS;
                } else if (Pattern.matches("^/tasks/subtask/", path) && query == null) {
                    return Endpoint.GET_SUBTASKS;
                } else if (Pattern.matches("^/tasks/epic/", path) && query == null) {
                    return Endpoint.GET_EPICS;
                } else if (Pattern.matches("^/tasks/task/", path)) {
                    return Endpoint.GET_TASK_BY_ID;
                } else if (Pattern.matches("^/tasks/subtask/", path)) {
                    return Endpoint.GET_SUBTASK_BY_ID;
                } else if (Pattern.matches("^/tasks/epic/", path)) {
                    return Endpoint.GET_EPIC_BY_ID;
                } else if (Pattern.matches("^/tasks/history/", path)) {
                    return Endpoint.GET_HISTORY;
                } else if (Pattern.matches("^/tasks/subtasks/epic/", path)) {
                    return Endpoint.GET_SUBS_OF_EPIC;
                }
            } else if (method.equals("POST")) {
                if (Pattern.matches("^/tasks/task/", path)) {
                    return Endpoint.POST_TASK;
                } else if (Pattern.matches("^/tasks/subtask/", path)) {
                    return Endpoint.POST_SUBTASK;
                } else if (Pattern.matches("^/tasks/epic/", path)) {
                    return Endpoint.POST_EPIC;
                }
            } else if (method.equals("DELETE")) {
                if (Pattern.matches("^/tasks/task/", path) && query == null) {
                    return Endpoint.DELETE_ALL_TASKS;
                } else if (Pattern.matches("^/tasks/subtask/", path) && query == null) {
                    return Endpoint.DELETE_ALL_SUBTASKS;
                } else if (Pattern.matches("^/tasks/epic/", path) && query == null) {
                    return Endpoint.DELETE_ALL_EPICS;
                } else if (Pattern.matches("^/tasks/task/", path)) {
                    return Endpoint.DELETE_TASK_BY_ID;
                } else if (Pattern.matches("^/tasks/subtask/", path)) {
                    return Endpoint.DELETE_SUBTASK_BY_ID;
                } else if (Pattern.matches("^/tasks/epic/", path)) {
                    return Endpoint.DELETE_EPIC_BY_ID;
                }
            }
            return Endpoint.ENDPOINT_UNKNOWN;
        }

        private String readText(HttpExchange h) throws IOException {
            return new String(h.getRequestBody().readAllBytes(), UTF_8);
        }

        private void sendText(HttpExchange exchange, String text, int code) throws IOException {
            byte[] response = text.getBytes(UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(code, response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        }
    }

    private enum Endpoint {
        GET_TASKS,
        GET_SUBTASKS,
        GET_EPICS,
        GET_TASK_BY_ID,
        GET_SUBTASK_BY_ID,
        GET_EPIC_BY_ID,
        GET_HISTORY,
        GET_PRIORITIZED,
        GET_SUBS_OF_EPIC,
        POST_TASK,
        POST_SUBTASK,
        POST_EPIC,
        DELETE_ALL_TASKS,
        DELETE_ALL_EPICS,
        DELETE_ALL_SUBTASKS,
        DELETE_TASK_BY_ID,
        DELETE_EPIC_BY_ID,
        DELETE_SUBTASK_BY_ID,
        ENDPOINT_UNKNOWN
    }
}
