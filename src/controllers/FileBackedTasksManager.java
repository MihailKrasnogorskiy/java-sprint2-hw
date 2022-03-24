package controllers;

import model.*;

import java.io.*;
import java.util.ArrayList;
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

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask epicTask = super.getEpicTaskById(id);
        save();
        return epicTask;
    }

    public TaskBase restoreTasksInHistoryById(int id) {
        if (getTaskDate().getTaskMap().containsKey(id)) {
            getInMemoryHistoryManager().addTask(getTaskDate().getTaskMap().get(id));
            return getTaskDate().getTaskMap().get(id);
        } else if (getTaskDate().getSubTaskMap().containsKey(id)) {
            getInMemoryHistoryManager().addTask(getTaskDate().getSubTaskMap().get(id));
            return getTaskDate().getSubTaskMap().get(id);
        } else if (getTaskDate().getEpicTaskMap().containsKey(id)) {
            getInMemoryHistoryManager().addTask(getTaskDate().getEpicTaskMap().get(id));
            return getTaskDate().getEpicTaskMap().get(id);
        }
        return null;
    }

    public void restoreTask(TaskBase task) {
        if (task == null) return;
        if (task instanceof SubTask) {
            getTaskDate().getSubTaskMap().put(task.getId(), (SubTask) task);
            System.out.println(getTaskDate().getEpicTaskMap().get(((SubTask) task).getEpic()).getSubTasks());
            getTaskDate().getEpicTaskMap().get(((SubTask) task).getEpic()).addSubTask((SubTask) task);
            return;
        }
        if (task instanceof Task) {
            getTaskDate().getTaskMap().put(task.getId(), (Task) task);
            return;
        }
        if (task instanceof EpicTask) {
            getTaskDate().getEpicTaskMap().put(task.getId(), (EpicTask) task);
        }
    }

    private void save() {
        List<TaskBase> list = new ArrayList<>(getAllEpicTask());
        list.addAll(getAllTask());
        list.addAll(getAllSubTask());
        try {
            FileWriter fr = new FileWriter(file);
            BufferedWriter br = new BufferedWriter(fr);
            br.write("id,type,name,status,description,epic\n");
            for (TaskBase task : list) {
                br.write(task.toString() + "\n");
            }
            br.write("\n");
            System.out.println("Лист " + history());
            for (TaskBase task : history()) {
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
            scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (!line.equals("")) {
                    manager.restoreTask(fromString(line));
                    continue;
                } else {
                    String[] splitLine = scanner.nextLine().split(",");
                    for (String s : splitLine) {
                        manager.restoreTasksInHistoryById(Integer.parseInt(s));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        }
        return manager;
    }
}
