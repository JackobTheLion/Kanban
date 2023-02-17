package tasks;

import java.util.Objects;

public class SubTask extends Task {
    private int epicId; // id эпика, к которому принадлежит subtask
    private static final TaskType type = TaskType.SUBTASK;

    public SubTask(String name, String description, int idOfEpic, Status status) {
        super(name, description, status);
        this.epicId = idOfEpic;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return type;
    } //без переопределения всегда возвращается значение TASK в CSVTaskFormat.taskFromString(), не понимаю почему

    @Override
    public String toString() {
        return "SubTask{" +
                "idOfEpic=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", start time='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}' + System.lineSeparator();
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
