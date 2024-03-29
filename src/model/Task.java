package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

// класс задачи
public class Task extends TaskBase {
    private final TaskType TYPE = TaskType.TASK;

    public Task(String name, String description) {
        super(name, description);
    }

    public Task(String name, String description, int id, Status status) {
        super(name, description, id, status);
    }

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        super(name, description);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status;
    }

    public String toString() {

        return id + "," + TYPE + "," + name + "," + status + "," + description + ","
                + startTime + "," + duration;
    }
}
