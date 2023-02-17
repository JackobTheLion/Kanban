package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subTasksOfEpic = new ArrayList<>(); // список подзадач эпика ID -> subTask
    private static final TaskType type = TaskType.EPIC;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubTasksOfEpic() {
        return subTasksOfEpic;
    }

    @Override
    public TaskType getType() {
        return type;
    } //без переопределения всегда возвращается значение TASK в CSVTaskFormat.taskFromString(), не понимаю почему

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setSubTasksOfEpic(ArrayList<Integer> subTasksOfEpic) {
        this.subTasksOfEpic = subTasksOfEpic;
    }

    public void addSubTaskId (int id) {
        subTasksOfEpic.add(id);
    }

    public void deleteSubTaskId(int id) {
        subTasksOfEpic.remove(Integer.valueOf(id));
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "name='" + name + '\'' +
                ", description'" + description + '\'' +
                ", start time='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                "subTasksOfEpic=" + subTasksOfEpic +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}' + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasksOfEpic, epic.subTasksOfEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasksOfEpic);
    }
}
