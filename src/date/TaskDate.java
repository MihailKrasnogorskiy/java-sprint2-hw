package date;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;

import java.util.HashMap;
import java.util.TreeSet;

// класс для хранения задач
public class TaskDate {

    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTaskMap = new HashMap<>();
    private final TreeSet<TaskBase> sortTasks = new TreeSet<>((task1, task2) -> {
        if (task2.getStartTime() == null && task1.getStartTime() != null) {
            return -1;
        } else if (task2.getStartTime() == null) {
            return task1.getId() - task2.getId();
        } else if (task1.getStartTime() == null && task2.getStartTime() != null) {
            return 1;
        } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
            return 1;
        } else if (task1.getStartTime().equals(task2.getStartTime())) {
            return task1.getId() - task2.getId();
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
