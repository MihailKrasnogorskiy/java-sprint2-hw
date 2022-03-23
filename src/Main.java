import controllers.FileBackedTasksManager;
import controllers.Managers;
import controllers.TaskManager;

import model.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println(  "TASK".equals(TaskType.TASK.toString()));
        // создание объектов задач, подзадач, эпиков и менеджера
//        Task task1 = new Task("Задача 1", "тестирование кода 1");
//        Task task2 = new Task("Задача 2", "тестирование кода 2");
//        EpicTask epic1 = new EpicTask("эпик 1", "что-то большое сложное 1");
//        EpicTask epic2 = new EpicTask("эпик 2", "что-то большое сложное 2");
//        SubTask subTask11 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1", epic1);
//        SubTask subTask12 = new SubTask("подзадача 1.2", "что-то маленькое и лёгкое 1.2", epic1);
//        SubTask subTask13 = new SubTask("подзадача 1.3", "что-то маленькое и лёгкое 1.2", epic1);
//        File file = new File("src/date/condition.csv");
//        TaskManager manager = new FileBackedTasksManager(file);
//        // сохранение задач, подзадач и эпиков
//        manager.addTask(task1);
//        manager.addTask(task2);
//        manager.addTask(epic1);
//        manager.addTask(epic2);
//        manager.addTask(subTask11);
//        manager.addTask(subTask12);
//        manager.addTask(subTask13);
//        //получаем задачи для записи в историю и выводим историю
//        manager.getTaskById(1);
//        manager.getTaskById(2);
//        manager.getEpicTaskById(3);
//        manager.getEpicTaskById(4);
//        manager.getSubTaskById(5);
//        manager.getSubTaskById(7);
//        System.out.println("История " + manager.history());
//        for (int i = 0; i <= 6; i++) {
//            manager.getSubTaskById(6);
//        }
//        System.out.println("История " + manager.history());
//        manager.getTaskById(2);
//        manager.getSubTaskById(5);
//        System.out.println("История " + manager.history());
//        manager.removeById(1);
//        System.out.println("История " + manager.history());
//        manager.removeById(3);
//        System.out.println("История " + manager.history());
//        manager.removeAllTask();
//        System.out.println("История " + manager.history());
//        manager.addTask(epic1);
//        manager.addTask(subTask11);
//        manager.addTask(subTask12);
//        manager.addTask(subTask13);
//        manager.getEpicTaskById(8);
//        manager.getSubTaskById(9);
//        manager.getSubTaskById(10);
//        manager.getSubTaskById(11);
//        System.out.println("История " + manager.history());
//      //  manager.removeAllEpic();
//        System.out.println("История " + manager.history());
    }
}