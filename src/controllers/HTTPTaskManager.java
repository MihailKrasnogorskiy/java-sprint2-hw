package controllers;

public class HTTPTaskManager extends InMemoryTaskManager {
    private String url;

    public HTTPTaskManager(String url) {
        this.url = url;
    }
}
