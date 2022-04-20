package servers;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    private HttpTaskServer server = new HttpTaskServer();
    private HttpClient client = HttpClient.newHttpClient();
    private HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
    final String URL = "http://localhost:8080/tasks";

    HttpTaskServerTest() throws IOException {
    }


    @Test
    void test23_tasks() throws IOException, InterruptedException {
        URI uri = URI.create(URL);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response=null;

           response = client.send(request, handler);

        assertEquals("[]",response.body());
        HttpRequest request1 = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("test"))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        response= client.send(request1,handler);
        assertEquals(400,response.statusCode());
    }
}