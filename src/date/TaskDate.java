package date;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.HashMap;

// класс для хранения задач
public class TaskDate {

    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskMap = new HashMap<>();
    private HashMap<Integer, EpicTask> epicTaskMap = new HashMap<>();

    public HashMap<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public HashMap<Integer, EpicTask> getEpicTaskMap() {
        return epicTaskMap;
    }
}
