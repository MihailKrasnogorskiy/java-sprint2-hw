package clients;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
//класс http клиента для сервера сохранения
public class KVTaskClient {
    private final String API_KEY;
    HttpClient client = HttpClient.newHttpClient();
    HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();


    public KVTaskClient(String url) throws IOException, InterruptedException {
        URI uri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        API_KEY = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        String SAVE_LINK = "http://localhost:8078/POST/save/";
        URI uri = URI.create(SAVE_LINK + key + "?API_KEY=" + API_KEY);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        System.out.println(response.statusCode());
    }

    public String load(String key) throws IOException, InterruptedException {
        String LOAD_LINK = "http://localhost:8078/load/";
        URI uri = URI.create(LOAD_LINK + key + "?API_KEY=" + API_KEY);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        return response.body();
    }
}
