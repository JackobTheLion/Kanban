package Tasks;

public class SubTask extends Task{
    int idOfEpic; // id эпика, к которому принадлежит subtask

    public SubTask(String name, String description, int idOfEpic, int id) {
        super(name, description);
        this.idOfEpic = idOfEpic;
        this.id = id;
    }

    public int getIdOfEpic(){
        return idOfEpic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "idOfEpic=" + idOfEpic +
                ", name='" + name + '\'' +
                ", description='" + description.length() + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
