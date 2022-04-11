package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
//класс для тестирования класса Epic
class EpicTaskTest {

       @Test//тестирование расчёта статуса
    void test1_shouldGetStatusNewWithEmptySubtaskList() {
        EpicTask epicTask = new EpicTask("Epic1", "description");
        assertEquals(Status.NEW, epicTask.getStatus());
    }

    @Test//тестирование расчёта статуса
    void test2_shouldGetStatusNew() {
        EpicTask epicTask = new EpicTask("Epic1", "description");
        assertEquals(Status.NEW, epicTask.getStatus());
        SubTask subTask1 = new SubTask("subtask1", "descriprion", epicTask.getId());
        SubTask subTask2 = new SubTask("subtask2", "descriprion", epicTask.getId());
        SubTask subTask3 = new SubTask("subtask3", "descriprion", epicTask.getId());
        SubTask subTask4 = new SubTask("subtask4", "descriprion", epicTask.getId());
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        epicTask.addSubTask(subTask3);
        epicTask.addSubTask(subTask4);
        assertEquals(Status.NEW, epicTask.getStatus());
    }

    @Test//тестирование расчёта статуса
    void test3_shouldGetStatusInProgressIfAllSubTaskInProgress() {
        EpicTask epicTask = new EpicTask("Epic1", "description");
        SubTask subTask1 = new SubTask("subtask1", "descriprion", epicTask.getId());
        subTask1.setStatus(Status.IN_PROGRESS);
        SubTask subTask2 = new SubTask("subtask2", "descriprion", epicTask.getId());
        subTask2.setStatus(Status.IN_PROGRESS);
        SubTask subTask3 = new SubTask("subtask3", "descriprion", epicTask.getId());
        subTask3.setStatus(Status.IN_PROGRESS);
        SubTask subTask4 = new SubTask("subtask4", "descriprion", epicTask.getId());
        subTask4.setStatus(Status.IN_PROGRESS);
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        epicTask.addSubTask(subTask3);
        epicTask.addSubTask(subTask4);
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus());
    }

    @Test//тестирование расчёта статуса
    void test4_shouldGetStatusInProgressIfSubTaskStatusDifferent() {
        EpicTask epicTask = new EpicTask("Epic1", "description");
        SubTask subTask1 = new SubTask("subtask1", "descriprion", epicTask.getId());
        epicTask.addSubTask(subTask1);
        subTask1.setStatus(Status.DONE);
        SubTask subTask2 = new SubTask("subtask2", "descriprion", epicTask.getId());
        epicTask.addSubTask(subTask2);
        subTask2.setStatus(Status.NEW);
        SubTask subTask3 = new SubTask("subtask3", "descriprion", epicTask.getId());
        epicTask.addSubTask(subTask3);
        subTask3.setStatus(Status.DONE);
        SubTask subTask4 = new SubTask("subtask4", "descriprion", epicTask.getId());
        epicTask.addSubTask(subTask4);
        subTask4.setStatus(Status.DONE);
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus());
    }

    @Test//тестирование расчёта статуса
    void test5_shouldGetStatusInProgressIfAllSubTaskDone() {
        EpicTask epicTask = new EpicTask("Epic1", "description");
        SubTask subTask1 = new SubTask("subtask1", "descriprion", epicTask.getId());
        subTask1.setStatus(Status.DONE);
        SubTask subTask2 = new SubTask("subtask2", "descriprion", epicTask.getId());
        subTask2.setStatus(Status.DONE);
        SubTask subTask3 = new SubTask("subtask3", "descriprion", epicTask.getId());
        subTask3.setStatus(Status.DONE);
        SubTask subTask4 = new SubTask("subtask4", "descriprion", epicTask.getId());
        subTask4.setStatus(Status.DONE);
        epicTask.addSubTask(subTask1);
        epicTask.addSubTask(subTask2);
        epicTask.addSubTask(subTask3);
        epicTask.addSubTask(subTask4);
        assertEquals(Status.DONE, epicTask.getStatus());
    }

    @Test//тестирование расчёта статуса
    void test6_shouldGet1addSubTask() {
        EpicTask epicTask = new EpicTask("Epic1", "description");
        SubTask subTask1 = new SubTask("subtask1", "descriprion", epicTask.getId());
        epicTask.addSubTask(subTask1);
        assertEquals(1,epicTask.getSubTasks().size());
    }
}