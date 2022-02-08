package model;

import java.util.Objects;

// класс подзадачи
public class SubTask extends TaskBase {
    private EpicTask epic;

    public SubTask(String name, String description, EpicTask epic) {
        super(name, description);
        this.epic = epic;
        this.epic.addSubTask(this);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public EpicTask getEpic() {
        return epic;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + epic.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        SubTask subTask = (SubTask) o;
        return id == subTask.id && Objects.equals(name, subTask.name)
                && Objects.equals(description, subTask.description) && status == subTask.status
                && epic.equals(subTask.epic);
    }
}
