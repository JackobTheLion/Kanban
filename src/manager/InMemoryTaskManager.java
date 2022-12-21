package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    private final static HashMap<Integer, Task> tasks = new HashMap<>();
    private final static HashMap<Integer, Epic> epicTasks = new HashMap<>();
    private final static HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private static int id = 0;

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.println("ID " + id + " не найден в списках задач");
            return null;
        } else {
            inMemoryHistoryManager.addToHistory(task);
            return task;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicTasks.get(id);
        if (epic == null) {
            System.out.println("ID " + id + " не найден в списке эпиков");
            return null;
        } else {
            inMemoryHistoryManager.addToHistory(epic);
            return epic;
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            System.out.println("ID " + id + " не найден в списке сабтасков");
            return null;
        } else {
            inMemoryHistoryManager.addToHistory(subTask);
            return subTask;
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            System.out.println("ID " + id + " не найден в списках задач");
            return;
        }
        tasks.remove(id);
    }

    @Override
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

    @Override
    public void deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            System.out.println("ID " + id + " не найден в сабтасках");
            return;
        }
        SubTask subTask = subTasks.get(id);
        Epic epic = epicTasks.get(subTask.getEpicId());
        epic.deleteSubTaskId(id);
        calcStatusOfEpic(epic.getId());
        subTasks.remove(id);
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epicTasks.clear();
        subTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (Epic epic : epicTasks.values()) {
            epic.getSubTasksOfEpic().clear();
            epic.setStatus(Status.NEW); //по условию при отсутствии подзадач статус NEW
        }
        subTasks.clear();
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
        epicTasks.clear();
        subTasks.clear();
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        } else {
            System.out.println("Невозможно обновить. ID " + updatedTask.getId() + " не найден в задачах");
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (epicTasks.containsKey(updatedEpic.getId())) {
            Epic savedEpic = epicTasks.get(updatedEpic.getId());
            savedEpic.setName(updatedEpic.getName());
            savedEpic.setDescription(updatedEpic.getDescription());
        } else {
            System.out.println("Невозможно обновить. ID " + updatedEpic.getId() + " не найден в эпиках");
        }
    }

    @Override
    public void updateSubtask(SubTask updatedSubTask) {
        if (!epicTasks.containsKey(updatedSubTask.getEpicId())) {
            System.out.println("Не существует эпика, к которому относится сабтаск");
            return;
        }
        if (subTasks.containsKey(updatedSubTask.getId())) {
            subTasks.put(updatedSubTask.getId(), updatedSubTask);
            Epic epicOfUpdatedSub = epicTasks.get(updatedSubTask.getEpicId()); //достаем нужный эпик
            epicOfUpdatedSub.addSubTaskId(updatedSubTask.getId()); //кладем в него обновленный саб
            calcStatusOfEpic(epicOfUpdatedSub.getId());
        } else {
            System.out.println("Невозможно обновить. ID " + updatedSubTask.getId() + " не найден в сабтасках");
        }
    }

    @Override
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

    @Override
    public void createTask(Task task) {
        if (task == null) {
            System.out.println("Ошибка. task == nul");
            return;
        }
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic == null) {
            System.out.println("Ошибка. epic == null");
            return;
        }
        id++;
        epic.setId(id);
        epicTasks.put(id, epic);
    }

    @Override
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
            calcStatusOfEpic(subTask.getEpicId());
        } else {
            System.out.println("Указанного EpicTask не существует");
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    private void calcStatusOfEpic(int id) {
        Epic epic = epicTasks.get(id);
        ArrayList<Integer> subTasksOfEpic = epic.getSubTasksOfEpic(); //достаем список сабов эпика
        int statusCountNew = 0; //счетчик для статусов NEW
        int statusCountDone = 0; //счетчик для статусов DONE

        for (int i = 0; i < subTasksOfEpic.size(); i++) {
            SubTask subTask = subTasks.get(subTasksOfEpic.get(i));
            if (subTask.getStatus().equals(Status.NEW)) { //если статус равен NEW
                statusCountNew++; //увеличиваем счетчик NEW
            } else if (subTask.getStatus().equals(Status.DONE)) { //если статус равен DONE
                statusCountDone++; //увеличиваем счетчик DONE
            }
        }

        if (statusCountNew == subTasksOfEpic.size()) { //если все статусы NEW
            epic.setStatus(Status.NEW); //статус эпика NEW
        } else if (statusCountDone == subTasksOfEpic.size()) { //если все статусы DONE
            epic.setStatus(Status.DONE); //статус эпика DONE
        } else { //в иных случаях статус эпика IN_PROGRESS
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
