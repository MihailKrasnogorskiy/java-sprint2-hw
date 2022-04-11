package model;

import java.time.Duration;
import java.time.ZonedDateTime;

// абстрактный класс задачи
public abstract class TaskBase {
    String name;
    String description;
    int id;
    Status status;
    Duration duration;
    ZonedDateTime startTime;
    ZonedDateTime endTime;

    public TaskBase(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public TaskBase(String name, String description, int id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
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
        return hash + id;
    }

    public ZonedDateTime getEndTime() {
        if (startTime == null) {
            endTime = null;
            return endTime;
        }
        endTime = startTime.plus(duration);
        return endTime;
    }
}
