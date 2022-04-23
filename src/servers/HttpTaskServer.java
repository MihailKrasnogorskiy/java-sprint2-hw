package servers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.Managers;
import controllers.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

//класс сервера api
public class HttpTaskServer {
    private static final int PORT = 8080;
    public static Gson gson = new Gson();
    private static TaskManager manager;

    static {
        try {
            manager = Managers.getHTTPTaskManager();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

    {
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.start();
    }


    public HttpTaskServer() throws IOException {
    }

    public void stop() {
        httpServer.stop(0);
    }

    //класс для обработки запроса списка приоритезированных задач
    private static class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String response = "";
            if ("GET".equals(method)) {
                response = gson.toJson(manager.getSortTask());
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                httpExchange.sendResponseHeaders(400, 0);
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    //класс для обработки запросов задач
    private static class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String response = "";
            String query = httpExchange.getRequestURI().getRawQuery();
            int id = -1;
            if (query != null) {
                id = Integer.parseInt(query.split("=")[1]);
            }
            switch (method) {
                case "GET":
                    if (id == -1) {
                        response = gson.toJson(manager.getAllTask());

                    } else {
                        response = gson.toJson(manager.getAnyTaskByID(id));
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "POST":
                    try {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        if (id == -1) {
                            //сохраняем задачу
                            manager.addTask(tasksDeserialization(body));
                        } else {
                            //обновляем задачу
                            manager.updateTask(id, tasksDeserialization(body));
                        }
                    } catch (IllegalArgumentException e) {
                        httpExchange.sendResponseHeaders(400, 0);
                        break;
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "DELETE":
                    if (id == -1) {
                        manager.removeAllTask();
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        //удаляем задачу по id
                        if (manager.removeById(id)) {
                            response = "Задача " + id + " удалена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            throw new IllegalArgumentException("id not found");
                        }
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(400, 0);
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    //класс для обработки запросов эпиков
    private static class EpicHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            System.out.println(method);
            String response = "";
            String path = httpExchange.getRequestURI().getPath();
            System.out.println(path);
            String[] splitPath = path.split("/");
            String query = httpExchange.getRequestURI().getRawQuery();
            int id = -1;
            if (query != null) {
                id = Integer.parseInt(query.split("=")[1]);
            }
            switch (method) {
                case "GET":
                    if (id == -1) {
                        response = gson.toJson(manager.getAllEpicTask());
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        if (splitPath.length == 3) {
                            response = gson.toJson(manager.getAnyTaskByID(id));
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            if (splitPath.length == 4) {
                                //возвращаем список подзадач эпика
                                if (splitPath[2].equals("epic") & splitPath[3].equals("subtasks")) {
                                    response = gson.toJson(manager.getSubTaskFromEpic(manager.getEpicTaskById(id)));
                                    httpExchange.sendResponseHeaders(200, 0);
                                } else {
                                    httpExchange.sendResponseHeaders(400, 0);
                                    throw new IllegalArgumentException("unregistered endpoint");
                                }
                            }
                        }
                    }
                    break;
                case "POST":
                    try {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        if (id == -1) {
                            //сохраняем задачу
                            manager.addTask(tasksDeserialization(body));
                        } else {
                            //обновляем задачу
                            manager.updateTask(id, tasksDeserialization(body));
                        }
                    } catch (IllegalArgumentException e) {
                        httpExchange.sendResponseHeaders(400, 0);
                        break;
                    }
                    httpExchange.sendResponseHeaders(200, 0);

                    break;
                case "DELETE":
                    if (id == -1) {
                        //удаляем все эпики
                        manager.removeAllEpic();
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        //удаляем задачу по id
                        if (manager.removeById(id)) {
                            response = "Задача " + id + " удалена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            throw new IllegalArgumentException("id not found");
                        }
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(400, 0);
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    //класс для обработки запросов подзадач
    private static class SubtaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String response = "";
            String path = httpExchange.getRequestURI().getPath();
            String[] splitPath = path.split("/");
            String query = httpExchange.getRequestURI().getRawQuery();
            int id = -1;
            if (query != null) {
                id = Integer.parseInt(query.split("=")[1]);
            }
            switch (method) {
                case "GET":
                    if (id == -1) {
                        response = gson.toJson(manager.getAllEpicTask());
                    }
                    if (splitPath.length == 3) {
                        response = gson.toJson(manager.getAnyTaskByID(id));
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        if (splitPath.length == 4) {
                            //возвращаем id эпика по подзадаче
                            if (splitPath[2].equals("subtask") & splitPath[3].equals("epic")) {
                                response += manager.getSubTaskById(id).getEpic();
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                httpExchange.sendResponseHeaders(400, 0);
                                throw new IllegalArgumentException("unregistered endpoint");
                            }
                        }
                    }
                    break;
                case "POST":
                    try {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                        if (id == -1) {
                            //сохраняем задачу
                            manager.addTask(tasksDeserialization(body));
                        } else {
                            //обновляем задачу
                            manager.updateTask(id, tasksDeserialization(body));
                        }
                    } catch (IllegalArgumentException e) {
                        httpExchange.sendResponseHeaders(400, 0);
                        break;
                    }
                    httpExchange.sendResponseHeaders(200, 0);

                    break;
                case "DELETE":
                    if (id == -1) {
                        //удаляем все подзадачи
                        manager.removeAllSubTask();
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        //удаляем задачу по id
                        if (manager.removeById(id)) {
                            response = "Задача " + id + " удалена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            throw new IllegalArgumentException("id not found");
                        }
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(400, 0);
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    //класс для обработки запроса истории задач
    private static class HistoryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String response = "";
            if ("GET".equals(method)) {
                response = gson.toJson(manager.history());
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                httpExchange.sendResponseHeaders(400, 0);
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    //десериализация входящих задач
    private static TaskBase tasksDeserialization(String body) {
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("request body is not json object");
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("TYPE").getAsString();
        switch (type) {
            case "TASK":
                return gson.fromJson(body, Task.class);
            case "SUBTASK":
                return gson.fromJson(body, SubTask.class);
            case "EPIC":
                return gson.fromJson(body, EpicTask.class);
            default:
                throw new IllegalArgumentException("request body is not tasks");
        }
    }
}
