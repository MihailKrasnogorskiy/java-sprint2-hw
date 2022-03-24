package controllers;

// утилитарный класс менеджер
public class Managers {
    // возвращение менеджера задач по умолчанию
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    // возвращение менеджера истории по умолчанию
    public static HistoryTaskManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
