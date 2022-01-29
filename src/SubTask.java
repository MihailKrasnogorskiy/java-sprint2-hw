import jdk.jshell.Snippet;

// класс подзадачи
public class SubTask {

    private String name;
    private String description;
    private int id;
    private Status status;
    private EpicTask epic;

    public SubTask(String name, String description, EpicTask epic) {
        this.name = name;
        this.description = description;
        this.epic = epic;
        status = Status.NEW;
        this.epic.addSubTask(this);
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

    public void setStatus(Status status) {
        this.status = status;
        epic.checkStatus();
    }

    public EpicTask getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
