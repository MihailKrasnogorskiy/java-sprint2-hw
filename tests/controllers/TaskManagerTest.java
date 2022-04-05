package controllers;

import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;
    private Task task;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    private void createTask() {
        task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
    }

    @Test
    public void test7_addNewTask() {

        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void test8_updateTask() {
        task.setName("Name1");
        task.setDescription("Description1");
        task.setStatus(Status.DONE);
        final int taskId = task.getId();
        taskManager.updateTask(taskId, task);

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void test9_shouldGetTrueAfterRemoveById() {
        EpicTask epicTask = new EpicTask("Epic1","EpicDescription");
        taskManager.addTask(epicTask);
        SubTask subTask = new SubTask("SubTask","SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);
        assertTrue(taskManager.removeById(1));
        assertTrue(taskManager.removeById(3));
        assertTrue(taskManager.removeById(2));

    }

    @Test
    public void test10_shouldGetFalseAfterRemoveById() {

        assertFalse(taskManager.removeById(0));
    }
    @Test
    public void test11_shouldGet1GetAllTask() {
        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void test12_shouldGet1GetAllSubTask() {
        EpicTask epicTask = new EpicTask("Epic1","EpicDescription");
        taskManager.addTask(epicTask);
        SubTask subTask = new SubTask("SubTask","SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);
        final List<SubTask> tasks = taskManager.getAllSubTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subTask, tasks.get(0), "Задачи не совпадают.");
    }
    @Test
    public void test13_shouldGet1GetAllEpicTask() {
        EpicTask epicTask = new EpicTask("Epic1","EpicDescription");
        taskManager.addTask(epicTask);
        SubTask subTask = new SubTask("SubTask","SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);
        final List<EpicTask> tasks = taskManager.getAllEpicTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epicTask, tasks.get(0), "Задачи не совпадают.");
    }
}
