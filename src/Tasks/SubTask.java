package Tasks;

public class SubTask extends Task{
    int idOfEpic; // id эпика, к которому принадлежит subtask

    public SubTask(String name, String description, int idOfEpic, int id) {
        super(name, description, id);
        this.idOfEpic = idOfEpic;
    }

    public int getIdOfEpic(){
        return idOfEpic;
    }

    public void setIdOfEpic(int idOfEpic){
        this.idOfEpic = idOfEpic;
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
                "idOfEpic=" + idOfEpic +
                ", name='" + name + '\'' +
                ", description.length='" + descriptionLength + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
