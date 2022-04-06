package controllers;

import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {

    public T taskManager;
    private Task task = null;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    private void createTask() {
        task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
    }

    @Test
    public void test7_addNewTask() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                taskManager.addTask(null);
            }
        });

        assertEquals("переданы неверные значения", exception.getMessage());
        createTask();
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask);
        assertEquals(task, savedTask);
    }

    @Test
    public void test8_updateTask() {

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                taskManager.updateTask(0, null);
            }
        });

        assertEquals("неверные входные данные", exception.getMessage());
        createTask();
        taskManager.addTask(task);
        final int taskId = task.getId();
        task.setName("Name1");
        task.setDescription("Description1");
        task.setStatus(Status.DONE);
        taskManager.updateTask(taskId, task);
        EpicTask epicTask = new EpicTask("Epic1", "EpicDescription");
        taskManager.addTask(epicTask);
        final int epicId = epicTask.getId();
        epicTask.setName("newEpicName");
        taskManager.updateTask(epicId, epicTask);
        SubTask subTask = new SubTask("SubTask", "SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);
        final int subTaskId = subTask.getId();
        subTask.setDescription("new description");
        taskManager.updateTask(subTaskId, subTask);
        final Task savedTask = taskManager.getTaskById(taskId);
        final EpicTask savedEpic = taskManager.getEpicTaskById(epicId);
        final SubTask savedSubTask = taskManager.getSubTaskById(subTaskId);
        assertNotNull(savedTask);
        assertEquals(task, savedTask);
        assertNotNull(savedTask);
        assertEquals(epicTask, savedEpic);
        assertNotNull(savedTask);
        assertEquals(subTask, savedSubTask);
    }

    @Test
    public void test9_shouldGetTrueAfterRemoveById() {
        createTask();
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
        createTask();
        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void test12_shouldGet1GetAllSubTask() {
        createTask();
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
        createTask();
        EpicTask epicTask = new EpicTask("Epic1", "EpicDescription");
        taskManager.addTask(epicTask);
        SubTask subTask = new SubTask("SubTask", "SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);
        final List<EpicTask> tasks = taskManager.getAllEpicTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epicTask, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void test14_shouldGet2GetSubTaskFromEpic() {
        createTask();
        EpicTask epicTask = new EpicTask("Epic1", "EpicDescription");
        taskManager.addTask(epicTask);
        SubTask subTask = new SubTask("SubTask", "SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);
        SubTask subTask1 = new SubTask("SubTask1", "SubTaskDescription1", epicTask.getId());
        taskManager.addTask(subTask);
        final List<SubTask> tasks = taskManager.getSubTaskFromEpic(epicTask);

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
    }

    @Test
    public void test15_shouldGetTrueRemoveAllDifferentTasks() {
        createTask();
        EpicTask epicTask = new EpicTask("Epic1", "EpicDescription");
        taskManager.addTask(epicTask);
        SubTask subTask = new SubTask("SubTask", "SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);
        taskManager.removeAllTask();
        final List<Task> tasks = taskManager.getAllTask();
        assertTrue(tasks.isEmpty());
        taskManager.removeAllEpic();
        final List<EpicTask> epics = taskManager.getAllEpicTask();
        final List<SubTask> subTasks = taskManager.getAllSubTask();
        assertTrue(subTasks.isEmpty());
        assertTrue(epics.isEmpty());
    }

    @Test
    public void test15_shouldGetNullGetDifferentTasksById() {
        createTask();
        assertNull(taskManager.getSubTaskById(0));
        assertNull(taskManager.getTaskById(0));
        assertNull(taskManager.getEpicTaskById(0));
    }

    @Test
    void test16_shouldTrueGetNullHistory() {
        final List<TaskBase> history = taskManager.history();
        assertTrue(history.isEmpty());
        createTask();
        EpicTask epicTask = new EpicTask("Epic1", "EpicDescription");
        taskManager.addTask(epicTask);
        SubTask subTask = new SubTask("SubTask", "SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(2);
        taskManager.getSubTaskById(3);
        final List<TaskBase> historyNew = taskManager.history();
        assertEquals(3,historyNew.size());

    }
}
