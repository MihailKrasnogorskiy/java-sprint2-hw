package servers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static servers.HttpTaskServer.gson;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private HttpClient client = HttpClient.newHttpClient();
    private HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    final String URL = "http://localhost:8080/tasks";
    final String jsonTask = "{\"TYPE\":\"TASK\",\"name\":\"Задача 1\",\"description\":\"тестирование кода 1\",\"id\":1," +
            "\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0},\"startTime\":{\"date\":{\"year\":-999999999," +
            "\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}}," +
            "\"endTime\":{\"date\":{\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":30," +
            "\"second\":0,\"nano\":0}}}";
    final String jsonTask1 = "{\"TYPE\":\"TASK\",\"name\":\"Задача 2\",\"description\":\"тестирование кода 1\",\"id\":1," +
            "\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0},\"startTime\":{\"date\":{\"year\":-999999999," +
            "\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}}," +
            "\"endTime\":{\"date\":{\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":30," +
            "\"second\":0,\"nano\":0}}}";
    final String jsonEpic = "{\"TYPE\":\"EPIC\",\"subTasks\":[],\"name\":\"эпик 2\"," +
            "\"description\":\"что-то большое сложное 2\",\"id\":2,\"status\":\"NEW\"}";
    final String jsonEpic1 = "{\"TYPE\":\"EPIC\",\"subTasks\":[{\"TYPE\":\"SUBTASK\",\"epic\":2," +
            "\"name\":\"подзадача 1.1\",\"description\":\"что-то маленькое и лёгкое 1.1\",\"id\":3,\"status\":\"NEW\"}]," +
            "\"name\":\"эпик 2\",\"description\":\"что-то большое сложное 2\",\"id\":2,\"status\":\"NEW\"}";
    final String jsonSubTask = "{\"TYPE\":\"SUBTASK\",\"epic\":2,\"name\":\"подзадача 1.1\"," +
            "\"description\":\"что-то маленькое и лёгкое 1.1\",\"id\":3,\"status\":\"NEW\"}";

    final String jsonSortTask = "[{\"TYPE\":\"TASK\",\"name\":\"Задача 2\",\"description\":\"тестирование кода 1\"," +
            "\"id\":1,\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0},\"startTime\":{\"date\":{" +
            "\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}}," +
            "\"endTime\":{\"date\":{\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":30," +
            "\"second\":0,\"nano\":0}}},{\"TYPE\":\"SUBTASK\",\"epic\":2,\"name\":\"подзадача 1.1\"," +
            "\"description\":\"что-то маленькое и лёгкое 1.1\",\"id\":3,\"status\":\"NEW\"}]";
    final String jsonStory = "[{\"TYPE\":\"TASK\",\"name\":\"Задача 2\",\"description\":\"тестирование кода 1\"," +
            "\"id\":1,\"status\":\"NEW\",\"duration\":{\"seconds\":1800,\"nanos\":0},\"startTime\":{\"date\":{" +
            "\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}}," +
            "\"endTime\":{\"date\":{\"year\":-999999999,\"month\":1,\"day\":1},\"time\":{\"hour\":0,\"minute\":30," +
            "\"second\":0,\"nano\":0}}},{\"TYPE\":\"SUBTASK\",\"epic\":2,\"name\":\"подзадача 1.1\"," +
            "\"description\":\"что-то маленькое и лёгкое 1.1\",\"id\":3,\"status\":\"NEW\"},{\"TYPE\":\"EPIC\"," +
            "\"subTasks\":[{\"TYPE\":\"SUBTASK\",\"epic\":2,\"name\":\"подзадача 1.1\",\"description\":\"" +
            "что-то маленькое и лёгкое 1.1\",\"id\":3,\"status\":\"NEW\"}],\"name\":\"эпик 2\",\"description\":" +
            "\"что-то большое сложное 2\",\"id\":2,\"status\":\"NEW\"}]";

    HttpTaskServerTest() {
    }


    @Test

        //тест ответов /tasks на POST и DELETE запросы
    void test23_tasksResponse() throws IOException, InterruptedException {
        URI uri = URI.create(URL);
        HttpResponse<String> response;
        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("test"))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request1, handler);

        assertEquals(400, response.statusCode());

        HttpRequest request2 = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request2, handler);

        assertEquals(400, response.statusCode());
    }

    @Test
        //тестирование методов сохранения, изменения и удаления задач
    void Test24_taskResponsePOSTAndDELITE() throws IOException, InterruptedException {
        LocalDateTime startTime1 = LocalDateTime.MIN;
        Duration duration = Duration.ofMinutes(30);
        Task task1 = new Task("Задача 1", "тестирование кода 1", duration, startTime1);
        URI uri = URI.create(URL + "/task");
        //сохранение задачи1 на сервер
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response;

        response = client.send(request, handler);
        assertEquals(200, response.statusCode());
        //получение списка всех задач с сервера
        HttpRequest request1 = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request1, handler);
        assertEquals("[" + jsonTask + "]", response.body());
        //сохранение некорректной строки на сервер
        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("test"))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request2, handler);
        assertEquals(400, response.statusCode());

        //получение задачи1 по id
        HttpRequest request3 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/task/?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request3, handler);
        assertEquals(jsonTask, response.body());
        assertEquals(200, response.statusCode());

        TaskBase task2 = tasksDeserialization(response.body());

        task2.setName("Задача 2");
        //отправка существующей задачи повторно
        HttpRequest request4 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        response = client.send(request4, handler);
        assertEquals(400, response.statusCode());
        //обновление задачи 1
        HttpRequest request5 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .uri(URI.create(URL + "/task/?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        response = client.send(request5, handler);
        assertEquals(200, response.statusCode());
        //получение обновлённой задачи
        HttpRequest request6 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/task/?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        response = client.send(request6, handler);
        assertEquals(jsonTask1, response.body());
        assertEquals(200, response.statusCode());

        EpicTask epic2 = new EpicTask("эпик 2", "что-то большое сложное 2");
        //отправка эпика
        HttpRequest request7 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2)))
                .uri(URI.create(URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        response = client.send(request7, handler);
        assertEquals(200, response.statusCode());
        //получение эпика
        HttpRequest request8 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/epic/?id=2"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request8, handler);
        assertEquals(jsonEpic, response.body());
        assertEquals(200, response.statusCode());

        SubTask subTask11 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1", 2);
        //отправка подзадачи
        HttpRequest request9 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask11)))
                .uri(URI.create(URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request9, handler);
        assertEquals(200, response.statusCode());
        //получение подзадачи
        HttpRequest request10 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/subtask/?id=3"))
                .build();
        response = client.send(request10, handler);
        assertEquals(jsonSubTask, response.body());
        assertEquals(200, response.statusCode());
        //получение обновлённого эпика
        HttpRequest request11 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/epic/?id=2"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request11, handler);
        assertEquals(jsonEpic1, response.body());
        assertEquals(200, response.statusCode());
        //получение приоритезированного списка задач
        HttpRequest request15 = HttpRequest.newBuilder()
                .GET()
                .uri((URI.create(URL)))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request15, handler);

        assertEquals(jsonSortTask, response.body());
        //получение истории
        HttpRequest request16 = HttpRequest.newBuilder()
                .GET()
                .uri((URI.create(URL + "/history")))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request16, handler);

        assertEquals(jsonStory, response.body());
        //получение id пика по id подзадачи
        HttpRequest request18 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/subtask/epic/?id=3"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request18, handler);
        assertEquals("2", response.body());
        assertEquals(200, response.statusCode());
        //получение id пика по id подзадачи
        HttpRequest request19 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/epic/subtasks/?id=2"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request19, handler);
        assertEquals("[" + jsonSubTask + "]", response.body());
        assertEquals(200, response.statusCode());
        //удаление всех эпиков
        HttpRequest request12 = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request12, handler);
        assertEquals(200, response.statusCode());
        //удаление задачи по id
        HttpRequest request13 = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(URL + "/task/?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request13, handler);
        assertEquals(200, response.statusCode());
        //получение приоритезированного списка задач
        HttpRequest request14 = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request14, handler);
        //получение истории
        assertEquals("[]", response.body());

        HttpRequest request17 = HttpRequest.newBuilder()
                .GET()
                .uri((URI.create(URL + "/history")))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request17, handler);

        assertEquals("[]", response.body());

    }

    @BeforeEach
    void start() throws IOException {
        server = new HttpTaskServer();
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    //дессериализация задачи
    private TaskBase tasksDeserialization(String body) {
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