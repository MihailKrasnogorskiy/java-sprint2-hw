package controllers;

import model.*;
import date.TaskDate;

import java.util.ArrayList;

//менеджер задач
public class TaskManager {
    private TaskDate taskDate = new TaskDate();
    private int id;

    public void addTask(TaskBase task) {    // добавление(сохранение) задачи
        if (task == null) return;
        if (task instanceof SubTask) {
            id++;
            ((SubTask) task).setId(id);
            taskDate.getSubTaskMap().put(id, (SubTask) task);
            return;
        }
        if (task instanceof Task) {
            id++;
            ((Task) task).setId(id);
            taskDate.getTaskMap().put(id, (Task) task);
            return;
        }
        if (task instanceof EpicTask) {
            id++;
            ((EpicTask) task).setId(id);
            taskDate.getEpicTaskMap().put(id, (EpicTask) task);
        }
    }

    public Task getTaskById(int id) {        // возвращение задачи по идентификатору
        if (taskDate.getTaskMap().containsKey(id)) {
            return taskDate.getTaskMap().get(id);
        }
        return null;
    }

    public SubTask getSubTaskById(int id) {   // возвращение подзадачи по идентификатору
        if (taskDate.getSubTaskMap().containsKey(id)) {
            return taskDate.getSubTaskMap().get(id);
        }
        return null;
    }

    public EpicTask getEpicTaskById(int id) {    // возвращение эпика по идентификатору
        if (taskDate.getEpicTaskMap().containsKey(id)) {
            return taskDate.getEpicTaskMap().get(id);
        }
        return null;
    }

    public void removeAllTask() {      // удаление всех задач
        taskDate.getTaskMap().clear();
    }

    public void removeAllEpic() {     // удаление всех эпиков
        taskDate.getEpicTaskMap().clear();
        taskDate.getSubTaskMap().clear();
    }

    public void removeAllSubTask() {    // удаление всех подзадач
        taskDate.getSubTaskMap().clear();
    }

    public ArrayList<SubTask> getAllSubTask() {   // возвращение списка всех подзадач
        return new ArrayList<>(taskDate.getSubTaskMap().values());
    }

    public ArrayList<Task> getAllTask() {    // возвращение списка всех задач
        return new ArrayList<>(taskDate.getTaskMap().values());
    }

    public ArrayList<EpicTask> getAllEpicTask() {    // возвращение списка всех эпиков
        return new ArrayList<>(taskDate.getEpicTaskMap().values());
    }

    public void removeById(int id) {    // удаление задачи по идентификатору
        if (taskDate.getTaskMap().containsKey(id)) {
            taskDate.getTaskMap().remove(id);
        } else if (taskDate.getSubTaskMap().containsKey(id)) {
            taskDate.getSubTaskMap().remove(id);
        } else if (taskDate.getEpicTaskMap().containsKey(id)) {
            for (SubTask subTask : taskDate.getEpicTaskMap().get(id).getSubTasks()) {
                taskDate.getSubTaskMap().remove(subTask.getId());
            }
            taskDate.getEpicTaskMap().remove(id);
        } else System.out.println("Данный id не найден");
    }

    public void updateTask(int id, TaskBase Task) {    // обновление задачи
        if (Task == null || id == 0) return;
        if (Task instanceof SubTask) {
            taskDate.getSubTaskMap().put(id, (SubTask) Task);
            ((SubTask) Task).getEpic().checkStatus();
            return;
        }
        if (Task instanceof EpicTask) {
            taskDate.getEpicTaskMap().put(id, (EpicTask) Task);
            return;
        }
        if (Task instanceof Task) {
            taskDate.getTaskMap().put(id, (Task) Task);
        }
    }

    public ArrayList<SubTask> getSubTaskFromEpic(EpicTask epicTask) {  //возвращение всех подзадач эпика
        return epicTask.getSubTasks();
    }
}