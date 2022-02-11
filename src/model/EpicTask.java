package model;

import java.util.ArrayList;
import java.util.Objects;

// класс эпика
public class EpicTask extends TaskBase {

    private ArrayList<SubTask> subTasks;

    public EpicTask(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
        status = Status.NEW;
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) { // добавление подзадачи в эпик
        subTasks.add(subTask);
        checkStatus();
    }

    public void checkStatus() {   // проверка статуса эпика
        if (subTasks.size() == 0) {
            status = Status.NEW;
        }
        int counterNew = 0;
        int counterDone = 0;
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() == Status.DONE) {
                counterDone++;
            } else if (subTask.getStatus() == Status.NEW) {
                counterNew++;
            }
        }
        if (counterDone == subTasks.size()) {
            status = Status.DONE;
        } else if (counterNew == subTasks.size()) {
            status = Status.NEW;
        } else status = Status.IN_PROGRESS;
    }

    @Override
    public String toString() {
        return "model.EpicTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subTasks=" + subTasks +
                '}';
    }

    @Override
    public int hashCode() {
        int hash = 23;
        if (name != null) {
            hash = hash + name.hashCode();
        }
        hash = hash * 31;
        if (description != null) {
            hash = hash + description.hashCode();
        }
        if (status != null) {
            hash = hash + status.hashCode();
        }
        return hash + subTasks.hashCode() + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EpicTask)) return false;
        EpicTask epicTask = (EpicTask) o;
        return id == epicTask.id && Objects.equals(name, epicTask.name)
                && Objects.equals(description, epicTask.description) && status == epicTask.status
                && Objects.equals(subTasks, epicTask.subTasks);
    }
}
