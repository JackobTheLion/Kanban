import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

import java.util.HashMap;

public class Manager {

    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, EpicTask> epicTaskList = new HashMap<>();
    private HashMap<Integer, SubTask> subTaskList = new HashMap<>();
    private int id = 1;

    public HashMap<Integer, Task> getListOfTasks(int type) {
        HashMap<Integer, Task> listOfAllTasks = new HashMap<>(); //создаем мапу, которую будем возвращать
        // мапа в значениях имеет родительский класс Task т.к. мы не знаем мапу каких задач будем возвращать
        switch (type) {
            case 1:
                return taskList;
            case 2:
                for (Integer id : epicTaskList.keySet()) {
                    listOfAllTasks.put(id, epicTaskList.get(id));
                }
                break;
            case 3:
                for (Integer id : subTaskList.keySet()) {
                    listOfAllTasks.put(id, subTaskList.get(id));
                }
                break;
            default: //отлов ошибки на случай неверного запроса
                System.out.println("Ошибка. Неверно указан тип выводимых задач");
                return null;
        }
        return listOfAllTasks;
    }

    public void deleteAllTasks(int typeOfTask){ // 1 - tasks; 2 - epicTask; 3 - subTasks
        switch (typeOfTask) {
            case 1:
                taskList.clear();
                System.out.println("Список tasks очищен.");
                break;
            case 2:
                epicTaskList.clear();
                subTaskList.clear(); //при удалении эпиков также чистим все подзадачи
                System.out.println("Список epicTasks очищен.");
                break;
            case 3:
                subTaskList.clear();
                for (EpicTask epicTask : epicTaskList.values()){ //удаляем позадачи из эпиков
                    epicTask.getSubTasksOfEpic().clear(); //удаляем позадачи из эпиков
                    epicTask.setStatus("NEW"); // и обновляем статус на NEW
                }
                System.out.println("Список subTasks очищен.");
                break;
            default: //отлов ошибки на случай неверного запроса
                System.out.println("Ошибка. Неверно указан тип удаляемых задач");
        }
    }

    public Task getTaskById(int idToFind){
        Task taskToReturn;
        if (taskList.containsKey(idToFind)) {
                taskToReturn = taskList.get(idToFind);
                return taskToReturn;
        } else if (epicTaskList.containsKey(idToFind)) {
                taskToReturn = epicTaskList.get(idToFind);
                return taskToReturn;
        } else if (subTaskList.containsKey(idToFind)) {
                taskToReturn = subTaskList.get(idToFind);
                return taskToReturn;
        } else
            System.out.println("Задача с ID " + idToFind + " не найдена.");
            return null;
    }

    public void addNewTask(Task task) {
        if (task == null) {
            System.out.println("Ошибка. Переданный Task == null.");
            return;
        }
        if(task.getClass() == Task.class) {
            taskList.put(task.getId(), task);
        } else if(task.getClass() == EpicTask.class) {
            EpicTask taskToAdd = (EpicTask) task;
            epicTaskList.put(taskToAdd.getId(), taskToAdd);
        } else if(task.getClass() == SubTask.class) {
            SubTask taskToAdd = (SubTask) task;
            EpicTask epicTask = epicTaskList.get(taskToAdd.getIdOfEpic());
            HashMap<Integer, SubTask> subTasksOfEpic = epicTask.getSubTasksOfEpic();
            subTasksOfEpic.put(taskToAdd.getId(), taskToAdd);
            calcStatusOfEpic(epicTaskList.get(taskToAdd.getIdOfEpic()));
            subTaskList.put(taskToAdd.getId(), taskToAdd);
        }
    }

    public void updateTask(Task updatedTask) {
        if (taskList.containsKey(updatedTask.getId())) {
            taskList.put(updatedTask.getId(), updatedTask);
        } else if (epicTaskList.containsKey(updatedTask.getId())) {
            epicTaskList.put(updatedTask.getId(), (EpicTask) updatedTask);
        } else if (subTaskList.containsKey(updatedTask.getId())){
            SubTask updatedSub = (SubTask) updatedTask;
            subTaskList.put(updatedSub.getId(), updatedSub);
            EpicTask epicOfUpdatedSub = epicTaskList.get(updatedSub.getIdOfEpic()); //достаем нужный эпик
            HashMap <Integer, SubTask> subTasksOfEpic = epicOfUpdatedSub.getSubTasksOfEpic(); //достаем его список сабов
            subTasksOfEpic.put(updatedSub.getId(), updatedSub); //кладем в него обновленный саб
            calcStatusOfEpic(epicOfUpdatedSub);
        }
    }

    private void calcStatusOfEpic (EpicTask epicTask) {
        HashMap <Integer, SubTask> subTasksOfEpic = epicTask.getSubTasksOfEpic(); //достаем список сабов эпика
        int statusCountNew = 0; //счетчик для статусов NEW
        int statusCountDone = 0; //счетчик для статусов DONE

        for (SubTask subTask : subTasksOfEpic.values()) {
            if (subTask.getStatus().equals("NEW")) { //если статус равен NEW
                statusCountNew++; //увеличиваем счетчик NEW
            } else if (subTask.getStatus().equals("DONE")) { //если статус равен DONE
                statusCountDone++; //увеличиваем счетчик DONE
            }
        }
        if (statusCountNew == subTasksOfEpic.size()) { //если все статусы NEW
            epicTask.setStatus("NEW"); //статус эпика NEW
        } else if (statusCountDone == subTasksOfEpic.size()) { //если все статусы DONE
            epicTask.setStatus("DONE"); //статус эпика DONE
        } else { //в иных случаях статус эпика IN_PROGRESS
            epicTask.setStatus("IN_PROGRESS");
        }
    }

    public Task createUpdatedTask (String nameofFieldToUpdate, String newFieldValue, int idOfTaskToUpdate) {
        if (!(nameofFieldToUpdate.equals("name") //проверим, верно ли указано изменяемое поле
                || nameofFieldToUpdate.equals("description")
                || nameofFieldToUpdate.equals("status"))) {
            System.out.println("Ошибка. Имя изменяемого поля указано неверно.");
            return null;
        }

        if (taskList.containsKey(idOfTaskToUpdate)) { //ищем задачу, которую нужно обновить
            Task taskToUpdate = taskList.get(idOfTaskToUpdate); //достаем обновляемый таск
            Task updatedTask = createTask(taskToUpdate.getName(), taskToUpdate.getDescription()); //создаем новый таск
            updatedTask.setStatus(taskToUpdate.getStatus()); //даем ему статус обновляемой задачи
            updatedTask.setId(taskToUpdate.getId()); //присваиваем ему корректный ID
            switch (nameofFieldToUpdate) { //определяем, какое поле нужно изменить и меняем его
                case "name":
                    updatedTask.setName(newFieldValue);
                    return updatedTask;
                case "description":
                    updatedTask.setDescription(newFieldValue);
                    return updatedTask;
                case "status":
                    updatedTask.setStatus(newFieldValue);
                    return updatedTask;
            }
        } else if (epicTaskList.containsKey(idOfTaskToUpdate)) { //ищем задачу, которую нужно обновить
            EpicTask epicToUpdate = epicTaskList.get(idOfTaskToUpdate); // достаем обновляемый эпик
            EpicTask updatedEpic = createEpicTask(epicToUpdate.getName(), epicToUpdate.getDescription());
            updatedEpic.setId(epicToUpdate.getId());
            updatedEpic.setSubTasksOfEpic(epicToUpdate.getSubTasksOfEpic());
            switch (nameofFieldToUpdate) {
                case "name":
                    updatedEpic.setName(newFieldValue);
                    return updatedEpic;
                case "description":
                    updatedEpic.setDescription(newFieldValue);
                    return updatedEpic;
            }
        } else if (subTaskList.containsKey(idOfTaskToUpdate)) {
            SubTask subToUpdate = subTaskList.get(idOfTaskToUpdate);
            SubTask updatedSub = createSubTask(subToUpdate.getName(),
                                               subToUpdate.getDescription(),
                                               subToUpdate.getIdOfEpic());
            updatedSub.setId(subToUpdate.getId());
            switch (nameofFieldToUpdate){
                case "name":
                    updatedSub.setName(newFieldValue);
                    return updatedSub;
                case "description":
                    updatedSub.setDescription(newFieldValue);
                    return updatedSub;
                case "status":
                    updatedSub.setStatus(newFieldValue);
            }
            return updatedSub;
        } else
            System.out.println(idOfTaskToUpdate + " не найден ни в одном из списоков.");
            return null;
    }

    public void deleteById(int id){
        if (taskList.containsKey(id)) { //ищем id в списке задач
            taskList.remove(id); //если нашли - удаляем

        } else if (epicTaskList.containsKey(id)) { //ищем id в списке эпика
            HashMap <Integer, SubTask> subTaskOfEpic = epicTaskList.get(id).getSubTasksOfEpic();
            for (SubTask subTask : subTaskOfEpic.values()) { //удаляем все подзадачи, которые относятся к эпику
                subTaskList.remove(subTask.getId());
            }
            epicTaskList.remove(id); //если нашли - удаляем эпик

        } else if (subTaskList.containsKey(id)){ //ищем id в списке подзадач
            SubTask subTaskToRemove = subTaskList.get(id); //достаем подзадачу из списка подзадач
            EpicTask epicTask = epicTaskList.get(subTaskToRemove.getIdOfEpic()); //находим эпик подзадачи
            HashMap<Integer, SubTask> subTasksOfEpic = epicTask.getSubTasksOfEpic(); //достаем список подзадач эпика
            for (SubTask subTask : subTasksOfEpic.values()){ //ищем подзадачу, которую будем удалять
                if (subTask.getId() == id){
                    subTasksOfEpic.remove(id); //удаляем найденую подзадачу
                }
            }
            subTaskList.remove(id); //удаляем подзадачу из списка подзадач

        } else {
            System.out.println("ID " + id + " не найден ни в одном из списков задач");
        }
    }

    public HashMap<Integer, SubTask> getSubTasksOfEpic(int idOfEpic) {
        if (epicTaskList.containsKey(idOfEpic)){
            return epicTaskList.get(id).getSubTasksOfEpic();
        } else {
            System.out.println("Не удалось найти эпик");
            return null;
        }
    }

    public SubTask createSubTask(String name, String description, int idOfEpic) {
        //subTask можно создать только в том случае, если есть соответствующий epicTask
        if (epicTaskList.containsKey(idOfEpic)){
            SubTask subTask = new SubTask(name, description, idOfEpic, id);
            id++;
            return subTask;
        } else {
            System.out.println("Указанного EpicTask не существует");
            return null;
        }
    }

    public Task createTask(String name, String description) {
        Task task  = new Task(name,description, id);
        id++;
        return task;
    }

    public EpicTask createEpicTask (String name, String description){
        EpicTask epicTask = new EpicTask(name, description, id);
        id++;
        return epicTask;
    }
}
