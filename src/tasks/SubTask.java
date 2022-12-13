package tasks;

import java.util.Objects;

public class SubTask extends Task {
    private int epicId; // id эпика, к которому принадлежит subtask

    public SubTask(String name, String description, int idOfEpic) {
        super(name, description);
        this.epicId = idOfEpic;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String descriptionLength;
        if (description == null){
            descriptionLength = "null";
        } else {
            descriptionLength = String.valueOf(description.length());
        }

        return "SubTask{" +
                "idOfEpic=" + epicId +
                ", name='" + name + '\'' +
                ", description.length='" + descriptionLength + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
