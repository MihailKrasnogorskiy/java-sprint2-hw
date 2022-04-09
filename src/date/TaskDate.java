package date;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;

import java.util.HashMap;
import java.util.TreeSet;

// класс для хранения задач
public class TaskDate {

    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private HashMap<Integer, EpicTask> epicTaskMap = new HashMap<>();
    private TreeSet<TaskBase> sortTasks = new TreeSet<>((task1, task2) -> {
        if (((TaskBase) task2).getStartTime() == null && ((TaskBase) task1).getStartTime() != null) {
            return -1;
        } else if (((TaskBase) task2).getStartTime() == null) {
            return ((TaskBase) task1).getId() - ((TaskBase) task2).getId();
        } else if (((TaskBase) task1).getStartTime() == null && ((TaskBase) task2).getStartTime() != null) {
            return 1;
        } else if (((TaskBase) task1).getStartTime().isAfter(((TaskBase) task2).getStartTime())) {
            return 1;
        } else if (((TaskBase) task1).getStartTime().equals(((TaskBase) task2).getStartTime())) {
            return ((TaskBase) task1).getId() - ((TaskBase) task2).getId();
        }
        return -1;
    });

    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public HashMap<Integer, EpicTask> getEpicTaskMap() {
        return epicTaskMap;
    }

    public TreeSet<TaskBase> getSortTasks() {
        return sortTasks;
    }

}
