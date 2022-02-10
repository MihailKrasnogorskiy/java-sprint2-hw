package controllers;

import model.TaskBase;

import java.util.List;

public interface HistoryTaskManager {

    void addTask(TaskBase Task);

    List<TaskBase> getHistory();

    void removeTaskInHistory(TaskBase task);
}
