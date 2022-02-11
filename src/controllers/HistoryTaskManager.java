package controllers;

import model.TaskBase;

import java.util.List;

// интерфейс менеджер истории
public interface HistoryTaskManager {
    // добавление задачи в историю просмотра
    void addTask(TaskBase Task);

    //возвращение истории просмотра
    List<TaskBase> getHistory();

    //удаление задачи из истории просмотра
    void removeTaskInHistory(TaskBase task);
}
