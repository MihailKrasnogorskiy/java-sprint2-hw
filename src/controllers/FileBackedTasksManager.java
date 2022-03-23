package controllers;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class FileBackedTasksManager extends inMemoryTaskManager {
    private File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

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
            br.write("id,type,name,status,description,epic\n");
            for (TaskBase task : list) {
                br.write(task.toString() + "\n");
            }
            br.write("\n");
            list.clear();
            list.addAll(history());
            for (TaskBase task : list) {
                br.write(task.getId() + ",");
            }
            br.flush();
        } catch (IOException e) {
            // Todo писать выброс исключения
        }
    }

    private static TaskBase fromString(String value) {
        String[] split = value.split(",");
        if (split[1].equals(TaskType.TASK.toString())) {
            return new Task(split[2], split[4], Integer.parseInt(split[0]), statusFromString(split[3]));
        } else if (split[1].equals(TaskType.EPIC.toString())) {
            return new EpicTask(split[2], split[4], Integer.parseInt(split[0]), statusFromString(split[3]));
        } else return new SubTask(split[2], split[4], Integer.parseInt(split[0]),
                statusFromString(split[3]), Integer.parseInt(split[5]));
    }

    private static Status statusFromString(String value) {
        if (value.equals(Status.NEW.toString())) {
            return Status.NEW;
        } else if (value.equals(Status.IN_PROGRESS.toString())) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try {
            Scanner scanner = new Scanner(file);
            scanner.next();
            while (scanner.hasNext()) {
                if (scanner.next() != null) {
                    manager.addTask(fromString(scanner.next()));
                    continue;
                } else {
                    scanner.next();
                    String[] splitLine = scanner.next().split(",");
                    for(String s:splitLine){
                        manager.getTaskById(Integer.parseInt(s));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return manager;
    }
}
