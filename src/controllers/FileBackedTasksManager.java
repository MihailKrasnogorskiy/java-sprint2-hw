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
    private File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("src/date/condition.csv");
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        ZonedDateTime startTime1 = ZonedDateTime.parse("2022-04-10T17:50+03:00[Europe/Moscow]");
        Duration duration = Duration.ofMinutes(30);
        ZonedDateTime startTime2 = startTime1.plusHours(1);
        // создание объектов задач, подзадач, эпиков и менеджера
        Task task1 = new Task("Задача 1", "тестирование кода 1", duration,startTime1);
        Task task2 = new Task("Задача 2", "тестирование кода 2");
        EpicTask epic1 = new EpicTask("эпик 1", "что-то большое сложное 1");
        EpicTask epic2 = new EpicTask("эпик 2", "что-то большое сложное 2");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(epic1);
        manager.addTask(epic2);
        SubTask subTask11 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1", epic1.getId());
        SubTask subTask12 = new SubTask("подзадача 1.2", "что-то маленькое и лёгкое 1.2", epic1.getId());
        SubTask subTask13 = new SubTask("подзадача 1.3", "что-то маленькое и лёгкое 1.2", epic1.getId());

        // сохранение задач, подзадач и эпиков
        manager.addTask(subTask11);
        manager.addTask(subTask12);
        manager.addTask(subTask13);
        //получаем задачи для записи в историю и выводим историю
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicTaskById(3);
        manager.getEpicTaskById(4);
        manager.getSubTaskById(5);
        manager.getSubTaskById(7);
        System.out.println("История " + manager.history());
        for (int i = 0; i <= 6; i++) {
            manager.getSubTaskById(6);
        }
        System.out.println("История " + manager.history());
        manager.getTaskById(2);
        manager.getSubTaskById(5);
        System.out.println("История " + manager.history());
        manager.removeById(1);
        System.out.println("История " + manager.history());
        manager.removeById(3);
        System.out.println("История " + manager.history());
        manager.removeAllTask();
        System.out.println("История " + manager.history());
        EpicTask epic3 = new EpicTask("эпик 1", "что-то большое сложное 1");
        manager.addTask(epic3);
        SubTask subTask31 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1", epic3.getId(), duration, startTime1);
        SubTask subTask32 = new SubTask("подзадача 1.2", "что-то маленькое и лёгкое 1.2", epic3.getId());
        SubTask subTask33 = new SubTask("подзадача 1.3", "что-то маленькое и лёгкое 1.2", epic3.getId());
        Task task11 = new Task("Задача 1", "тестирование кода 1", duration,startTime2);
        manager.addTask(subTask31);
        manager.addTask(subTask32);
        manager.addTask(subTask33);
        manager.addTask(task11);
        manager.getEpicTaskById(8);
        manager.getSubTaskById(9);
        manager.getSubTaskById(10);
        manager.getSubTaskById(11);

      FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(file);
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
    private void save() {
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

    //метод создания задачи из строки
    private static TaskBase fromString(String value) {
        String[] split = value.split(",");
        if (split[1].equals(TaskType.TASK.toString())) {
            Task task =  new Task(split[2], split[4], Integer.parseInt(split[0]), statusFromString(split[3]));
            if(split[6].equals("null")){
                task.setDuration(null);
            } else task.setDuration(Duration.parse(split[6]));
            if(split[5].equals("null")){
                task.setStartTime(null);
            } else task.setStartTime(ZonedDateTime.parse(split[5]));
            return task;
        } else if (split[1].equals(TaskType.EPIC.toString())) {
            return new EpicTask(split[2], split[4], Integer.parseInt(split[0]), statusFromString(split[3]));
        } else{ SubTask subTask = new SubTask(split[2], split[4], Integer.parseInt(split[0]),
                statusFromString(split[3]), Integer.parseInt(split[5]));
            if(split[7].equals("null")){
                subTask.setDuration(null);
            } else subTask.setDuration(Duration.parse(split[7]));
            if(split[6].equals("null")){
                subTask.setStartTime(null);
            } else subTask.setStartTime(ZonedDateTime.parse(split[6]));
            return subTask;}
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
}
