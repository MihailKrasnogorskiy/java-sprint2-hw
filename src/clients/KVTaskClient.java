package clients;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
//класс http клиента для сервера сохранения
public class KVTaskClient {
    private String API_KEY = "?API_KEY=";
    private final String URL;
    private HttpClient client = HttpClient.newHttpClient();
    private HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    public KVTaskClient(String url) throws IOException, InterruptedException {
        URL = url;
        registration();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        final String SAVE_LINK = URL + "/save/";
        URI uri = URI.create(SAVE_LINK + key + API_KEY);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        if (response.statusCode() == 503) {
            throw new IllegalStateException("save failed, please try again ");
        }
        if (response.statusCode() == 400) {
            throw new IllegalArgumentException("key or value is empty, enter key or value please");
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        final String LOAD_LINK = URL + "/load/";
        URI uri = URI.create(LOAD_LINK + key + API_KEY);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = client.send(request, handler);
        if (response.statusCode() == 503) {
            throw new IllegalStateException("load failed, please try again ");
        }
        if (response.statusCode() == 403) {
            throw new IllegalStateException("Client is not registered in KVServer, please try registration again ");
        }
        if (response.statusCode() == 400) {
            throw new IllegalArgumentException("key is empty, enter key please");
        }
        if (response.statusCode() == 404) {
            throw new IllegalArgumentException("this " + key + " not found ");
        }
        return response.body();
    }

    private void registration() {
        URI uri = URI.create(URL + "/registration");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, handler);
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("registration failed, please try again ");
        }
        if (response.body() != null) {
            API_KEY += response.body();
        } else {
            throw new IllegalStateException("registration failed, please try again ");
        }
    }
}
