import java.util.ArrayList;

// класс эпика
public class EpicTask {

    private String name;
    private String description;
    private int id;
    private Status status;
    private ArrayList<SubTask> subTasks;

    public EpicTask(String name, String description) {
        this.name = name;
        this.description = description;
        subTasks = new ArrayList<>();
        status = Status.NEW;
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
        return "EpicTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subTasks=" + subTasks +
                '}';
    }
}
