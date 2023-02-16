package manager;

import manager.exceptions.ScheduleBookingException;
import tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected final ScheduleManager scheduleManager = Managers.getDefaultScheduleManager();
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epicTasks = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
        if(task1.getStartTime() == null && task2.getStartTime() == null) {
            return task1.getId() - task2.getId();
        } else if (task1.getStartTime() != null && task2.getStartTime() == null) {
            return 1;
        } else if (task1.getStartTime() == null && task2.getStartTime() != null) {
            return -1;
        } else return task1.getStartTime().compareTo(task2.getStartTime());
    });
    protected int generatorId = 0;

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
            //System.out.println("ID " + id + " не найден в списках задач");
            return null;
        } else {
            inMemoryHistoryManager.add(task);
            return task;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicTasks.get(id);
        if (epic == null) {
            //System.out.println("ID " + id + " не найден в списке эпиков");
            return null;
        } else {
            inMemoryHistoryManager.add(epic);
            return epic;
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            //System.out.println("ID " + id + " не найден в списке сабтасков");
            return null;
        } else {
            inMemoryHistoryManager.add(subTask);
            return subTask;
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasks.containsKey(id)) {
            //System.out.println("ID " + id + " не найден в списках задач");
            return;
        }
        scheduleManager.unBookSchedule(tasks.get(id));
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (!epicTasks.containsKey(id)) {
            //System.out.println("ID " + id + " не найден в эпиках");
            return;
        }
        Epic epic = epicTasks.get(id);
        ArrayList<Integer> subTaskOfEpic = epic.getSubTasksOfEpic();
        for (Integer integer : subTaskOfEpic) {
            prioritizedTasks.remove(subTasks.get(integer));
            subTasks.remove(integer);
            inMemoryHistoryManager.remove(integer);
        }
        epicTasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (!subTasks.containsKey(id)) {
            //System.out.println("ID " + id + " не найден в сабтасках");
            return;
        }
        SubTask subTask = subTasks.get(id);
        Epic epic = epicTasks.get(subTask.getEpicId());
        epic.deleteSubTaskId(id);
        calcStatusOfEpic(epic.getId());
        calcDurationOfEpic(epic.getId());
        calcStartTimeOfEpic(epic.getId());

        scheduleManager.unBookSchedule(subTasks.get(id));
        prioritizedTasks.remove(subTask);
        subTasks.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            scheduleManager.unBookSchedule(task);
            prioritizedTasks.remove(task);
            inMemoryHistoryManager.remove(task.getId());
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epicTasks.values()) {
            inMemoryHistoryManager.remove(epic.getId());
        }
        epicTasks.clear();
        for (SubTask subTask : subTasks.values()) {
            scheduleManager.unBookSchedule(subTask);
            prioritizedTasks.remove(subTask);
            inMemoryHistoryManager.remove(subTask.getId());
        }
        subTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (Epic epic : epicTasks.values()) {
            epic.getSubTasksOfEpic().clear();
            epic.setStatus(Status.NEW); //по условию при отсутствии подзадач статус NEW
        }
        for (SubTask subTask : subTasks.values()) {
            scheduleManager.unBookSchedule(subTask);
            prioritizedTasks.remove(subTask);
            inMemoryHistoryManager.remove(subTask.getId());
        }
        subTasks.clear();
    }

    @Override
    public void clearAllTasks() {
        prioritizedTasks.clear();
        scheduleManager.unbookAll();
        inMemoryHistoryManager.clearHistory();
        tasks.clear();
        epicTasks.clear();
        subTasks.clear();
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (updatedTask == null) {
            return;
        }

        if (tasks.containsKey(updatedTask.getId())) {
            Task oldTask = tasks.get(updatedTask.getId());
            tasks.put(updatedTask.getId(), updatedTask);
            for (Task task : prioritizedTasks) {
                if (task.getId() == updatedTask.getId()){
                    prioritizedTasks.remove(task);
                    break;
                }
            }
            scheduleManager.updateBooking(oldTask, updatedTask);
            prioritizedTasks.add(updatedTask);
        } else {
            System.out.println("Невозможно обновить. ID " + updatedTask.getId() + " не найден в задачах");
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (updatedEpic == null) {
            return;
        }

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
        if (updatedSubTask == null) {
            return;
        }

        if (!epicTasks.containsKey(updatedSubTask.getEpicId())) {
            System.out.println("Не существует эпика, к которому относится сабтаск");
            return;
        }
        if (subTasks.containsKey(updatedSubTask.getId())) {
            SubTask oldSubTask = subTasks.get(updatedSubTask.getId());
            subTasks.put(updatedSubTask.getId(), updatedSubTask);
            Epic epicOfUpdatedSub = epicTasks.get(updatedSubTask.getEpicId()); //достаем нужный эпик
            epicOfUpdatedSub.addSubTaskId(updatedSubTask.getId()); //кладем в него обновленный саб
            prioritizedTasks.remove(oldSubTask);
            scheduleManager.updateBooking(oldSubTask, updatedSubTask);
            prioritizedTasks.add(updatedSubTask);
            calcStatusOfEpic(epicOfUpdatedSub.getId());
            calcDurationOfEpic(epicOfUpdatedSub.getId());
            calcStartTimeOfEpic(epicOfUpdatedSub.getId());

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
            //System.out.println("Не удалось найти эпик");
            return null;
        }
    }

    @Override
    public int createTask(Task task) {
        if (task == null) {
            //System.out.println("Ошибка. task == null");
            return -1;
        }

        try {
            scheduleManager.bookSchedule(task);
            generatorId++;
            task.setId(generatorId);
            tasks.put(generatorId, task);
            prioritizedTasks.add(task);
            return generatorId;
        } catch (ScheduleBookingException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    @Override
    public int createEpic(Epic epic) {
        if (epic == null) {
            //System.out.println("Ошибка. epic == null");
            return -1;
        }
        generatorId++;
        epic.setId(generatorId);
        epicTasks.put(generatorId, epic);
        return generatorId;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (subTask == null) {
            //System.out.println("Ошибка. subTask == null");
            return -1;
        }
        //subTask можно создать только в том случае, если есть соответствующий epicTask
        try {
            if (epicTasks.containsKey(subTask.getEpicId())) {
                scheduleManager.bookSchedule(subTask);
                generatorId++;
                subTask.setId(generatorId);
                subTasks.put(generatorId, subTask);
                epicTasks.get(subTask.getEpicId()).getSubTasksOfEpic().add(generatorId); //добавляем саб таск в список эпика
                calcStatusOfEpic(subTask.getEpicId());
                calcDurationOfEpic(subTask.getEpicId());
                calcStartTimeOfEpic(subTask.getEpicId());
                prioritizedTasks.add(subTask);
            } else {
                //System.out.println("Указанного EpicTask не существует");
                return -1;
            }
            return generatorId;
        } catch (ScheduleBookingException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<Task>(prioritizedTasks);
    }

    protected void calcStatusOfEpic(int id) {
        Epic epic = epicTasks.get(id);
        ArrayList<Integer> subTasksOfEpic = epic.getSubTasksOfEpic(); //достаем список сабов эпика
        int statusCountNew = 0; //счетчик для статусов NEW
        int statusCountDone = 0; //счетчик для статусов DONE

        for (Integer integer : subTasksOfEpic) {
            SubTask subTask = subTasks.get(integer);
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

    protected void calcDurationOfEpic(int id) {
        Epic epic = epicTasks.get(id);
        ArrayList<Integer> subTasksOfEpic = epic.getSubTasksOfEpic(); //достаем список сабов эпика
        int duration = 0;

        for (Integer subTaskId : subTasksOfEpic) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask.getDuration() != null) {
                duration += subTask.getDuration().toMinutes();
            }
        }
        epic.setDuration(duration);
    }

    protected void calcStartTimeOfEpic(int id) {
        Epic epic = epicTasks.get(id);
        ArrayList<Integer> subTasksOfEpic = epic.getSubTasksOfEpic();
        LocalDateTime startTime = epic.getStartTime();

        for (Integer integer : subTasksOfEpic) {
            SubTask subTask = subTasks.get(integer);
            if (startTime == null) {
                startTime = subTask.getStartTime();
            } else if (subTask.getStartTime() != null && subTask.getStartTime().isBefore(startTime)) {
                    startTime = subTask.getStartTime();
            }
        }
        epic.setStartTime(startTime);
    }
}