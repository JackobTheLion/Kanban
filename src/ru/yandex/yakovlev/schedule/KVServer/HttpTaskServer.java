package ru.yandex.yakovlev.schedule.KVServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.yakovlev.schedule.manager.Managers;
import ru.yandex.yakovlev.schedule.manager.TaskManager;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer  {
    TaskManager taskManager = Managers.getDefault();
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();
    HttpServer httpServer;

    public void startServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new tasksHandler());
    }

    static class tasksHandler implements HttpHandler {
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
                case POST_TASK:
                    handlePostTask(exchange);
                    break;
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }

        private void handleGetAllTasks(HttpExchange exchange) {

        }

        private void handleGetTaskById(HttpExchange exchange) {

        }

        private void handleGetHistory(HttpExchange exchange) {

        }

        private void handleGetSubTasksOfEpic(HttpExchange exchange) {

        }

        private void handleGetPrioritizedTasks(HttpExchange exchange) {

        }

        private void handleDeleteAllTasks(HttpExchange exchange) {

        }

        private void handleDeleteTaskById(HttpExchange exchange) {

        }

        private void handlePostTask(HttpExchange exchange) {

        }

        private Endpoint getEndpoint (String requestPath, String requestMethod) {
            String[] path = requestPath.split("/");
            int pathLength = requestPath.length();

            if (requestMethod.equals("POST")) {  //POST /tasks/task/ Body: {Task} json
                return Endpoint.POST_TASK;
            } else if (requestMethod.equals("GET")) {
                if (pathLength == 3 && path[2].equals("task")) { // GET /tasks/task
                    return Endpoint.GET_ALL_TASKS;
                } else if (pathLength == 4) { // GET /tasks/task/?id=
                    return Endpoint.GET_TASK_BY_ID;
                } else if (pathLength == 5) { // GET /tasks/subtask/epic/?id=
                    return Endpoint.GET_SUBTASKS_OF_EPIC;
                } else if (pathLength == 3 && path[2].equals("history")) {
                    return Endpoint.GET_HISTORY; // GET /tasks/history
                } else return Endpoint.GET_PRIORITIZED_TASKS; //GET /tasks/
            } else if (requestMethod.equals("DELETE")) {
                if (pathLength == 3) {
                    return Endpoint.DELETE_ALL_TASKS; // DELETE /tasks/task/
                } else return Endpoint.DELETE_TASK_BY_ID; // GET /tasks/history
            }
            return Endpoint.UNKNOWN;
        }
        private void writeResponse(HttpExchange exchange,
                                   String responseString,
                                   int responseCode) throws IOException {
            if(responseString.isBlank()) {
                exchange.sendResponseHeaders(responseCode, 0);
            } else {
                byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
                exchange.sendResponseHeaders(responseCode, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            }
            exchange.close();
        }

        enum Endpoint {
            GET_ALL_TASKS, // GET /tasks/task
            GET_TASK_BY_ID, // GET /tasks/task/?id=
            POST_TASK, //POST /tasks/task/ Body: {Task} json
            DELETE_ALL_TASKS, // DELETE /tasks/task/
            DELETE_TASK_BY_ID, // DELETE /tasks/task/?id=
            GET_SUBTASKS_OF_EPIC, // GET /tasks/subtask/epic/?id=
            GET_HISTORY, // GET /tasks/history
            GET_PRIORITIZED_TASKS, //GET /tasks/
            UNKNOWN
        }

    }
}


