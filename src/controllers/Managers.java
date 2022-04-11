package controllers;

import java.io.File;

// утилитарный класс менеджер
public class Managers {
    public static File fileTest = new File("src/resources/fileTest.csv");

    // возвращение менеджера задач по умолчанию
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    // возвращение менеджера истории по умолчанию
    public static HistoryTaskManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    //возвращение менеджера способного восстановиться
    public static TaskManager getRestorableManager() {
        return new FileBackedTasksManager(fileTest);
    }
}
