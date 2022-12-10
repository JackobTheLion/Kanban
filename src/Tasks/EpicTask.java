package Tasks;

import java.util.HashMap;

public class EpicTask extends Task{
    HashMap<Integer, SubTask> subTasksOfEpic = new HashMap<>(); // список подзадач эпика ID -> subTask

    public EpicTask(String name, String description, int id) {
        super(name, description, id);
    }

    public HashMap<Integer, SubTask> getSubTasksOfEpic() {
        return subTasksOfEpic;
    }

    public void setSubTasksOfEpic(HashMap<Integer, SubTask> subTasksOfEpic) {
        this.subTasksOfEpic = subTasksOfEpic;
    }

    @Override
    public String toString() {
        String descriptionLength;
        if (description == null){
            descriptionLength = "null";
        } else {
            descriptionLength = String.valueOf(description.length());
        }
        String subTasksOfEpicSize;
        if (subTasksOfEpic == null){
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
}
