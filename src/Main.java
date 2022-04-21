import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controllers.Managers;
import controllers.TaskManager;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;
import servers.HttpTaskServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    static Gson gson = new Gson();

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskServer server = new HttpTaskServer();


        TaskManager manager = Managers.getDefaultTaskManager();
        LocalDateTime startTime1 = LocalDateTime.MIN;
        Duration duration = Duration.ofMinutes(30);
        LocalDateTime startTime2 = startTime1.plusHours(1);
//        LocalDateTime startTime3 = startTime2.plusHours(1);
//        ZonedDateTime startTime4 = startTime3.plusHours(1);
//        // создание объектов задач, подзадач, эпиков и менеджера
        Task task1 = new Task("Задача 1", "тестирование кода 1", duration, startTime1);
        manager.addTask(task1);
        System.out.println(gson.toJson(task1));
//        Task task2 = new Task("Задача 2", "тестирование кода 2", duration, startTime1);
//        Task task3 = new Task("Задача 2", "тестирование кода 2");
//        Task task4 = new Task("Задача 2", "тестирование кода 2");
//        task4.getEndTime();
////
////        System.out.println(epic1.getStatus());
        EpicTask epic2 = new EpicTask("эпик 2", "что-то большое сложное 2");

        manager.addTask(epic2);
        System.out.println(gson.toJson(epic2));
   //     System.out.println(s1);
////        manager.addTask(task1);
////        manager.addTask(task2);
//        manager.addTask(task3);
////        manager.addTask(task4);
////        System.out.println(manager.getSortTask());
////        manager.removeById(2);
////
////        System.out.println(manager.getSortTask());
////        task1.setStatus(Status.DONE);
////        System.out.println(manager.getSortTask());
////        manager.removeAllTask();
////        System.out.println(manager.getSortTask());
//        EpicTask epic1 = new EpicTask("эпик 1", "что-то большое сложное 1");
//        manager.addTask(epic1);
////        System.out.println(manager.getSortTask());
////
//       manager.addTask(epic1);
////        manager.addTask(epic2);
//        SubTask subTask11 = new SubTask("подзадача 1.1", "что-то маленькое и лёгкое 1.1",
//                epic1.getId());
//
////        SubTask subTask12 = new SubTask("подзадача 1.2", "что-то маленькое и лёгкое 1.2",
////                epic1.getId(), duration, startTime4);
////
//       manager.addTask(subTask11);
//        System.out.println(epic1.getDuration());
//        System.out.println(epic1.getEndTime());
////        manager.addTask(subTask12);
////        System.out.println(epic1);
////        System.out.println(manager.getSortTask());
//////        SubTask subTask13 = new SubTask("подзадача 1.3", "что-то маленькое и лёгкое 1.2", epic1.getId());
//        System.out.println(gson.toJson(manager.getAllSubTask()));
    }

    public static TaskBase tasksDeserialization(String body) {
        JsonElement jsonElement = JsonParser.parseString(body);
        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("request body is not json object");
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("TYPE").getAsString();
        if (type.equals("TASK")) {
            return gson.fromJson(body, Task.class);
        } else if (type.equals("SUBTASK")) {
            return gson.fromJson(body, SubTask.class);
        } else if (type.equals("EPIC")) {
            return gson.fromJson(body, EpicTask.class);
        } else {
            throw new IllegalArgumentException("request body is not tasks");
        }
    }
}