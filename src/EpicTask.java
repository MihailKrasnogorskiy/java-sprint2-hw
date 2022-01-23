import java.util.HashMap;

public class EpicTask {

    private String name;
    private String description;
    private Integer id;
    private String status;
    private HashMap<Integer, SubTask> subTasks;

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

    public void checkStatus() {

    }

}
