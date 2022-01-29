package controllers;

import model.*;
import date.TaskBase;

import java.util.ArrayList;

//менеджер задач
public class TaskManager {
    private TaskBase taskBase = new TaskBase();
    private int id;

    public void addTask(Object obj) {    // добавление(сохранение) задачи
        if (obj == null) return;
        if (obj instanceof SubTask) {
            id++;
            ((SubTask) obj).setId(id);
            taskBase.getSubTaskMap().put(id, (SubTask) obj);
            return;
        }
        if (obj instanceof Task) {
            id++;
            ((Task) obj).setId(id);
            taskBase.getTaskMap().put(id, (Task) obj);
            return;
        }
        if (obj instanceof EpicTask) {
            id++;
            ((EpicTask) obj).setId(id);
            taskBase.getEpicTaskMap().put(id, (EpicTask) obj);
        }
    }

    public Task getTaskById(int id) {        // возвращение задачи по идентификатору
        if (taskBase.getTaskMap().containsKey(id)) {
            return taskBase.getTaskMap().get(id);
        }
        return null;
    }

    public SubTask getSubTaskById(int id) {   // возвращение подзадачи по идентификатору
        if (taskBase.getSubTaskMap().containsKey(id)) {
            return taskBase.getSubTaskMap().get(id);
        }
        return null;
    }

    public EpicTask getEpicTaskById(int id) {    // возвращение эпика по идентификатору
        if (taskBase.getEpicTaskMap().containsKey(id)) {
            return taskBase.getEpicTaskMap().get(id);
        }
        return null;
    }

    public void removeAllTask() {      // удаление всех задач
        taskBase.getTaskMap().clear();
    }

    public void removeAllEpic() {     // удаление всех эпиков
        taskBase.getEpicTaskMap().clear();
        taskBase.getSubTaskMap().clear();
    }

    public void removeAllSubTask() {    // удаление всех подзадач
        taskBase.getSubTaskMap().clear();
    }

    public ArrayList<SubTask> getAllSubTask() {   // возвращение списка всех подзадач
        return new ArrayList<>(taskBase.getSubTaskMap().values());
    }

    public ArrayList<Task> getAllTask() {    // возвращение списка всех задач
        return new ArrayList<>(taskBase.getTaskMap().values());
    }

    public ArrayList<EpicTask> getAllEpicTask() {    // возвращение списка всех эпиков
        return new ArrayList<>(taskBase.getEpicTaskMap().values());
    }

    public void removeById(int id) {    // удаление задачи по идентификатору
        if (taskBase.getTaskMap().containsKey(id)) {
            taskBase.getTaskMap().remove(id);
        } else if (taskBase.getSubTaskMap().containsKey(id)) {
            taskBase.getSubTaskMap().remove(id);
        } else if (taskBase.getEpicTaskMap().containsKey(id)) {
            for (SubTask subTask : taskBase.getEpicTaskMap().get(id).getSubTasks()) {
                taskBase.getSubTaskMap().remove(subTask.getId());
            }
            taskBase.getEpicTaskMap().remove(id);
        } else System.out.println("Данный id не найден");
    }

    public void updateTask(int id, Object obj) {    // обновление задачи
        if (obj == null || id == 0) return;
        if (obj instanceof SubTask) {
            taskBase.getSubTaskMap().put(id, (SubTask) obj);
            ((SubTask) obj).getEpic().checkStatus();
            return;
        }
        if (obj instanceof EpicTask) {
            taskBase.getEpicTaskMap().put(id, (EpicTask) obj);
            return;
        }
        if (obj instanceof Task) {
            taskBase.getTaskMap().put(id, (Task) obj);
        }
    }

    public ArrayList<SubTask> getSubTaskFromEpic(EpicTask epicTask) {  //возвращение всех подзадач эпика
        return epicTask.getSubTasks();
    }
}
