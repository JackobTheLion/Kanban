package tasks;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected String status = "NEW"; //по умолчанию при создании задачи ее статус NEW

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

    public void setStatus(String status) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}
