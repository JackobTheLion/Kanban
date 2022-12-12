package Manager;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int id = 0;

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for (Task task: tasks.values()) {
            result.add(task);
        }
        return result;
    }

    public ArrayList<Epic> getAllEpicTasks() {
        ArrayList<Epic> result = new ArrayList<>();
        for (Epic epic : epicTasks.values()) {
            result.add(epic);
        }
        return result;
    }

    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> result = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            result.add(subTask);
        }
        return result;
    }

    public Task getTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("ID " + id + " не найден в списках задач");
            return null;
        } else {
            return tasks.get(id);
        }
    }

    public Epic getEpicById(int id) {
        if (!epicTasks.containsKey(id)) {
            System.out.println("ID " + id + " не найден в списке эпиков");
            return null;
        } else {
            return epicTasks.get(id);
        }
    }

    public SubTask getSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("ID " + id + " не найден в списке сабтасков");
            return null;
        } else {
            return subTasks.get(id);
        }
    }

    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("ID " + id + " не найден в списках задач");
            return;
        }
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        if (!epicTasks.containsKey(id)) {
            System.out.println("ID " + id + " не найден в эпиках");
            return;
        }
        Epic epic = epicTasks.get(id);
        ArrayList<Integer> subTaskOfEpic = epic.getSubTasksOfEpic();
        for (Integer integer : subTaskOfEpic) {
            subTasks.remove(integer);
        }
        epicTasks.remove(id);
    }

    public void deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("ID " + id + " не найден в сабтасках");
            return;
        }
        SubTask subTask = subTasks.get(id);
        Epic epic = epicTasks.get(subTask.getEpicId());
        epic.getSubTasksOfEpic().remove(Integer.valueOf(id));
        calcStatusOfEpic(epic.getId());
        subTasks.remove(id);
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epicTasks.clear();
        subTasks.clear();
    }

    public void clearSubTasks() {
        for (Epic epic : epicTasks.values()) {
            epic.getSubTasksOfEpic().clear();
            epic.setStatus("NEW"); //по условию при отсутствии подзадач статус NEW
        }
        subTasks.clear();
    }

    public void clearAllTasks() {
        tasks.clear();
        epicTasks.clear();
        subTasks.clear();
    }

    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        } else {
            System.out.println("Невозможно обновить. ID " + updatedTask.getId() + " не найден в задачах");
        }
    }

    public void updateEpic(Epic updatedEpic) {
        if (epicTasks.containsKey(updatedEpic.getId())) {
            epicTasks.put(updatedEpic.getId(), (Epic) updatedEpic);
        } else {
            System.out.println("Невозможно обновить. ID " + updatedEpic.getId() + " не найден в эпиках");
        }
    }

    public void updateSubtask(SubTask updatedSubTask) {
        if (subTasks.containsKey(updatedSubTask.getId())) {
            subTasks.put(updatedSubTask.getId(), updatedSubTask);
            Epic epicOfUpdatedSub = epicTasks.get(updatedSubTask.getEpicId()); //достаем нужный эпик
            epicOfUpdatedSub.getSubTasksOfEpic().add(updatedSubTask.getId()); //кладем в него обновленный саб
            calcStatusOfEpic(epicOfUpdatedSub.getId());
        } else {
            System.out.println("Невозможно обновить. ID " + updatedSubTask.getId() + " не найден в сабтасках");
        }
    }


    public Task createUpdatedTask(String fieldToUpdate, String newFieldValue, int idOfTaskToUpdate) {
        if (tasks.containsKey(idOfTaskToUpdate)) {
                Task taskToUpdate = tasks.get(idOfTaskToUpdate); //достаем обновляемый таск
                Task updatedTask = new Task(taskToUpdate.getName(), taskToUpdate.getDescription()); //создаем новый таск
                updatedTask.setStatus(taskToUpdate.getStatus()); //даем ему статус обновляемой задачи
                updatedTask.setId(taskToUpdate.getId()); //присваиваем ему корректный ID
                switch (fieldToUpdate) { //определяем, какое поле нужно изменить и меняем его
                    case "name":
                        updatedTask.setName(newFieldValue);
                        break;
                    case "description":
                        updatedTask.setDescription(newFieldValue);
                        break;
                    case "status":
                        updatedTask.setStatus(newFieldValue);
                        break;
                    default:
                        System.out.println("Ошибка. Имя изменяемого поля указано неверно.");
                        return null;
                }
                return updatedTask;
        } else {
            System.out.println("Задача с ID " + idOfTaskToUpdate + " не найдена");
            return null;
        }
    }

    public Epic createUpdatedEpic(String fieldToUpdate, String newFieldValue, int idOfTaskToUpdate) {
        if (epicTasks.containsKey(idOfTaskToUpdate)) {
            Epic epicToUpdate = epicTasks.get(idOfTaskToUpdate); // достаем обновляемый эпик
            Epic updatedEpic = new Epic(epicToUpdate.getName(), epicToUpdate.getDescription());
            updatedEpic.setId(epicToUpdate.getId());
            updatedEpic.setSubTasksOfEpic(epicToUpdate.getSubTasksOfEpic());
            switch (fieldToUpdate) {
                case "name":
                    updatedEpic.setName(newFieldValue);
                    break;
                case "description":
                    updatedEpic.setDescription(newFieldValue);
                    break;
                default:
                    System.out.println("Ошибка. Имя изменяемого поля указано неверно.");
                    return null;
            }
            return updatedEpic;
        } else {
            System.out.println("Эпик с ID " + idOfTaskToUpdate + " не найден");
            return null;
        }
    }

    public SubTask createUpdatedSubTask (String fieldToUpdate, String newFieldValue, int idOfTaskToUpdate) {
        if (subTasks.containsKey(idOfTaskToUpdate)) {
            SubTask subToUpdate = subTasks.get(idOfTaskToUpdate);
            SubTask updatedSub = new SubTask(subToUpdate.getName(),subToUpdate.getDescription(),
                    subToUpdate.getEpicId());
            updatedSub.setId(subToUpdate.getId());
            switch (fieldToUpdate){
                case "name":
                    updatedSub.setName(newFieldValue);
                    break;
                case "description":
                    updatedSub.setDescription(newFieldValue);
                    break;
                case "status":
                    updatedSub.setStatus(newFieldValue);
                    break;
                default:
                    System.out.println("Ошибка. Имя изменяемого поля указано неверно.");
                    return null;
            }
            return updatedSub;
        } else {
            System.out.println(idOfTaskToUpdate + " не найден ни в списке subtask.");
            return null;
        }
    }

    public ArrayList<SubTask> getSubTasksByEpicId(int id) {
        if (epicTasks.containsKey(id)) {
            ArrayList<SubTask> subTasksByEpicId = new ArrayList<>();
            ArrayList<Integer> subTasksOfEpic = epicTasks.get(id).getSubTasksOfEpic();
            for (Integer integer : subTasksOfEpic) {
                SubTask subToAdd = subTasks.get(integer);
                subTasksByEpicId.add(subToAdd);
            }
            return subTasksByEpicId;
        } else {
            System.out.println("Не удалось найти эпик");
            return null;
        }
    }

    public void createTask(Task task) {
        if (task == null) {
            System.out.println("Ошибка. task == nul");
            return;
        }
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    public void createEpic(Epic epic) {
        if (epic == null) {
            System.out.println("Ошибка. epic == null");
            return;
        }
        id++;
        epic.setId(id);
        epicTasks.put(id, epic);
    }

    public void createSubTask(SubTask subTask) {
        if (subTask == null) {
            System.out.println("Ошибка. subTask == null");
            return;
        }
        //subTask можно создать только в том случае, если есть соответствующий epicTask
        if (epicTasks.containsKey(subTask.getEpicId())) {
            id++;
            subTask.setId(id);
            subTasks.put(id, subTask);
            epicTasks.get(subTask.getEpicId()).getSubTasksOfEpic().add(id); //добавляем саб таск в список эпика
        } else {
            System.out.println("Указанного EpicTask не существует");
        }
    }

    private void calcStatusOfEpic(int id) {
        Epic epic = epicTasks.get(id);
        ArrayList<Integer> subTasksOfEpic = epic.getSubTasksOfEpic(); //достаем список сабов эпика
        int statusCountNew = 0; //счетчик для статусов NEW
        int statusCountDone = 0; //счетчик для статусов DONE

        for (int i = 0; i < subTasksOfEpic.size(); i++) {
            SubTask subTask = subTasks.get(subTasksOfEpic.get(i));
            if (subTask.getStatus().equals("NEW")) { //если статус равен NEW
                statusCountNew++; //увеличиваем счетчик NEW
            } else if (subTask.getStatus().equals("DONE")) { //если статус равен DONE
                statusCountDone++; //увеличиваем счетчик DONE
            }
        }

        if (statusCountNew == subTasksOfEpic.size()) { //если все статусы NEW
            epic.setStatus("NEW"); //статус эпика NEW
        } else if (statusCountDone == subTasksOfEpic.size()) { //если все статусы DONE
            epic.setStatus("DONE"); //статус эпика DONE
        } else { //в иных случаях статус эпика IN_PROGRESS
            epic.setStatus("IN_PROGRESS");
        }
    }


}
