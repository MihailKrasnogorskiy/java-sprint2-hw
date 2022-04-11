package controllers;
//класс для тестирования класса InMemoryTaskManagerTest
class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() {
        super((InMemoryTaskManager) Managers.getDefaultTaskManager());
    }


}