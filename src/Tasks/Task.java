package Tasks;

public class Task {
    String name;
    String description;
    int id;
    String status = "NEW"; //по умолчанию при создании задачи ее статус NEW

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(String status){
        if (status.equals("NEW") || status.equals("IN_PROGRESS") || status.equals("DONE")) {
            this.status = status;
        } else {
            System.out.println("Ошибка присвоения статуса. Неверный status");
        }
    }

    public String getStatus() {
        return status;
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

    @Override
    public String toString() {
        String descriptionLength;
        if (description == null){
            descriptionLength = "null";
        } else {
            descriptionLength = String.valueOf(description.length());
        }

        return "Task{" +
                "name='" + name + '\'' +
                ", description.length='" + descriptionLength + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                "}";
    }
}
