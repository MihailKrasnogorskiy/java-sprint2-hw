package controllers;

import model.TaskBase;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryTaskManager {
    private final List<TaskBase> historyList = new ArrayList<>();
    public static final int MAX_HISTORY_SIZE = 10;
    public static final int OLDEST_HISTORY_INDEX = 0;

    @Override
    public void addTask(TaskBase task) {
        if (historyList.size() == MAX_HISTORY_SIZE) {
            historyList.remove(OLDEST_HISTORY_INDEX);
        }
        historyList.add(task);
    }

    @Override
    public List<TaskBase> getHistory() {
        return historyList;
    }

    public void removeTaskInHistory(TaskBase task) {
        List<TaskBase> copyHistoryList = new ArrayList<>(historyList);
        for (int i = copyHistoryList.size() - 1; i >= 0; i--) {
            if (copyHistoryList.get(i).equals(task)) {
                historyList.remove(i);
            }
        }
    }
}
