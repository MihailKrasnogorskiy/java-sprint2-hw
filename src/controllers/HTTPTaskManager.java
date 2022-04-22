package controllers;

import clients.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.TaskBase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//класс http енеджера
public class HTTPTaskManager extends FileBackedTasksManager {
    static Gson gson = new Gson();
    private final String KEY_TASKS_META_DATA = "tasksMetaData";
    private final String KEY_HISTORY = "history";
    private final KVTaskClient client;

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        super(null);
        this.client = new KVTaskClient(url);
    }

    //загрузка с сервера
    public static HTTPTaskManager loadFromServer(String url) throws IOException, InterruptedException {
        HTTPTaskManager manager = new HTTPTaskManager(url);
        KVTaskClient client = manager.getClient();
        String ids = client.load(manager.KEY_TASKS_META_DATA);
        if (!ids.equals("isEmpty")) {
            String[] tasksArray = ids.split(",");
            String[] tasks = new String[tasksArray.length];
            for (int i = 0; i < tasksArray.length; i++) {
                tasks[i] = client.load(tasksArray[i]);
            }
            Arrays.stream(tasks)
                    .map(HTTPTaskManager::tasksDeserialization)
                    .forEach(manager::restoreTask);
        }

        String historyLine = client.load(manager.KEY_HISTORY);
        if (!historyLine.equals("isEmpty")) {
            String[] history = historyLine.split(",");
            Arrays.stream(history)
                    .forEach(id -> manager.restoreTasksInHistoryById((Integer.parseInt(id))));
        }
        return manager;
    }

    //десериализация входящих задач
    private static TaskBase tasksDeserialization(String body) {
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("TYPE").getAsString();
        if (type.equals("TASK")) {
            return gson.fromJson(body, Task.class);
        } else if (type.equals("SUBTASK")) {
            return gson.fromJson(body, SubTask.class);
        } else {
            return gson.fromJson(body, EpicTask.class);
        }
    }

    public KVTaskClient getClient() {
        return client;
    }

    @Override
    //сохранение на сервер
    public void save() {
        List<TaskBase> list = getAllTasks();
        StringBuilder Ids = new StringBuilder();
        StringBuilder history = new StringBuilder();
        if (!list.isEmpty()) {
            for (TaskBase task : list) {
                Ids.append(task.getId());
                Ids.append(",");
                try {
                    client.put(String.valueOf(task.getId()), gson.toJson(task));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                client.put(KEY_TASKS_META_DATA, Ids.toString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                client.put(KEY_TASKS_META_DATA, "isEmpty");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!history().isEmpty()) {
            for (TaskBase taskBase : history()) {
                history.append(taskBase.getId());
                history.append(",");
            }
            try {
                client.put(KEY_HISTORY, history.toString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                client.put(KEY_HISTORY, "isEmpty");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
