import com.google.gson.Gson;
import controllers.Managers;
import controllers.TaskManager;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.ZonedDateTime;

public class Main {
   static Gson gson = new Gson();
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefaultTaskManager();
        ZonedDateTime startTime1 = ZonedDateTime.now();
        Duration duration = Duration.ofMinutes(30);
        ZonedDateTime startTime2 = startTime1.plusHours(1);
        ZonedDateTime startTime3 = startTime2.plusHours(1);
        ZonedDateTime startTime4 = startTime3.plusHours(1);
        // создание объектов задач, подзадач, эпиков и менеджера
        Task task1 = new Task("Задача 1", "тестирование кода 1", duration, startTime2);
        Task task2 = new Task("Задача 2", "тестирование кода 2", duration, startTime1);
        Task task3 = new Task("Задача 2", "тестирование кода 2");
        Task task4 = new Task("Задача 2", "тестирование кода 2");
//
//        System.out.println(epic1.getStatus());
//        EpicTask epic2 = new EpicTask("эпик 2", "что-то большое сложное 2");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        System.out.println(manager.getSortTask());
        manager.removeById(2);

        System.out.println(manager.getSortTask());
        task1.setStatus(Status.DONE);
        System.out.println(manager.getSortTask());
        manager.removeAllTask();
        System.out.println(manager.getSortTask());
        EpicTask epic1 = new EpicTask("эпик 1", "что-то большое сложное 1");
        manager.addTask(epic1);
        System.out.println(manager.getSortTask());

//        manager.addTask(epic1);
//        manager.addTask(epic2);
        SubTask subTask11 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1",
                epic1.getId(), duration, startTime3);
        SubTask subTask12 = new SubTask("подзадача 1.2", "что-то маленькое и лёгкое 1.2",
                epic1.getId(), duration, startTime4);

        manager.addTask(subTask11);
        manager.addTask(subTask12);
        System.out.println(epic1);
        System.out.println(manager.getSortTask());
//        SubTask subTask13 = new SubTask("подзадача 1.3", "что-то маленькое и лёгкое 1.2", epic1.getId());
        System.out.println(gson.toJson(manager.getAllSubTask()));
    }
}