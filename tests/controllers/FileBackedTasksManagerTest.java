package controllers;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

//класс для тестирования класса FileBackedTasksManager
class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public FileBackedTasksManagerTest() throws IOException {
        super((FileBackedTasksManager) Managers.getRestorableManagerForTests());
    }

    File loadFile = new File("src/resources/loadFile.csv");
    File loadFileWithoutHistory = new File("src/resources/loadFileWithoutHistory.csv");
    File test = new File("src/resources/test.csv");
    File fileIsEmpty = new File("src/resources/fileIsEmpty.csv");
    File testWithOutHistory = new File("src/resources/testWithOutHistory.csv");

    @Test
        //тестирование загрузки из файла
    void test20_loadFromFile() {
        FileBackedTasksManager emptyManager = FileBackedTasksManager.loadFromFile(fileIsEmpty);
        assertTrue(emptyManager.history().isEmpty());
        assertTrue(emptyManager.getAllTask().isEmpty());
        assertTrue(emptyManager.getAllSubTask().isEmpty());
        assertTrue(emptyManager.getAllEpicTask().isEmpty());
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
        final List<TaskBase> historyIsEmpty = loadedManagerWithOutHistory.history();
        assertTrue(historyIsEmpty.isEmpty());
    }

    @Test
        //тестирование сохранения в файл
    void test21_saveToFile() {
        taskManager.save();
        compareFiles(fileIsEmpty, taskManager.getFile());
        LocalDateTime startTime1 = LocalDateTime.parse("2022-04-10T17:50");
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime startTime2 = startTime1.plusHours(1);
        LocalDateTime startTime3 = startTime2.plusHours(1);
        LocalDateTime startTime4 = startTime3.plusHours(1);
        // создание объектов задач, подзадач, эпиков и менеджера
        Task task1 = new Task("Задача 1", "тестирование кода 1", duration, startTime1);
        Task task2 = new Task("Задача 2", "тестирование кода 2", duration, startTime2);
        EpicTask epic1 = new EpicTask("эпик 1", "что-то большое сложное 1");
        EpicTask epic2 = new EpicTask("эпик 2", "что-то большое сложное 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(epic1);
        taskManager.addTask(epic2);
        SubTask subTask11 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1", epic1.getId(),
                duration, startTime3);
        SubTask subTask12 = new SubTask("подзадача 1.2", "что-то маленькое и лёгкое 1.2", epic1.getId(),
                duration, startTime4);
        SubTask subTask13 = new SubTask("подзадача 1.3", "что-то маленькое и лёгкое 1.2", epic1.getId());
        taskManager.addTask(subTask11);
        taskManager.addTask(subTask12);
        taskManager.addTask(subTask13);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(4);
        compareFiles(test, taskManager.getFile());
        FileBackedTasksManager managerWithOutHistory = (FileBackedTasksManager) Managers.getRestorableManagerForTests();
        managerWithOutHistory.addTask(task1);
        managerWithOutHistory.addTask(task2);
        managerWithOutHistory.addTask(epic1);
        managerWithOutHistory.addTask(epic2);
        managerWithOutHistory.addTask(subTask11);
        managerWithOutHistory.addTask(subTask12);
        managerWithOutHistory.addTask(subTask13);
        compareFiles(testWithOutHistory, managerWithOutHistory.getFile());

    }

    private void compareFiles(File test, File file) {
        String[] testString = new String[10];
        String[] fileString = new String[10];
        int i = 0;

        try {
            Scanner scanner = new Scanner(test);
            while (scanner.hasNext()) {
                testString[i] = scanner.nextLine();
                i++;
            }
            Scanner scanner1 = new Scanner(file);
            i = 0;
            while (scanner1.hasNext()) {
                fileString[i] = scanner1.nextLine();
                i++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int y = 0; y < testString.length; y++) {
            assertEquals(testString[y], fileString[y]);
        }
    }
}