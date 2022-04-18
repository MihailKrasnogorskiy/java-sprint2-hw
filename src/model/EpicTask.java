package model;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Objects;

// класс эпика
public class EpicTask extends TaskBase {

    private final ArrayList<SubTask> subTasks;

    public EpicTask(String name, String description) {
        super(name, description);
        subTasks = new ArrayList<>();
        status = Status.NEW;
    }

    public EpicTask(String name, String description, int id, Status status) {
        super(name, description, id, status);
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) { // добавление подзадачи в эпик
        subTasks.add(subTask);
        getStatus();
        getDuration();
    }

    @Override
    public Status getStatus() {   // проверка и возвращение статуса эпика
        if (subTasks.isEmpty()) {
            status = Status.NEW;
            return Status.NEW;
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
        return status;
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

    @Override
    public String toString() {

        return id + "," + TaskType.EPIC + "," + name + "," + status + "," + description + ","
                + startTime + "," + duration;
    }

    @Override
    public Duration getDuration() {
        if (!subTasks.isEmpty()) {
            ZonedDateTime minStartTime = subTasks.get(0).getStartTime();
            ZonedDateTime maxEndTIme = subTasks.get(0).getEndTime();

            for (SubTask subTask : subTasks) {
                if (subTask.getStartTime() == null || subTask.getEndTime() == null) {
                    continue;
                }
                if (subTask.getStartTime().isBefore(minStartTime)) {
                    minStartTime = subTask.getStartTime();
                }
                if (subTask.getEndTime().isAfter(maxEndTIme)) {
                    maxEndTIme = subTask.getEndTime();
                }
                duration = Duration.between(minStartTime, maxEndTIme);
                endTime = maxEndTIme;
                startTime = minStartTime;
            }
            return duration;
        }
        return Duration.ZERO;
    }

    @Override
    public ZonedDateTime getEndTime() {
        return endTime;
    }
}
