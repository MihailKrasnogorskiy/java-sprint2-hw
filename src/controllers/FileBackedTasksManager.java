package controllers;

import model.SubTask;
import model.TaskBase;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileBackedTasksManager extends inMemoryTaskManager {
    private File file;

    @Override
    public void addTask(TaskBase task) {
        super.addTask(task);
        save();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public void removeById(int id) {
        super.removeById(id);
        save();
    }

    @Override
    public void updateTask(int id, TaskBase task) {
        super.updateTask(id, task);
        save();
    }

    private void save() {
        List<TaskBase> list = new ArrayList<>(getAllSubTask());
        list.addAll(getAllTask());
        list.addAll(getAllEpicTask());
        list.sort(new Comparator<TaskBase>() {
            @Override
            public int compare(TaskBase o1, TaskBase o2) {
                return o1.getId() - o2.getId();
            }
        });
        try {
            FileWriter fr = new FileWriter(file);
            BufferedWriter br = new BufferedWriter(fr);
            for (TaskBase task : list) {
                if (task instanceof SubTask) {
                    br.write(((SubTask) task).toString() + "\n");
                } else {
                    br.write(task.toString() + "\n");
                }
            }
            br.write("\n");
            list.clear();
            list.addAll(history());
            for (TaskBase task : list) {
                br.write(task.getId() + ",");
            }
        } catch (IOException e) {
            // Todo писать выброс исключения
        }
    }
}
