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

    HttpTaskServerTest() throws IOException {
    }


    @Test
    void test23_tasksResponse() throws IOException, InterruptedException {
        URI uri = URI.create(URL);
//        HttpRequest request = HttpRequest.newBuilder()
//                .GET()
//                .uri(uri)
//                .version(HttpClient.Version.HTTP_1_1)
//                .build();
        HttpResponse<String> response = null;
//
//        response = client.send(request, handler);
//
//        assertEquals("[]", response.body());

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
    void Test24_taskResponsePOST() throws IOException, InterruptedException {
        LocalDateTime startTime1 = LocalDateTime.MIN;
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime startTime2 = startTime1.plusHours(1);
        Task task1 = new Task("Задача 1", "тестирование кода 1", duration, startTime1);
        URI uri = URI.create(URL + "/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = null;

        response = client.send(request, handler);
        assertEquals(200, response.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request1, handler);
        assertEquals("[" + jsonTask + "]", response.body());

        HttpRequest request2 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("test"))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request2, handler);
        assertEquals(400, response.statusCode());


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

        HttpRequest request4 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        response = client.send(request4, handler);
        assertEquals(400, response.statusCode());

        System.out.println(task2);
        HttpRequest request5 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .uri(URI.create(URL + "/task/?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        response = client.send(request5, handler);
        assertEquals(200, response.statusCode());

        HttpRequest request6 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/task/?id=1"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request6, handler);
        assertEquals(jsonTask1, response.body());
        assertEquals(200, response.statusCode());

        EpicTask epic2 = new EpicTask("эпик 2", "что-то большое сложное 2");

        HttpRequest request7 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic2)))
                .uri(URI.create(URL + "/epic"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        response = client.send(request7, handler);
        assertEquals(200, response.statusCode());

        HttpRequest request8 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/epic/?id=2"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request8, handler);
        assertEquals(jsonEpic, response.body());
        assertEquals(200, response.statusCode());

        SubTask subTask11 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1", epic2.getId(),
                duration,startTime2);
        System.out.println(gson.toJson(subTask11));
        HttpRequest request9 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask11)))
                .uri(URI.create(URL + "/subtask"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        response = client.send(request9, handler);
        assertEquals(200, response.statusCode());

        HttpRequest request10 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/subtask/?id=3"))
                .build();
        response = client.send(request10, handler);
        assertEquals(jsonSubTask, response.body());
        assertEquals(200, response.statusCode());

        HttpRequest request11 = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL + "/epic/?id=2"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response = client.send(request11, handler);
        assertEquals(jsonEpic1, response.body());
        assertEquals(200, response.statusCode());
    }

    @BeforeEach
    void start() throws IOException {
        server = new HttpTaskServer();
    }

    @AfterEach
    void stop() {
        server.stop();
    }

    private TaskBase tasksDeserialization(String body) {
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("request body is not json object");
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("TYPE").getAsString();
        if (type.equals("TASK")) {
            return gson.fromJson(body, Task.class);
        } else if (type.equals("SUBTASK")) {
            return gson.fromJson(body, SubTask.class);
        } else if (type.equals("EPIC")) {
            return gson.fromJson(body, EpicTask.class);
        } else {
            throw new IllegalArgumentException("request body is not tasks");
        }
    }
}