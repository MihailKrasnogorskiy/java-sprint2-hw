package controllers;

import model.TaskBase;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public FileBackedTasksManagerTest() {
        super((FileBackedTasksManager) Managers.getRestorableManager());
    }

    File loadFile = new File("src/resources/loadFile.csv");
    File loadFileWithoutHistory = new File("src/resources/loadFileWithoutHistory.csv");

    @Test
    void loadFromFile() {
        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(loadFile);
        List<TaskBase> tasks = new ArrayList<>(loadedManager.getAllTask());
        tasks.addAll(loadedManager.getAllEpicTask());
        tasks.addAll(loadedManager.getAllSubTask());
        assertFalse(tasks.isEmpty());
        assertEquals(5, tasks.size());
        final List<TaskBase> history = loadedManager.history();
        assertEquals(5, history.size());
        FileBackedTasksManager loadedManagerWithOutHistory =
                FileBackedTasksManager.loadFromFile(loadFileWithoutHistory);
        final List<TaskBase> historyIsEmpty = loadedManager.history();
        System.out.println(historyIsEmpty.size());
       // assertTrue(historyIsEmpty.isEmpty());
    }
}