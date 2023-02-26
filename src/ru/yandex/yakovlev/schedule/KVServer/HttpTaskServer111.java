package ru.yandex.yakovlev.schedule.KVServer;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.yakovlev.schedule.adapter.DurationConverter;
import ru.yandex.yakovlev.schedule.adapter.LocalDateTimeConverter;
import ru.yandex.yakovlev.schedule.manager.Managers;
import ru.yandex.yakovlev.schedule.manager.TaskManager;
import ru.yandex.yakovlev.schedule.tasks.*;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class HttpTaskServer111 {
    public TaskManager taskManager = Managers.getDefault();
    private static final int PORT = 8000;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .registerTypeAdapter(Duration.class, new DurationConverter())
            .create();
    HttpServer httpServer;

    public void startServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new tasksHandler());
        httpServer.start();
        System.out.println("Сервер запущен на порту " + PORT);
    }

    class tasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

            switch (endpoint) {
                case GET_ALL_TASKS:
                    handleGetAllTasks(exchange);
                    break;
                case GET_TASK_BY_ID:
                    handleGetTaskById(exchange);
                    break;
                case GET_EPIC_BY_ID:
                    handleGetEpicById(exchange);
                    break;
                case GET_SUBTASK_BY_ID:
                    handleGetSubtaskById(exchange);
                    break;
                case GET_HISTORY:
                    handleGetHistory(exchange);
                    break;
                case GET_SUBTASKS_OF_EPIC:
                    handleGetSubTasksOfEpic(exchange);
                    break;
                case GET_PRIORITIZED_TASKS:
                    handleGetPrioritizedTasks(exchange);
                    break;
                case DELETE_ALL_TASKS:
                    handleDeleteAllTasks(exchange);
                    break;
                case DELETE_TASK_BY_ID:
                    handleDeleteTaskById(exchange);
                    break;
                case DELETE_SUBTASK_BY_ID:
                    handleDeleteSubTaskById(exchange);
                    break;
                case DELETE_EPIC_BY_ID:
                    handleDeleteEpicById(exchange);
                    break;
                case POST_TASK:
                    handlePostTask(exchange);
                    break;
                case POST_SUBTASK:
                    handlePostSubTask(exchange);
                    break;
                case POST_EPIC:
                    handlePostEpic(exchange);
                    break;
                default:
                    sendText(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private void handleGetAllTasks(HttpExchange exchange) throws IOException {
            List<Task> tasks = taskManager.getAllTasks();
            String tasksJson = gson.toJson(tasks);
            sendText(exchange, tasksJson, 200);
            System.out.println("Все задачи возвращены");
        }

        private void handleGetTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange, "Передан некорректный ID", 400);
                return;
            }
            int id = idOpt.get();
            Task task = taskManager.getTaskById(id);
            if (task == null) {
                sendText(exchange, "Задачи с ID " + id + " не существует", 404);
                return;
            }
            String taskJson = gson.toJson(task);
            sendText(exchange, taskJson, 200);
        }

        private void handleGetEpicById(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange, "Передан некорректный ID", 400);
                return;
            }
            int id = idOpt.get();
            Epic epic = taskManager.getEpicById(id);
            if (epic == null) {
                sendText(exchange, "Эпика с ID " + id + " не существует", 404);
                return;
            }
            String epicJson = gson.toJson(epic);
            sendText(exchange, epicJson, 200);
        }

        private void handleGetSubtaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange, "Передан некорректный ID", 400);
                return;
            }
            int id = idOpt.get();
            SubTask subTask = taskManager.getSubTaskById(id);
            if (subTask == null) {
                sendText(exchange, "Сабтаска с ID " + id + " не существует", 404);
                return;
            }
            String subTaskJson = gson.toJson(subTask);
            sendText(exchange, subTaskJson, 200);
        }

        private void handleGetHistory(HttpExchange exchange) throws IOException {
            List<Task> history = taskManager.getHistory();
            String historyJson = gson.toJson(history);
            sendText(exchange, historyJson, 200);
            System.out.println("История вернулась");
        }

        private void handleGetSubTasksOfEpic(HttpExchange exchange) throws IOException {
            Optional<Integer> epicIdOpt = getTaskId(exchange);
            if(epicIdOpt.isEmpty()) {
                sendText(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            List<SubTask> subsOfEpic = taskManager.getSubTasksByEpicId(epicIdOpt.get());
            String subsOfEpicJson = gson.toJson(subsOfEpic);
            sendText(exchange,subsOfEpicJson, 200);
        }

        private void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
            List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
            String prioritizedTasksJson = gson.toJson(prioritizedTasks);
            sendText(exchange, prioritizedTasksJson, 200);
            System.out.println("Задачи по приоритету вернулись");
        }

        private void handleDeleteAllTasks(HttpExchange exchange) throws IOException {
            taskManager.clearAllTasks();
            sendText(exchange, "Все задачи успешно удалены", 200);
            System.out.println("Все задачи удалены");
        }

        private void handleDeleteTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if(taskIdOpt.isEmpty()) {
                sendText(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            taskManager.deleteTaskById(taskId);
            sendText(exchange, "Задача с ID " + taskId + " удалена", 201);
        }

        private void handleDeleteSubTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if(taskIdOpt.isEmpty()) {
                sendText(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            taskManager.deleteSubTaskById(taskId);
            sendText(exchange, "Сабтаск с ID " + taskId + " удален", 201);
        }

        private void handleDeleteEpicById(HttpExchange exchange) throws IOException {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if(taskIdOpt.isEmpty()) {
                sendText(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            taskManager.deleteEpicById(taskId);
            sendText(exchange, "Эпик с ID " + taskId + " удален", 201);
        }

        private void handlePostTask(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            try {
                Task task = gson.fromJson(body, Task.class);
                if (task.getId() == 0) {
                    taskManager.createTask(task);
                } else taskManager.updateTask(task);
            } catch (JsonSyntaxException e) {
                sendText(exchange, "Получен некорректный JSON", 400);
            }
        }

        private void handlePostSubTask(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            try {
                SubTask subTask = gson.fromJson(body, SubTask.class);
                if (subTask.getId() == 0) {
                    taskManager.createTask(subTask);
                } else taskManager.updateTask(subTask);
            } catch (JsonSyntaxException e) {
                sendText(exchange, "Получен некорректный JSON", 400);
            }
        }

        private void handlePostEpic(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            try {
                Epic epic = gson.fromJson(body, Epic.class);
                if (epic.getId() == 0) {
                    taskManager.createTask(epic);
                } else taskManager.updateTask(epic);
            } catch (JsonSyntaxException e) {
                sendText(exchange, "Получен некорректный JSON", 400);
            }
        }

        private Optional<Integer> getTaskId(HttpExchange exchange) {
            String pathId = exchange.getRequestURI().getQuery().split("=")[1];
            try {
                return Optional.of(Integer.parseInt(pathId));
            } catch (NumberFormatException exception) {
                return Optional.empty();
            }
        }

        private Endpoint getEndpoint (String requestPath, String requestMethod) {
            if (requestMethod.equals("POST")) {  //POST /tasks/task/ Body: {Task} json
                if (Pattern.matches("^/tasks/task", requestPath)) {
                    return Endpoint.POST_TASK;
                } else if (Pattern.matches("^/tasks/subtask", requestPath)) {
                    return Endpoint.POST_SUBTASK;
                } else if (Pattern.matches("^/tasks/epic", requestPath)) {
                    return Endpoint.POST_EPIC;
                }
            } else if (requestMethod.equals("GET")) {
                if (Pattern.matches("^/tasks/task/", requestPath)) {
                    return Endpoint.GET_TASK_BY_ID;
                } else if (Pattern.matches("^/tasks/epic/", requestPath)) {
                    return Endpoint.GET_EPIC_BY_ID;
                } else if (Pattern.matches("^/tasks/subtask/", requestPath)) {
                    return Endpoint.GET_SUBTASK_BY_ID;
                } else if (Pattern.matches("^/tasks/subtask/epic/", requestPath)) {
                    return Endpoint.GET_SUBTASKS_OF_EPIC;
                } else if (Pattern.matches("^/tasks/history", requestPath)) {
                    return Endpoint.GET_HISTORY;
                } else if (Pattern.matches("^/tasks/task$", requestPath)) {
                    return Endpoint.GET_ALL_TASKS;
                } else if (Pattern.matches("^/tasks$", requestPath)) {
                    return Endpoint.GET_PRIORITIZED_TASKS;
                }
            } else if (requestMethod.equals("DELETE")) {
                if (Pattern.matches("^/tasks/task$", requestPath)) {
                    return Endpoint.DELETE_ALL_TASKS; // DELETE /tasks/task/
                } else if ((Pattern.matches("^/tasks/task/", requestPath))) {
                    return Endpoint.DELETE_TASK_BY_ID; // DELETE /tasks/task/?id=
                } else if ((Pattern.matches("^/tasks/subtask/", requestPath))) {
                    return Endpoint.DELETE_SUBTASK_BY_ID; // DELETE /tasks/subtask/?id=
                } else if ((Pattern.matches("^/tasks/epic/", requestPath))) {
                    return Endpoint.DELETE_EPIC_BY_ID; // DELETE /tasks/epic/?id=
                }
            }
            return Endpoint.UNKNOWN;
        }

        protected void sendText(HttpExchange h, String text, int code) throws IOException {
            byte[] resp = text.getBytes(DEFAULT_CHARSET);
            h.getResponseHeaders().add("Content-Type", "application/json");
            h.sendResponseHeaders(code, resp.length);
            h.getResponseBody().write(resp);
            h.close();
        }
    }

    private enum Endpoint {
        GET_ALL_TASKS, // GET /tasks/task
        GET_TASK_BY_ID, // GET /tasks/task/?id=
        GET_SUBTASK_BY_ID, // GET /tasks/subtask/?id=
        GET_EPIC_BY_ID, // GET /tasks/epic/?id=
        POST_TASK, //POST /tasks/task/ Body: {Task} json
        POST_EPIC, //POST /tasks/epic/ Body: {Task} json
        POST_SUBTASK, //POST /tasks/subtask/ Body: {Task} json
        DELETE_ALL_TASKS, // DELETE /tasks/task/
        DELETE_TASK_BY_ID, // DELETE /tasks/task/?id=
        DELETE_SUBTASK_BY_ID, // DELETE /tasks/subtask/?id=
        DELETE_EPIC_BY_ID, // DELETE /tasks/epic/?id=
        GET_SUBTASKS_OF_EPIC, // GET /tasks/subtask/epic/?id=
        GET_HISTORY, // GET /tasks/history
        GET_PRIORITIZED_TASKS, //GET /tasks/
        UNKNOWN
    }
}


