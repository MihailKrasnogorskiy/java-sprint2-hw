package controllers;

import org.junit.jupiter.api.Test;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public FileBackedTasksManagerTest() {
        super((FileBackedTasksManager) Managers.getRestorableMeneger());
    }

    @Test
    void loadFromFile() {
    }
}