import controllers.TaskManager;
import model.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // создание объектов задач, подзадач, эпиков и менеджера
        Task task1 = new Task("Задача 1", "тестирование кода 1");
        Task task2 = new Task("Задача 2", "тестирование кода 2");
        EpicTask epic1 = new EpicTask("эпик 1", "что-то большое сложное 1");
        EpicTask epic2 = new EpicTask("эпик 2", "что-то большое сложное 2");
        SubTask subTask11 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1", epic1);
        SubTask subTask12 = new SubTask("подзадача 1.2", "что-то маленькое и лёгкое 1.2", epic1);
        SubTask subTask21 = new SubTask("подзадача 2.1", "что-то маленькое и лёгкое 2.1", epic2);
        TaskManager manager = new TaskManager();
        // сохранение задач, подзадач и эпиков
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(epic1);
        manager.addTask(epic2);
        manager.addTask(subTask11);
        manager.addTask(subTask12);
        manager.addTask(subTask21);
        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpicTask());
        // изменяем статусы задачи и подзадач
        task1.setStatus(Status.DONE);
        subTask12.setStatus(Status.DONE);
        subTask21.setStatus(Status.IN_PROGRESS);
        // обновляем в менеджере
        manager.updateTask(task1.getId(), task1);
        manager.updateTask(subTask12.getId(), subTask12);
        manager.updateTask(subTask21.getId(), subTask21);
        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpicTask());
        // удаление задачи и эпика по id
        manager.removeById(task2.getId());
        manager.removeById(epic1.getId());
        System.out.println(manager.getAllTask());
        System.out.println(manager.getAllEpicTask());
        System.out.println(manager.getAllSubTask());
    }
}
