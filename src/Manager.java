import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

import java.util.HashMap;

public class Manager {

    HashMap<Integer, Task> taskList = new HashMap<>();
    HashMap<Integer, EpicTask> epicTaskList = new HashMap<>();
    HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private int id = 1;

    public HashMap<Integer, Object> getListOfTasks(int type){
        HashMap<Integer, Object> listOfTasks = new HashMap<>();
        if (type == 1){
            for (Integer id : taskList.keySet()){
                listOfTasks.put(id, taskList.get(id));
            }
        } else if (type == 2){
            for (Integer id : epicTaskList.keySet()){
                listOfTasks.put(id, epicTaskList.get(id));
            }
        } else if (type == 3){
            for (Integer id : subTaskList.keySet()){
                listOfTasks.put(id, subTaskList.get(id));
            }
        } else
            System.out.println("Ошибка вывода списка задач");
        return listOfTasks;
    }

    public void deleteAllTasks(int type){
        if (type == 1) {
            taskList.clear();
            System.out.println("Список tasks очищен");
        } else if (type == 2) {
            epicTaskList.clear();
            System.out.println("Список epicTasks очищен");
        } else if (type == 3) {
            subTaskList.clear();
            System.out.println("Список subTasks очищен");
        }
    }

    public Task getTaskById(int id){
        Task taskToReturn;
        for (Integer i : taskList.keySet()){
            if (i == id){
                taskToReturn = taskList.get(id);
                return taskToReturn;
            }
        }
        for (Integer i : epicTaskList.keySet()){
            if (i == id){
                taskToReturn = epicTaskList.get(id);
                return taskToReturn;
            }
        }
        for (Integer i : subTaskList.keySet()){
            if (i == id){
                taskToReturn = subTaskList.get(id);
                return taskToReturn;
            }
        }
        System.out.println("Задача с ID " + id + " не найдена.");
        return null;
    }

    public void addNewTask(Object task){
        if(task.getClass() == Task.class) {
            Task thisTask = (Task) task;
            taskList.put(id, thisTask);
            ((Task) task).setId(id);
            id++;

        }else if(task.getClass() == EpicTask.class) {
            EpicTask thisTask = (EpicTask) task;
            epicTaskList.put(id, thisTask);
            thisTask.setId(id);
            id++;

        } else if(task.getClass() == SubTask.class) {
            SubTask thisTask = (SubTask) task;
            subTaskList.put(thisTask.getId(), thisTask);
        }
    }

    public void updateTask(){

    }

    public void deleteById(int id){

    }

    public HashMap<Integer, SubTask> getSubTasksOfEpic() {
        return null;
    }

    public void setStatus(Object task, int statusId) {
        if (task.getClass() == Task.class){ //если устанавливаем статус Task
            Task thisTask = (Task) task;
            thisTask.setStatus(statusId);

        } else if (task.getClass() == SubTask.class) { //если устанавливаем статус SubTask
            SubTask thisSubTask = (SubTask) task;
            thisSubTask.setStatus(statusId);

            EpicTask thisEpicTask = epicTaskList.get(thisSubTask.getIdOfEpic());
            HashMap<Integer, SubTask> subTasksOfEpic = thisEpicTask.getSubTasksOfEpic();

            int statusNew = 0;
            int statusDone = 0;
            for (SubTask subTask : subTasksOfEpic.values()) {
                if (subTask.getStatus().equals("NEW")) {
                    statusNew++;
                } else if (subTask.getStatus().equals("DONE")) {
                    statusDone++;
                }

                if (statusNew == subTasksOfEpic.size()) {
                    thisEpicTask.setStatus(1);
                } else if (statusDone == subTasksOfEpic.size()) {
                    thisEpicTask.setStatus(2);
                } else {
                    thisEpicTask.setStatus(3);
                }
            }
        } else System.out.println("Ошибка присвоения статуса в manager.setStatus");
    }

    public SubTask createSubTask(String name, String description, int idOfEpic) {
        //subTask можно создать только в том случае, если есть соответствующий epicTask
        if (epicTaskList.containsKey(idOfEpic)){
            SubTask subTask = new SubTask(name, description, idOfEpic, id);

            EpicTask epicTask = epicTaskList.get(idOfEpic);
            HashMap<Integer, SubTask> subTasksOfEpic = epicTask.getSubTasksOfEpic();
            subTasksOfEpic.put(id, subTask);

            id++;
            return subTask;
        } else
            System.out.println("Указанного EpicTask не существует");
        return null;
    }

    public Task createTask(String name, String description) {
        Task task  = new Task(name,description);
        return task;
    }

    public EpicTask createEpicTask (String name, String description){
        EpicTask epicTask = new EpicTask(name, description);
        return epicTask;
    }
}
