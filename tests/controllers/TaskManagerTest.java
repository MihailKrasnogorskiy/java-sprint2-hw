package controllers;

import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import servers.KVServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TaskManagerTest<T extends TaskManager> {

    public T taskManager;
    private Task task = null;
    private  EpicTask epicTask;
    private SubTask subTask;
    private KVServer server;

    public TaskManagerTest(T taskManager) throws IOException {
        this.taskManager = taskManager;
    }

    private void createTask() {
        LocalDateTime startTime1 = LocalDateTime.parse("2022-04-10T17:50");
        Duration duration = Duration.ofMinutes(30);
        task = new Task("Задача 1", "тестирование кода 1", duration, startTime1);
        taskManager.addTask(task);
        epicTask = new EpicTask("Epic1", "EpicDescription");
        taskManager.addTask(epicTask);
        subTask = new SubTask("SubTask", "SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);

    }

    @Test // тестирование добавления задачи
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

    @Test // тестирование обновления задачи
    public void test8_updateTask() {

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() {
                taskManager.updateTask(0, null);
            }
        });

        assertEquals("неверные входные данные", exception.getMessage());
        createTask();
        final int taskId = task.getId();
        task.setName("Name1");
        task.setDescription("Description1");
        task.setStatus(Status.DONE);
        taskManager.updateTask(taskId, task);
        final int epicId = epicTask.getId();
        epicTask.setName("newEpicName");
        taskManager.updateTask(epicId, epicTask);
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

    @Test // тестирование удаления задачи по id
    public void test9_shouldGetTrueAfterRemoveById() {
        createTask();
        assertTrue(taskManager.removeById(1));
        assertTrue(taskManager.removeById(3));
        assertTrue(taskManager.removeById(2));

    }

    @Test //тестирование удаления задачи с неверным  идентификатором
    public void test10_shouldGetFalseAfterRemoveById() {
        assertFalse(taskManager.removeById(0));
    }
    @Test //тестирование возвращения списка всех задач
    public void test11_shouldGet1GetAllTask() {
        createTask();
        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }

    @Test //тестирование возвращения списка всех подзадач
    public void test12_shouldGet1GetAllSubTask() {
        createTask();
        final List<SubTask> tasks = taskManager.getAllSubTask();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(subTask, tasks.get(0));
    }
    @Test //тестирование возвращения списка всех эпиков
    public void test13_shouldGet1GetAllEpicTask() {
        createTask();
        final List<EpicTask> tasks = taskManager.getAllEpicTask();

        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals(epicTask, tasks.get(0));
    }

    @Test //тестирование возвращения списка подзадач из эпика
    public void test14_shouldGet2GetSubTaskFromEpic() {
        createTask();
        SubTask subTask1 = new SubTask("SubTask1", "SubTaskDescription1", epicTask.getId());
        taskManager.addTask(subTask);
        final List<SubTask> tasks = taskManager.getSubTaskFromEpic(epicTask);

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
    }

    @Test //тестирование удаления задач
    public void test15_shouldGetTrueRemoveAllDifferentTasks() {
        createTask();
        taskManager.removeAllTask();
        final List<Task> tasks = taskManager.getAllTask();
        assertTrue(tasks.isEmpty());
        taskManager.removeAllEpic();
        final List<EpicTask> epics = taskManager.getAllEpicTask();
        final List<SubTask> subTasks = taskManager.getAllSubTask();
        assertTrue(subTasks.isEmpty());
        assertTrue(epics.isEmpty());
    }

    @Test //тестирование возвращения задачи с неверным id
    public void test15_shouldGetNullGetDifferentTasksById() {
        createTask();
        assertNull(taskManager.getSubTaskById(0));
        assertNull(taskManager.getTaskById(0));
        assertNull(taskManager.getEpicTaskById(0));
    }

    @Test //тестирование истории
    void test16_shouldTrueGetHistory() {
        final List<TaskBase> history = taskManager.history();
        assertTrue(history.isEmpty());
        createTask();
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(2);
        taskManager.getSubTaskById(3);
        final List<TaskBase> historyNew = taskManager.history();
        assertEquals(3,historyNew.size());
    }
    @Test //тестирование добавления в сортированный список
    void test22_getSortTask(){
        assertTrue(taskManager.getSortTask().isEmpty());
        createTask();
        assertEquals(2,taskManager.getSortTask().size());

    }

    @Test /*тестирование поиска пересечений. В данном тесте создание задач оставленно
    по причине необходимости создания задач последовательно по одной и тестирования работы метода после каждой
    новой задачи*/
    void test23_canSaveTaskInSortSet(){
        LocalDateTime startTime1 = LocalDateTime.parse("2022-04-10T17:50");
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime startTimeIsBefore = startTime1.minusMinutes(20);
        LocalDateTime startTimeIsAfter = startTime1.plusMinutes(10);
        Duration longDuration = Duration.ofMinutes(100);
        task = new Task("Задача 1", "тестирование кода 1");
        assertTrue(taskManager.canSaveTaskInSortSet(task));
        taskManager.addTask(task);
        EpicTask epicTask = new EpicTask("Epic1", "EpicDescription");
        assertTrue(taskManager.canSaveTaskInSortSet(epicTask));
        taskManager.addTask(epicTask);
        SubTask subTask =
                new SubTask("SubTask", "SubTaskDescription", epicTask.getId(),duration,startTime1);
        assertTrue(taskManager.canSaveTaskInSortSet(subTask));
        taskManager.addTask(subTask);
        SubTask subTask1 =
                new SubTask("SubTask", "SubTaskDescription", epicTask.getId(),duration,startTime1);
        assertFalse(taskManager.canSaveTaskInSortSet(subTask1));
        SubTask subTask2 =
                new SubTask("SubTask", "SubTaskDescription", epicTask.getId(),duration,startTimeIsBefore);
        assertFalse(taskManager.canSaveTaskInSortSet(subTask1));
        SubTask subTask3 =
                new SubTask("SubTask", "SubTaskDescription", epicTask.getId(),duration,startTimeIsAfter);
        assertFalse(taskManager.canSaveTaskInSortSet(subTask1));
        SubTask subTask4 =
                new SubTask("SubTask", "SubTaskDescription",
                        epicTask.getId(),longDuration,startTimeIsBefore);
        assertFalse(taskManager.canSaveTaskInSortSet(subTask2));
        assertFalse(taskManager.canSaveTaskInSortSet(subTask3));
        assertFalse(taskManager.canSaveTaskInSortSet(subTask4));

    }
}
