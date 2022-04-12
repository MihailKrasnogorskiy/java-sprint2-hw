package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//класс для тестирования класса Epic
class EpicTaskTest {
    private EpicTask epicTask = new EpicTask("Epic1", "description");

    @Test
//тестирование расчёта статуса
    void test1_shouldGetStatusNewWithEmptySubtaskList() {
        assertEquals(Status.NEW, epicTask.getStatus());
    }

    @Test
//тестирование расчёта статуса
    void test2_shouldGetStatusNew() {
        createSubTasks(Status.NEW);
        assertEquals(Status.NEW, epicTask.getStatus());
    }

    @Test
//тестирование расчёта статуса
    void test3_shouldGetStatusInProgressIfAllSubTaskInProgress() {
        createSubTasks(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus());
    }

    @Test
//тестирование расчёта статуса
    void test4_shouldGetStatusInProgressIfSubTaskStatusDifferent() {
        createSubTasks(Status.DONE);
        epicTask.getSubTasks().get(1).setStatus(Status.NEW);
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus());
    }

    @Test
//тестирование расчёта статуса
    void test5_shouldGetStatusInProgressIfAllSubTaskDone() {
        createSubTasks(Status.DONE);
        assertEquals(Status.DONE, epicTask.getStatus());
    }

    @Test
        //тестирование возврата списка подзадач
    void test6_shouldGet1addSubTask() {
        SubTask subTask1 = new SubTask("subtask1", "descriprion", epicTask.getId());
        epicTask.addSubTask(subTask1);
        assertEquals(1, epicTask.getSubTasks().size());
    }

    //создание тестового набора
    private void createSubTasks(Status status) {
        SubTask subTask1 = new SubTask("subtask1", "descriprion", epicTask.getId());
        SubTask subTask2 = new SubTask("subtask2", "descriprion", epicTask.getId());
        SubTask subTask3 = new SubTask("subtask3", "descriprion", epicTask.getId());
        SubTask subTask4 = new SubTask("subtask4", "descriprion", epicTask.getId());
        subTask1.setStatus(status);
        subTask2.setStatus(status);
        subTask3.setStatus(status);
        subTask4.setStatus(status);
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        epicTask.addSubTask(subTask3);
        epicTask.addSubTask(subTask4);
    }
}