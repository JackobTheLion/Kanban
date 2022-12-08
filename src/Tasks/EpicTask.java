package Tasks;

import java.util.HashMap;

public class EpicTask extends Task{
    HashMap<Integer, SubTask> subTasksOfEpic = new HashMap<>(); // список подзадач эпика ID -> subTask

    public EpicTask(String name, String description) {
        super(name, description);
    }

    public HashMap<Integer, SubTask> getSubTasksOfEpic() {
        return subTasksOfEpic;
    }

    public void getSubTasksOfEpic(HashMap<Integer, SubTask> subTasksOfEpic) {
        this.subTasksOfEpic = subTasksOfEpic;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "subTasksOfEpic=" + subTasksOfEpic +
                ", name='" + name + '\'' +
                ", description='" + description.length() + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
