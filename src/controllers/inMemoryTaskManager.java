package controllers;

import model.*;
import date.TaskDate;

import java.util.ArrayList;
import java.util.List;

//реализация класса менеджер задач
public class inMemoryTaskManager implements TaskManager {
    private final TaskDate taskDate = new TaskDate();
    private final HistoryTaskManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private int id;

    @Override
    public void addTask(TaskBase task) {
        if (task == null) return;
        if (task instanceof SubTask) {
            id++;
            task.setId(id);
            taskDate.getSubTaskMap().put(id, (SubTask) task);
            return;
        }
        if (task instanceof Task) {
            id++;
            task.setId(id);
            taskDate.getTaskMap().put(id, (Task) task);
            return;
        }
        if (task instanceof EpicTask) {
            id++;
            task.setId(id);
            taskDate.getEpicTaskMap().put(id, (EpicTask) task);
        }
    }

    @Override
    public Task getTaskById(int id) {
        if (taskDate.getTaskMap().containsKey(id)) {
            inMemoryHistoryManager.addTask(taskDate.getTaskMap().get(id));
            return taskDate.getTaskMap().get(id);
        }
        return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (taskDate.getSubTaskMap().containsKey(id)) {
            inMemoryHistoryManager.addTask(taskDate.getSubTaskMap().get(id));
            return taskDate.getSubTaskMap().get(id);
        }
        return null;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        if (taskDate.getEpicTaskMap().containsKey(id)) {
            inMemoryHistoryManager.addTask(taskDate.getEpicTaskMap().get(id));
            return taskDate.getEpicTaskMap().get(id);
        }
        return null;
    }

    @Override
    public void removeAllTask() {      // удаление всех задач
        taskDate.getTaskMap().clear();
    }

    @Override
    public void removeAllEpic() {
        taskDate.getEpicTaskMap().clear();
        taskDate.getSubTaskMap().clear();
    }

    @Override
    public void removeAllSubTask() {    // удаление всех подзадач
        taskDate.getSubTaskMap().clear();
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {   // возвращение списка всех подзадач
        return new ArrayList<>(taskDate.getSubTaskMap().values());
    }

    @Override
    public ArrayList<Task> getAllTask() {    // возвращение списка всех задач
        return new ArrayList<>(taskDate.getTaskMap().values());
    }

    @Override
    public ArrayList<EpicTask> getAllEpicTask() {    // возвращение списка всех эпиков
        return new ArrayList<>(taskDate.getEpicTaskMap().values());
    }

    @Override
    public void removeById(int id) {
        if (taskDate.getTaskMap().containsKey(id)) {
            inMemoryHistoryManager.removeTaskInHistory(taskDate.getTaskMap().get(id));
            taskDate.getTaskMap().remove(id);
        } else if (taskDate.getSubTaskMap().containsKey(id)) {
            inMemoryHistoryManager.removeTaskInHistory(taskDate.getSubTaskMap().get(id));
            taskDate.getSubTaskMap().remove(id);
        } else if (taskDate.getEpicTaskMap().containsKey(id)) {
            for (SubTask subTask : taskDate.getEpicTaskMap().get(id).getSubTasks()) {
                inMemoryHistoryManager.removeTaskInHistory(subTask);
                taskDate.getSubTaskMap().remove(subTask.getId());
            }
            inMemoryHistoryManager.removeTaskInHistory(taskDate.getEpicTaskMap().get(id));
            taskDate.getEpicTaskMap().remove(id);
        } else System.out.println("Данный id не найден");
    }

    @Override
    public void updateTask(int id, TaskBase Task) {
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

    @Override
    public ArrayList<SubTask> getSubTaskFromEpic(EpicTask epicTask) {
        return epicTask.getSubTasks();
    }

    public List<TaskBase> history() {
        return inMemoryHistoryManager.getHistory();
    }
}