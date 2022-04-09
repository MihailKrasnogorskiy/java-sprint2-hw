package controllers;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() {
        super((InMemoryTaskManager) Managers.getDefaultTaskManager());
    }


}