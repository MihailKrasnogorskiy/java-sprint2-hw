package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

// класс подзадачи
public class SubTask extends TaskBase {
    private final TaskType TYPE = TaskType.SUBTASK;
    private final int epic;

    public SubTask(String name, String description, int epic) {
        super(name, description);
        this.epic = epic;
    }

    public SubTask(String name, String description, int id, Status status, int epic) {
        super(name, description, id, status);
        this.epic = epic;
    }

    public SubTask(String name, String description, int epic, Duration duration, LocalDateTime startTime) {
        super(name, description);
        this.epic = epic;
        this.duration = duration;
        this.startTime = startTime;
        endTime = getEndTime();
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getEpic() {
        return epic;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        SubTask subTask = (SubTask) o;
        return id == subTask.id && Objects.equals(name, subTask.name)
                && Objects.equals(description, subTask.description) && status == subTask.status
                && epic == subTask.epic;
    }

    public String toString() {

        return id + "," + TYPE + "," + name + "," + status + "," + description + "," + epic + ","
                + startTime + "," + duration;
    }
}
