package controllers;

import java.io.File;

// утилитарный класс менеджер
public class Managers {
    public static File fileTest = new File("tests/fileTest.csv");
    // возвращение менеджера задач по умолчанию
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    // возвращение менеджера истории по умолчанию
    public static HistoryTaskManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getRestorableMeneger(){
        return new FileBackedTasksManager(fileTest);
    }
}
