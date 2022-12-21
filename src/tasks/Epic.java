package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subTasksOfEpic = new ArrayList<>(); // список подзадач эпика ID -> subTask

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubTasksOfEpic() {
        return subTasksOfEpic;
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
        String descriptionLength;
        if (description == null) {
            descriptionLength = "null";
        } else {
            descriptionLength = String.valueOf(description.length());
        }
        String subTasksOfEpicSize;
        if (subTasksOfEpic == null) {
            subTasksOfEpicSize = "null";
        } else {
            subTasksOfEpicSize = String.valueOf(subTasksOfEpic.size());
        }

        return "EpicTask{" +
                ", name='" + name + '\'' +
                ", description.length='" + descriptionLength + '\'' +
                "subTasksOfEpic.size=" + subTasksOfEpicSize +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
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
