package controllers;

import servers.KVServer;

import java.io.File;
import java.io.IOException;

// утилитарный класс менеджер
public class Managers {
    public static File fileTest = new File("src/resources/fileTest.csv");
    public static KVServer KVserver;

    static {
        try {
            KVserver = new KVServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // возвращение менеджера задач по умолчанию
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    // возвращение менеджера истории по умолчанию
    public static HistoryTaskManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    //возвращение менеджера способного восстановиться
    public static TaskManager getRestorableManagerForTests() {
        return new FileBackedTasksManager(fileTest);
    }

    //возвращение KV сервера

    public static HTTPTaskManager getHTTPTaskManager() throws IOException, InterruptedException {
        try {
            KVserver.start();
        } catch (IllegalStateException e) {
            System.out.println("Server is run");
        }
        return new HTTPTaskManager("http://localhost:8078");
    }


}
