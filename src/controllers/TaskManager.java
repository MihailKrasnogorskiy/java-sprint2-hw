package controllers;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void addTask(TaskBase task);

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    EpicTask getEpicTaskById(int id);

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubTask();

    ArrayList<SubTask> getAllSubTask();

    ArrayList<Task> getAllTask();

    ArrayList<EpicTask> getAllEpicTask();

    void removeById(int id);

    void updateTask(int id, TaskBase Task);

    ArrayList<SubTask> getSubTaskFromEpic(EpicTask epicTask);

    List<TaskBase> history();

}
