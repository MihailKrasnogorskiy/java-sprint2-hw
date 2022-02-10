package controllers;

public class Managers {
    public static TaskManager getDefaultTaskManager() {
        return new inMemoryTaskManager();
    }

    public static HistoryTaskManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
