import controllers.Managers;
import controllers.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefaultTaskManager();
        // создание объектов задач, подзадач, эпиков и менеджера
        Task task1 = new Task("Задача 1", "тестирование кода 1");
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
        SubTask subTask31 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1", epic3.getId());
        SubTask subTask32 = new SubTask("подзадача 1.2", "что-то маленькое и лёгкое 1.2", epic3.getId());
        SubTask subTask33 = new SubTask("подзадача 1.3", "что-то маленькое и лёгкое 1.2", epic3.getId());
        manager.addTask(subTask31);
        manager.addTask(subTask32);
        manager.addTask(subTask33);
        manager.getEpicTaskById(8);
        manager.getSubTaskById(9);
        manager.getSubTaskById(10);
        manager.getSubTaskById(11);
        System.out.println("История " + manager.history());
        manager.removeAllEpic();
        System.out.println("История " + manager.history());
    }
}