package controllers;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HTTPTaskManagerTest extends TaskManagerTest {
    public HTTPTaskManagerTest() throws IOException, InterruptedException {
        super(Managers.getHTTPTaskManager());
    }

    @Test
    void test22_saveAndLoad() throws IOException, InterruptedException {
        LocalDateTime startTime1 = LocalDateTime.parse("2022-04-10T17:50");
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime startTime2 = startTime1.plusHours(1);
        LocalDateTime startTime3 = startTime2.plusHours(1);
        LocalDateTime startTime4 = startTime3.plusHours(1);
        // создание объектов задач, подзадач, эпиков и менеджера
        Task task1 = new Task("Задача 1", "тестирование кода 1",duration,startTime1);
        Task task2 = new Task("Задача 2", "тестирование кода 2");
        EpicTask epic1 = new EpicTask("эпик 1", "что-то большое сложное 1");
        EpicTask epic2 = new EpicTask("эпик 2", "что-то большое сложное 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(epic1);
        taskManager.addTask(epic2);
        SubTask subTask11 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1", epic1.getId());
        SubTask subTask12 = new SubTask("подзадача 1.2", "что-то маленькое и лёгкое 1.2", epic1.getId());
        taskManager.addTask(subTask11);
        taskManager.addTask(subTask12);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(4);
        final List<TaskBase> list = taskManager.getAllTasks();
        final List<TaskBase> history = taskManager.history();
        HTTPTaskManager taskManager1 = HTTPTaskManager.loadFromServer("http://localhost:8078");
        assertEquals(list.get(0).toString(),taskManager1.getAllTasks().get(0).toString());
        assertEquals(history.get(0).toString(),taskManager1.history().get(0).toString());
    }
}