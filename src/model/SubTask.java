package model;

// класс подзадачи
public class SubTask extends Task {
    private EpicTask epic;

    public SubTask(String name, String description, EpicTask epic) {
        super(name, description);
        this.epic = epic;
        this.epic.addSubTask(this);
    }

    public EpicTask getEpic() {
        return epic;
    }

    public int hashCode() {
        return super.hashCode() + epic.hashCode();
    }
}
