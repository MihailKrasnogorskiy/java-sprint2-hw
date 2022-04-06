package controllers;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;

import java.util.ArrayList;
import java.util.List;

// интерфейс менеджер задач
public interface TaskManager {
    // добавление(сохранение) задачи
    void addTask(TaskBase task);

    //возвращение текущего id

    int getId();

    // возвращение задачи по идентификатору
    Task getTaskById(int id);

    // возвращение подзадачи по идентификатору
    SubTask getSubTaskById(int id);

    // возвращение эпика по идентификатору
    EpicTask getEpicTaskById(int id);

    // удаление всех задач
    void removeAllTask();

    // удаление всех эпиков
    void removeAllEpic();

    // удаление всех подзадач
    void removeAllSubTask();

    // возвращение списка всех подзадач
    ArrayList<SubTask> getAllSubTask();

    // возвращение списка всех задач
    ArrayList<Task> getAllTask();

    // возвращение списка всех эпиков
    ArrayList<EpicTask> getAllEpicTask();

    // удаление задачи по идентификатору
    boolean removeById(int id);

    // обновление задачи
    void updateTask(int id, TaskBase Task);

    // возвращение списка всех подзадач конкретного эпика
    ArrayList<SubTask> getSubTaskFromEpic(EpicTask epicTask);

    // возвращение истории просмотра
    List<TaskBase> history();

}
