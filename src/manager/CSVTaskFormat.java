package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {

    public static Task taskFromString(String value) {
        final String[] taskFields = value.split(","); //id,type,name,status,description,epic
        final int id = Integer.parseInt(taskFields[0]);
        final TaskType type = TaskType.valueOf(taskFields[1]);
        final String name = taskFields[2];
        final Status status = Status.valueOf(taskFields[3]);
        final String description = taskFields[4];

        if (type == TaskType.TASK) {
            Task task = new Task(name, description, status);
            task.setId(id);
            return task;
        }
        if (type == TaskType.EPIC) {
            Epic epic = new Epic(name, description, status);
            epic.setId(id);
            return epic;
        } //if type == TaskType.SubTask
        final int epicId = Integer.parseInt(taskFields[5]);
        SubTask subTask = new SubTask(name, description, epicId, status);
        subTask.setId(id);
        return subTask;
    }

    public static String historyToString(HistoryManager historyManager) {
        final List<Task> history = historyManager.getHistory(); //достаем историю
        if (history.isEmpty()) { //если история пуста
            return ""; //возвращаем ""
        }
        StringBuilder sb = new StringBuilder();
        sb.append(history.get(0).getId()); //"прикрепляем" ID первого элемента (с индексом 0)
        for (int i = 1; i < history.size(); i++) {
            Task task = history.get(i); //в цикле достаем элементы по индексу начиная с 1
            sb.append(","); //добавляем запятую
            sb.append(task.getId()); //и следующее значение
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] values = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String element : values) {
            history.add(Integer.parseInt(element));
        }
        return history;
    }

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String taskToString(Task task) { //id,type,name,status,description,epic
        return task.getId() + ","
                + task.getType() + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getDescription() + ","
                + (task.getType().equals(TaskType.SUBTASK) ? ((SubTask) task).getEpicId() : "");
        //getEpicId() есть только у сабтаска, поэтому явное приведение
    }
}
