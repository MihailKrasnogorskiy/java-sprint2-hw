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
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start();
    }


    public HttpTaskServer() throws IOException {
    }

    public void stop() {
        httpServer.stop(0);
    }

    private static class TaskHandler implements HttpHandler {
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
                        if (splitPath.length == 2) {
                            //возвращаем сортированный список всех задач и подзадач
                            response = gson.toJson(manager.getSortTask());
                        } else if (splitPath[2].equals("task")) {
                            //возвращаем список всех задач
                            response = gson.toJson(manager.getAllTask());
                        } else if (splitPath[2].equals("subtask")) {
                            //возвращаем список всех подзадач
                            response = gson.toJson(manager.getAllSubTask());
                        } else if (splitPath[2].equals("epic")) {
                            //возвращаем список всех эпиков
                            response = gson.toJson(manager.getAllEpicTask());
                        } else if (splitPath[2].equals("history")) {
                            //возвращаем историю
                            response = gson.toJson(manager.history());
                        } else {
                            httpExchange.sendResponseHeaders(400, 0);
                            throw new IllegalArgumentException("unregistered endpoint");
                        }
                    } else {
                        if (splitPath.length == 4) {
                            //возвращаем id эпика по подзадаче
                            if (splitPath[2].equals("subtask") & splitPath[3].equals("epic")) {
                                response += manager.getSubTaskById(id).getEpic();
                                //возвращаем список подзадач эпика
                            } else if (splitPath[2].equals("epic") & splitPath[3].equals("subtasks")) {
                                response = gson.toJson(manager.getSubTaskFromEpic(manager.getEpicTaskById(id)));
                            } else {
                                httpExchange.sendResponseHeaders(400, 0);
                                throw new IllegalArgumentException("unregistered endpoint");
                            }
                        } else {
                            response = gson.toJson(manager.getAnyTaskByID(id));
                        }
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                case "POST":
                    if (splitPath.length == 2) {
                        httpExchange.sendResponseHeaders(400, 0);
                    } else {
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
                    }
                    break;
                case "DELETE":
                    if (splitPath.length == 2) {
                        httpExchange.sendResponseHeaders(400, 0);
                    } else {
                        if (id == -1) {
                            if ("task".equals(splitPath[2])) {//удаляем все задачи
                                manager.removeAllTask();
                            } else if ("subtask".equals(splitPath[2])) {//удаляем все подзадачи
                                manager.removeAllSubTask();
                            } else if (splitPath[2].equals("epic")) {//удаляем все эпики
                                manager.removeAllEpic();
                            } else {
                                httpExchange.sendResponseHeaders(400, 0);
                                throw new IllegalArgumentException("unregistered endpoint");
                            }
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
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(400, 0);
                    throw new IllegalArgumentException("unregistered http method");
            }

            try (
                    OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
