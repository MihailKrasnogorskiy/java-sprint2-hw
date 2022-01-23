public class SubTask {

    private String name;
    private String description;
    private Integer id;
    private String status;
    private EpicTask epic;

    public SubTask(EpicTask epic) {
        this.epic = epic;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        epic.checkStatus();
    }
}
