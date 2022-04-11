package controllers;

import Exceptions.ManagerSaveException;
import model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//класс сохраняемого менеджера
public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    //метод создания задачи из строки
    private static TaskBase fromString(String value) {
        String[] split = value.split(",");
        if (split[1].equals(TaskType.TASK.toString())) {
            Task task = new Task(split[2], split[4], Integer.parseInt(split[0]), statusFromString(split[3]));
            if (split[6].equals("null")) {
                task.setDuration(null);
            } else task.setDuration(Duration.parse(split[6]));
            if (split[5].equals("null")) {
                task.setStartTime(null);
            } else task.setStartTime(ZonedDateTime.parse(split[5]));
            return task;
        } else if (split[1].equals(TaskType.EPIC.toString())) {
            return new EpicTask(split[2], split[4], Integer.parseInt(split[0]), statusFromString(split[3]));
        } else {
            SubTask subTask = new SubTask(split[2], split[4], Integer.parseInt(split[0]),
                    statusFromString(split[3]), Integer.parseInt(split[5]));
            if (split[7].equals("null")) {
                subTask.setDuration(null);
            } else subTask.setDuration(Duration.parse(split[7]));
            if (split[6].equals("null")) {
                subTask.setStartTime(null);
            } else subTask.setStartTime(ZonedDateTime.parse(split[6]));
            return subTask;
        }
    }

    //метод возвращения элемента перечисления из строки
    private static Status statusFromString(String value) {
        if (value.equals(Status.NEW.toString())) {
            return Status.NEW;
        } else if (value.equals(Status.IN_PROGRESS.toString())) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }

    //метод восстановления менеджера из файла
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try {
            Scanner scanner = new Scanner(file);
            scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (!line.equals("")) {
                    manager.restoreTask(fromString(line));
                } else {
                    String[] splitLine = scanner.nextLine().split(",");
                    for (String s : splitLine) {
                        manager.restoreTasksInHistoryById(Integer.parseInt(s));
                    }
                }
            }
        } catch (IOException e) {
            for (StackTraceElement stack : e.getStackTrace()) {
                System.out.println("Класс: " + stack.getClassName() + ", " + "метод: " + stack.getMethodName() + ", " +
                        "строка кода: " + stack.getLineNumber());
            }

            throw new ManagerSaveException("ошибка чтения, либо файл не обнаружен");
        }
        return manager;
    }

    public File getFile() {
        return file;
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
    public boolean removeById(int id) {
        boolean isRemove;

        isRemove = super.removeById(id);
        save();
        return isRemove;
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

    //метод для восстановления истории просмотров
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

    // метод для восстановления задач
    public void restoreTask(TaskBase task) {
        if (task == null) return;
        if (task instanceof SubTask) {
            getTaskDate().getSubTaskMap().put(task.getId(), (SubTask) task);
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

    //метод сохранения состояния менеджера
    public void save() {
        List<TaskBase> list = new ArrayList<>(getAllEpicTask());
        list.addAll(getAllTask());
        list.addAll(getAllSubTask());
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            br.write("id,type,name,status,description,epic\n");
            for (TaskBase task : list) {
                br.write(task.toString() + "\n");
            }
            if (!history().isEmpty()) {
                br.write("\n");
                for (TaskBase task : history()) {
                    br.write(task.getId() + ",");
                }
                br.flush();
            }
        } catch (IOException e) {
            for (StackTraceElement stack : e.getStackTrace()) {
                System.out.println("Класс: " + stack.getClassName() + ", " + "метод: " + stack.getMethodName() + ", " +
                        "строка кода: " + stack.getLineNumber());
            }
            throw new ManagerSaveException("ошибка записи");
        }
    }
}
