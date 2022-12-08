package Tasks;

public class Task {
    String name;
    String description;
    int id;
    String status = "NEW";

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(int statusId){
        if (statusId == 1) {
            this.status = "NEW";
        } else if (statusId == 2) {
            this.status = "IN_PROGRESS";
        } else if (statusId == 3) {
            this.status = "DONE";
        } else System.out.println("Ошибка присвоения статуса. Неверный statusId");
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description.length() + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
