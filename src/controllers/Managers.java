package controllers;

// утилитарный класс менеджер
public class Managers {
    // возвращение менеджера задач по имолчанию
    public static TaskManager getDefaultTaskManager() {
        return new inMemoryTaskManager();
    }

    // возвращение менеджера истории по имолчанию
    public static HistoryTaskManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
