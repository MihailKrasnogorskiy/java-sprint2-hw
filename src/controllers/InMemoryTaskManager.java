package controllers;

import date.TaskDate;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

//реализация класса менеджер задач
public class InMemoryTaskManager implements TaskManager {
    private final TaskDate taskDate = new TaskDate();
    private final HistoryTaskManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private int id;

    public TaskDate getTaskDate() {
        return taskDate;
    }

    public HistoryTaskManager getInMemoryHistoryManager() {
        return inMemoryHistoryManager;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addTask(TaskBase task) {
        if (task == null) {
            throw new IllegalArgumentException("переданы неверные значения");
        }
        if (!canSaveTaskInSortSet(task)) {
            throw new IllegalArgumentException("задача пересекается по времени с существующей");
        }
        if (task instanceof SubTask) {
            id++;
            task.setId(id);
            taskDate.getSubTaskMap().put(id, (SubTask) task);
            taskDate.getEpicTaskMap().get(((SubTask) task).getEpic()).addSubTask((SubTask) task);
            taskDate.getSortTasks().add(task);
            return;
        }
        if (task instanceof Task) {
            id++;
            task.setId(id);
            taskDate.getTaskMap().put(id, (Task) task);
            taskDate.getSortTasks().add(task);
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
        for (Task task : taskDate.getTaskMap().values()) {
            inMemoryHistoryManager.removeTaskInHistory(task);
            taskDate.getSortTasks().remove(task);
        }
        taskDate.getTaskMap().clear();
    }

    @Override
    public void removeAllEpic() {
        removeAllSubTask();
        for (EpicTask epicTask : taskDate.getEpicTaskMap().values()) {
            inMemoryHistoryManager.removeTaskInHistory(epicTask);
        }
        taskDate.getEpicTaskMap().clear();
    }

    @Override
    public void removeAllSubTask() {  // удаление всех подзадач
        for (SubTask subTask : taskDate.getSubTaskMap().values()) {
            inMemoryHistoryManager.removeTaskInHistory(subTask);
            taskDate.getSortTasks().remove(subTask);
        }
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
    public boolean removeById(int id) {
        if (taskDate.getTaskMap().containsKey(id)) {
            inMemoryHistoryManager.removeTaskInHistory(taskDate.getTaskMap().get(id));
            taskDate.getSortTasks().remove(taskDate.getTaskMap().get(id));
            taskDate.getTaskMap().remove(id);
        } else if (taskDate.getSubTaskMap().containsKey(id)) {
            inMemoryHistoryManager.removeTaskInHistory(taskDate.getSubTaskMap().get(id));
            EpicTask epicTask = taskDate.getEpicTaskMap().get(taskDate.getSubTaskMap().get(id).getEpic());
            taskDate.getSortTasks().remove(taskDate.getSubTaskMap().get(id));
            taskDate.getSubTaskMap().remove(id);
            epicTask.getDuration();
            epicTask.getStatus();
        } else if (taskDate.getEpicTaskMap().containsKey(id)) {
            for (SubTask subTask : taskDate.getEpicTaskMap().get(id).getSubTasks()) {
                inMemoryHistoryManager.removeTaskInHistory(subTask);
                taskDate.getSortTasks().remove(subTask);
                taskDate.getSubTaskMap().remove(subTask.getId());
            }
            inMemoryHistoryManager.removeTaskInHistory(taskDate.getEpicTaskMap().get(id));
        } else {
            System.out.println("Данный id не найден");
            return false;
        }
        return true;
    }

    @Override
    public void updateTask(int id, TaskBase task) {
        if (task == null || id == 0 || task.getId() !=id) {
            throw new IllegalArgumentException("неверные входные данные");
        }
        if (task instanceof SubTask) {
            taskDate.getSubTaskMap().put(id, (SubTask) task);
            taskDate.getSortTasks().remove(taskDate.getSubTaskMap().get(id));
            taskDate.getSortTasks().add(task);
            taskDate.getEpicTaskMap().get(((SubTask) task).getEpic()).getStatus();
            taskDate.getEpicTaskMap().get(((SubTask) task).getEpic()).getDuration();
            return;
        }
        if (task instanceof EpicTask) {
            taskDate.getEpicTaskMap().put(id, (EpicTask) task);
            return;
        }
        if (task instanceof Task) {
            taskDate.getTaskMap().put(id, (Task) task);
            taskDate.getSortTasks().remove(taskDate.getTaskMap().get(id));
            taskDate.getSortTasks().add(task);
        }
    }

    @Override
    public ArrayList<SubTask> getSubTaskFromEpic(EpicTask epicTask) {
        return epicTask.getSubTasks();
    }

    public List<TaskBase> history() {
        return inMemoryHistoryManager.getHistory();
    }

    @Override

    public TreeSet<TaskBase> getSortTask() {
        return taskDate.getSortTasks();
    }

    @Override
    public boolean canSaveTaskInSortSet(TaskBase task) {
        if (getSortTask().isEmpty()) {
            return true;
        }
        if (task.getStartTime() == null) {
            return true;
        } else {
            for (TaskBase task1 : getSortTask()) {
                if (task1.getStartTime() == null || task1.getEndTime() == null) {
                    continue;
                }
                if (task.getStartTime().equals(task1.getStartTime()) || task.getEndTime().equals(task1.getEndTime())) {
                    return false;
                }
                if (task1.getStartTime().isAfter(task.getStartTime())
                        && task1.getEndTime().isBefore(task.getEndTime())
                        || task.getEndTime().isAfter(task1.getStartTime())
                        && task.getEndTime().isBefore(task1.getEndTime())
                        || task.getStartTime().isAfter(task1.getStartTime())
                        && task.getStartTime().isBefore(task1.getEndTime())) {
                    return false;
                }
            }
        }
        return true;
    }
}