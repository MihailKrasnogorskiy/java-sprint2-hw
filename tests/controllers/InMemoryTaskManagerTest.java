package controllers;

import java.io.IOException;

//класс для тестирования класса InMemoryTaskManagerTest
class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() throws IOException {
        super((InMemoryTaskManager) Managers.getDefaultTaskManager());
    }


}