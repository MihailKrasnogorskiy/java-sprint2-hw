package servers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controllers.FileBackedTasksManager;
import controllers.Managers;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static FileBackedTasksManager manager = (FileBackedTasksManager) Managers.getRestorableManagerForTests();
    private static Gson gson = new Gson();
    private static final int PORT = 8080;
    public HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

    {
       // httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/tasks/task/", new TaskHandler());
        httpServer.createContext("/tasks/epictask/", new EpicTaskHandler());
        httpServer.createContext("/tasks/subtask/", new SubTaskHandler());

        httpServer.start();
    }


    public HttpTaskServer() throws IOException {
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String response = gson.toJson(manager.getAllTasks());
            httpExchange.sendResponseHeaders(200, 0);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String response = "";
            String parameters = httpExchange.getRequestURI().getQuery();
            System.out.println(parameters);
            int id = 0;
            if(parameters!=null){
                id = Integer.parseInt(parameters.split("=")[1]);
            }
            switch (method) {
                case "GET":
                    if (id == 0) {
                        System.out.println("Отдаю все таски");
                        response = gson.toJson(manager.getAllTask());

                    } else {
                        System.out.println("Отдаю такску " + id);
                        response = gson.toJson(manager.getTaskById(id));
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "POST":
                    System.out.println("Началась обработка пост");
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    System.out.println(body);
                    if (id == 0) {
                        System.out.println(gson.fromJson(body, Task.class));
                        manager.addTask(gson.fromJson(body, Task.class));
                    } else {
                        manager.updateTask(id, gson.fromJson(body, Task.class));
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "DELETE":
                    if (id == 0) {
                        manager.removeAllTask();
                    } else {
                        if (manager.removeById(id)) {
                            response = "Задача " + id + " удалена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            throw new IllegalArgumentException("id not found");
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("unregistered http method");
            }

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private static class SubTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String response = "";
            String parameters = httpExchange.getRequestURI().getQuery();
            System.out.println(parameters);
            int id = 0;
            if(parameters!=null){
                id = Integer.parseInt(parameters.split("=")[1]);
            }
            switch (method) {
                case "GET":
                    if (id == 0) {
                        response = gson.toJson(manager.getAllSubTask());

                    } else {
                        response = gson.toJson(manager.getSubTaskById(id));
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    if (id == 0) {
                        manager.addTask(gson.fromJson(body, Task.class));
                    } else {
                        manager.updateTask(id, gson.fromJson(body, Task.class));
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "DELETE":
                    if (id == 0) {
                        manager.removeAllSubTask();
                    } else {
                        if (manager.removeById(id)) {
                            response = "Задача " + id + " удалена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            throw new IllegalArgumentException("id not found");
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("unregistered http method");
            }

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private static class EpicTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String method = httpExchange.getRequestMethod();
            String response = "";
            String parameters = httpExchange.getRequestURI().getQuery();
            System.out.println(parameters);
            int id = 0;
            if(parameters!=null){
                id = Integer.parseInt(parameters.split("=")[1]);
            }
            switch (method) {
                case "GET":
                    if (id == 0) {
                        response = gson.toJson(manager.getAllEpicTask());

                    } else {
                        response = gson.toJson(manager.getEpicTaskById(id));
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    if (id == 0) {
                        manager.addTask(gson.fromJson(body, Task.class));
                    } else {
                        manager.updateTask(id, gson.fromJson(body, Task.class));
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "DELETE":
                    if (id == 0) {
                        manager.removeAllEpic();
                    } else {
                        if (manager.removeById(id)) {
                            response = "Задача " + id + " удалена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            throw new IllegalArgumentException("id not found");
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("unregistered http method");
            }

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
