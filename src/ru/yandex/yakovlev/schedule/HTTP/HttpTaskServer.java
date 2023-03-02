package ru.yandex.yakovlev.schedule.HTTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.yakovlev.schedule.manager.Managers;
import ru.yandex.yakovlev.schedule.manager.TaskManager;
import ru.yandex.yakovlev.schedule.HTTP.adapter.*;
import ru.yandex.yakovlev.schedule.tasks.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

    public class HttpTaskServer {

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        Task task1 = new Task("задача 1","описание 1", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        task1.setDuration(60);
        httpTaskServer.taskManager.createTask(task1);

        Task task2 = new Task("задача 2","описание 2", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2023,4,15,8, 0));
        task2.setDuration(60);
        httpTaskServer.taskManager.createTask(task2);

        Task task3 = new Task("задача 2","описание 2", Status.NEW);
        httpTaskServer.taskManager.createTask(task3);

        Epic epic  = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        int epicId = httpTaskServer.taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Сабтаск 1", "Описание сабтаска 1", epicId, Status.NEW );
        httpTaskServer.taskManager.createSubTask(subTask);
    }

    private final int PORT = 8000;
    private HttpServer httpServer;
    private final TaskManager taskManager;

    private final Gson gson = new GsonBuilder()
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
    public TaskManager getTaskManager() {
        return taskManager;
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
                case GET_SUBTASK_BY_ID:
                    getSubTaskByID(exchange);
                    break;
                case GET_EPIC_BY_ID:
                    getEpicById(exchange);
                    break;
                case GET_HISTORY:
                    getHistory(exchange);
                    break;
                case GET_PRIORITIZED:
                    getPrioritized(exchange);
                    break;
                case GET_SUBS_OF_EPIC:
                    getSubsOfEpic(exchange);
                    break;
                case POST_TASK:
                    postTask(exchange);
                    break;
                case POST_EPIC:
                    postEpic(exchange);
                    break;
                case POST_SUBTASK:
                    postSubTask(exchange);
                    break;
                case DELETE_ALL_TASKS:
                    deleteAllTasks(exchange);
                    break;
                case DELETE_ALL_EPICS:
                    deleteAllEpics(exchange);
                    break;
                case DELETE_ALL_SUBTASKS:
                    deleteAllSubTasks(exchange);
                    break;
                case DELETE_TASK_BY_ID:
                    deleteTaskById(exchange);
                    break;
                case DELETE_SUBTASK_BY_ID:
                    deleteSubTaskById(exchange);
                    break;
                case DELETE_EPIC_BY_ID:
                    deleteEpicById(exchange);
                    break;
                case ENDPOINT_UNKNOWN:
                    sendText(exchange, "Такого эндпоинта не существует", 404);
                    break;
            }
        }



        private void getAllTasks(HttpExchange exchange) throws IOException {
            List<Task> tasks = taskManager.getAllTasks();
            sendText(exchange,gson.toJson(tasks), 200);
        }

        private void getAllSubTasks(HttpExchange exchange) throws IOException {
            List<SubTask> subTasks = taskManager.getAllSubTasks();
            sendText(exchange,gson.toJson(subTasks), 200);
        }

        private void getAllEpics(HttpExchange exchange) throws IOException {
            List<Epic> epics = taskManager.getAllEpicTasks();
            sendText(exchange,gson.toJson(epics), 200);
        }

        private void getTaskByID(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange,"Неверный ID", 400);
                return;
            }

            int id  = idOpt.get();
            Task task = taskManager.getTaskById(id);
            if (task == null) {
                sendText(exchange, "Таска с ID " + id + " не существует", 404);
                return;
            }
            sendText(exchange, gson.toJson(task), 200);
        }

        private void getSubTaskByID(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange,"Неверный ID", 400);
                return;
            }

            int id  = idOpt.get();
            SubTask subTask = taskManager.getSubTaskById(id);
            if (subTask == null) {
                sendText(exchange, "Сабтаска с ID " + id + " не существует", 404);
                return;
            }
            sendText(exchange, gson.toJson(subTask), 200);
        }

        private void getEpicById(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange,"Неверный ID", 400);
                return;
            }

            int id  = idOpt.get();
            Epic epic = taskManager.getEpicById(id);
            if (epic == null) {
                sendText(exchange, "Эпика с ID " + id + " не существует", 404);
                return;
            }
            sendText(exchange, gson.toJson(epic), 200);
        }

        private void getHistory(HttpExchange exchange) throws IOException {
            List<Task> history = taskManager.getHistory();
            if (history.isEmpty()) {
                sendText(exchange, "История пуста", 200);
                return;
            }
            sendText(exchange, gson.toJson(history), 200);
        }

        private void getPrioritized(HttpExchange exchange) throws IOException {
            List<Task> prioritized = taskManager.getPrioritizedTasks();
            if (prioritized.isEmpty()) {
                sendText(exchange, "Не создано ни одной задачи", 200);
                return;
            }
            sendText(exchange, gson.toJson(prioritized), 200);
        }

        private void getSubsOfEpic(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange,"Неверный ID", 400);
                return;
            }
            int id = idOpt.get();
            List<SubTask> subTasks = taskManager.getSubTasksByEpicId(id);
            if (subTasks == null) {
                sendText(exchange,"Эпика с ID " + id + " не существует", 404);
                return;
            }
            sendText(exchange, gson.toJson(subTasks), 200);
        }

        private void postTask(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes());
            try {
                Task task = gson.fromJson(body, Task.class);
                if (task.getId() == 0) {
                    int id = taskManager.createTask(task);
                    if (id == -1) {
                        sendText(exchange, "Не удалось создать задачу. Слот занят.", 400);
                        return;
                    }
                    sendText(exchange, "Задача успешно создана. ID " + id, 201);
                } else {
                    if (taskManager.getTaskById(task.getId()) == null) {
                        sendText(exchange, "Невозможно обновить задачу неверный ID", 404);
                        return;
                    }
                    taskManager.updateTask(task);
                    sendText(exchange, "Задача успешно обновлена.", 201);
                }
            } catch (JsonSyntaxException e) {
                sendText(exchange, "Получен некорректный JSON", 400);
            }
        }

        private void postEpic(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes());
            try {
                Epic epic = gson.fromJson(body, Epic.class);
                if (epic.getId() == 0) {
                    int id = taskManager.createEpic(epic);
                    sendText(exchange, "Эпик успешно создан. ID " + id, 201);
                } else {
                    if (taskManager.getEpicById(epic.getId()) == null) {
                        sendText(exchange, "Невозможно обновить эпик неверный ID", 404);
                        return;
                    }
                    taskManager.updateEpic(epic);
                    sendText(exchange, "Эпик успешно обновлен.", 201);
                }
            } catch (JsonSyntaxException e) {
                sendText(exchange, "Получен некорректный JSON", 400);
            }
        }

        private void postSubTask(HttpExchange exchange) throws IOException {
            String body = new String(exchange.getRequestBody().readAllBytes());
            try {
                SubTask subTask = gson.fromJson(body, SubTask.class);
                if (subTask.getId() == 0) {
                    int id = taskManager.createSubTask(subTask);
                    if (id == -1) {
                        sendText(exchange, "Не удалось создать сабтаск. Слот занят.", 400);
                        return;
                    }
                    sendText(exchange, "Сабтаск успешно создан. ID " + id, 201);
                } else {
                    if (taskManager.getSubTaskById(subTask.getId()) == null) {
                        sendText(exchange, "Невозможно обновить сабтаск неверный ID", 404);
                        return;
                    }
                    taskManager.updateSubtask(subTask);
                    sendText(exchange, "Сабтаск успешно обновлен.", 201);
                }
            } catch (JsonSyntaxException e) {
                sendText(exchange, "Получен некорректный JSON", 400);
            }
        }

        private void deleteAllTasks(HttpExchange exchange) throws IOException {
            taskManager.clearTasks();
            sendText(exchange, "Все задачи успешно удалены", 201);
        }

        private void deleteAllEpics(HttpExchange exchange) throws IOException {
            taskManager.clearEpics();
            sendText(exchange, "Все эпики успешно удалены", 201);
        }

        private void deleteAllSubTasks(HttpExchange exchange) throws IOException {
            taskManager.clearSubTasks();
            sendText(exchange, "Все сабтаски успешно удалены", 201);
        }

        private void deleteTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange,"Неверный ID", 400);
                return;
            }
            int id = idOpt.get();
            if (!getTaskManager().doesTaskExist(id)) {
                sendText(exchange,"Задача с ID " + id + " не существует.", 404);
                return;
            }
            taskManager.deleteTaskById(id);
            sendText(exchange, "Задача с ID " + id + " удалена.", 201);
        }

        private void deleteSubTaskById(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange,"Неверный ID", 400);
                return;
            }
            int id = idOpt.get();
            if (!getTaskManager().doesTaskExist(id)) {
                sendText(exchange,"Сабтаск с ID " + id + " не существует.", 404);
                return;
            }
            taskManager.deleteSubTaskById(id);
            sendText(exchange, "Сабтаск с ID " + id + " удален.", 201);
        }

        private void deleteEpicById(HttpExchange exchange) throws IOException {
            Optional<Integer> idOpt = getTaskId(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange,"Неверный ID", 400);
                return;
            }
            int id = idOpt.get();
            if (!getTaskManager().doesTaskExist(id)) {
                sendText(exchange,"Эпик с ID " + id + " не существует.", 404);
                return;
            }
            taskManager.deleteEpicById(id);
            sendText(exchange, "Эпик с ID " + id + " удален.", 201);
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

            switch (method) {
                case "GET":
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
                    } else if (Pattern.matches("^/tasks/subtask/epic/", path)) {
                        return Endpoint.GET_SUBS_OF_EPIC;
                    }
                    break;
                case "POST":
                    if (Pattern.matches("^/tasks/task/", path)) {
                        return Endpoint.POST_TASK;
                    } else if (Pattern.matches("^/tasks/subtask/", path)) {
                        return Endpoint.POST_SUBTASK;
                    } else if (Pattern.matches("^/tasks/epic/", path)) {
                        return Endpoint.POST_EPIC;
                    }
                    break;
                case "DELETE":
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
                    break;
            }
            return Endpoint.ENDPOINT_UNKNOWN;
        }

        private void sendText(HttpExchange exchange, String text, int code) throws IOException {
            byte[] response = text.getBytes(UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
