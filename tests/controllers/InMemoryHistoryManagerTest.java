package controllers;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//класс для тестирования класса InMemoryHistoryManagerTest
class InMemoryHistoryManagerTest {
    HistoryTaskManager historyTaskManager = Managers.getDefaultHistory();
    TaskManager taskManager = Managers.getDefaultTaskManager();


    @Test
    void test17_addTask() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                historyTaskManager.addTask(null);
            }
        });

        assertEquals("передан null", exception.getMessage());
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        Task task1 = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        taskManager.addTask(task1);
        historyTaskManager.addTask(task);
        final List<TaskBase> history = historyTaskManager.getHistory();
        assertEquals(1,history.size());
        historyTaskManager.addTask(task1);
        historyTaskManager.addTask(task);
        final List<TaskBase> historyNew = historyTaskManager.getHistory();
        assertEquals(2,historyNew.size());

    }

    @Test
    void test18_getHistory() {
        final List<TaskBase> history = historyTaskManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void test19_removeTaskInHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        EpicTask epicTask = new EpicTask("Epic1", "EpicDescription");
        taskManager.addTask(epicTask);
        SubTask subTask = new SubTask("SubTask", "SubTaskDescription", epicTask.getId());
        taskManager.addTask(subTask);
        SubTask subTask1 = new SubTask("SubTask1", "SubTaskDescription1", epicTask.getId());
        taskManager.addTask(subTask);
        historyTaskManager.addTask(task);
        historyTaskManager.addTask(epicTask);
        historyTaskManager.addTask(subTask);
        historyTaskManager.addTask(subTask1);
        historyTaskManager.removeTaskInHistory(epicTask);
        assertEquals(3,historyTaskManager.getHistory().size());
        historyTaskManager.removeTaskInHistory(subTask1);
        assertEquals(2,historyTaskManager.getHistory().size());
        historyTaskManager.removeTaskInHistory(task);
        assertEquals(1,historyTaskManager.getHistory().size());
        historyTaskManager.removeTaskInHistory(subTask);
        assertTrue(historyTaskManager.getHistory().isEmpty());



    }
}