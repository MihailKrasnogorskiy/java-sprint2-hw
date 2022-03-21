package controllers;

import model.TaskBase;

import java.io.*;
import java.util.ArrayList;
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
    public void updateTask(int id, TaskBase Task) {
        super.updateTask(id, Task);
        save();
    }

    private void save() {
        List<TaskBase> list = new ArrayList<>(getAllSubTask());
        list.addAll(getAllTask());
        list.addAll(getAllEpicTask());
        try {
            FileWriter fr = new FileWriter(file);
            BufferedWriter br = new BufferedWriter(fr);
            for (TaskBase task : list) {
                br.write(task.toString() + "\n");
            }
            br.write("\n");
            list.clear();
            list.addAll(history());
            for(TaskBase task : list){
                br.write(task.getId() + ",");
            }
        } catch (IOException e) {
            // Todo писать выброс исключения
        }
    }
}
