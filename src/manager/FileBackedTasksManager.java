package manager;

import manager.exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager  extends InMemoryTaskManager {

    public static void main(String[] args) {

        File file = new File("src/manager/backup/backup.csv");

        FileBackedTasksManager fileBackedTasksManager1 = new FileBackedTasksManager(file);
        System.out.println("Проверим историю. Должно быть пусто");
        System.out.println(fileBackedTasksManager1.getHistory());
        System.out.println("____________");

        System.out.println("Создадим задачи...");
        fileBackedTasksManager1.createTask(new Task("задача 1", "описание 1", Status.NEW));
        fileBackedTasksManager1.createTask(new Task("задача 2", "описание 2", Status.NEW));
        fileBackedTasksManager1.createEpic(new Epic("эпик 1", "описание эпика 1", Status.NEW));
        fileBackedTasksManager1.createSubTask(new SubTask("Сабтаск 1",
                "описание сабтаска 1", 3, Status.NEW));
        fileBackedTasksManager1.createSubTask(new SubTask("Сабтаск 2",
                "описание сабтаска 2", 3, Status.NEW));
        fileBackedTasksManager1.createSubTask(new SubTask("Сабтаск 3",
                "описание сабтаска 3", 3, Status.NEW));
        fileBackedTasksManager1.createEpic(new Epic("эпик 2", "описание эпика 1", Status.NEW));


        System.out.println("Вызовем задачи и посмотрим историю");
        fileBackedTasksManager1.getTaskById(1);
        fileBackedTasksManager1.getTaskById(2);
        fileBackedTasksManager1.getEpicById(3);
        fileBackedTasksManager1.getSubTaskById(4);
        fileBackedTasksManager1.getSubTaskById(5);
        fileBackedTasksManager1.getSubTaskById(6);
        fileBackedTasksManager1.getEpicById(7);

        System.out.println(fileBackedTasksManager1.getHistory());
        System.out.println("____________");

        System.out.println("Вызовем задачи в другом порядке и посмотрим историю");
        fileBackedTasksManager1.getTaskById(2);
        fileBackedTasksManager1.getSubTaskById(5);
        fileBackedTasksManager1.getSubTaskById(6);
        fileBackedTasksManager1.getSubTaskById(4);
        fileBackedTasksManager1.getEpicById(7);
        fileBackedTasksManager1.getEpicById(3);
        fileBackedTasksManager1.getTaskById(1);
        System.out.println(fileBackedTasksManager1.getHistory());

        System.out.println("____________");
        System.out.println("Создадим fileBackedTasksManager2 из файла бекапа.");
        FileBackedTasksManager fileBackedTasksManager2 = FileBackedTasksManager.loadFromFile(file);

        System.out.println("Проверим в fileBackedTasksManager2.");
        System.out.println(fileBackedTasksManager2.getHistory());
        System.out.println("\nИстории одинаковые?");
        //System.out.println(fileBackedTasksManager2.getHistory().equals(fileBackedTasksManager1.getHistory()));
        System.out.println("____________");
    }

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        save();
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        super.updateEpic(updatedEpic);
        save();
    }

    @Override
    public void updateSubtask(SubTask updatedSubTask) {
        super.updateSubtask(updatedSubTask);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    private void save() {
        StringBuilder saved = new StringBuilder();
        for (Task task : tasks.values()) {
            saved.append(taskToString(task));
        }
        for (Epic epic : epicTasks.values()) {
            saved.append(taskToString(epic));
        }
        for (SubTask subTask : subTasks.values()) {
            saved.append(taskToString(subTask));
        }

        saved.append("\n");

        saved.append(historyToString(inMemoryHistoryManager));

        try (OutputStreamWriter writer = new OutputStreamWriter
                (new FileOutputStream(file), StandardCharsets.UTF_8)) {
        writer.write(saved.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String taskToString(Task task) { //id,type,name,status,description,epic
        int id = task.getId();
        TaskType taskType = TaskType.TASK;
        String name = task.getName();
        Status status = task.getStatus();
        String description = task.getDescription();

        return String.format("%s,%s,%s,%s,%s\n", id, taskType, name, status, description);
    }

    private String taskToString(Epic task) { //id,type,name,status,description
        int id = task.getId();
        TaskType taskType = TaskType.EPIC;
        String name = task.getName();
        Status status = task.getStatus();
        String description = task.getDescription();

        return String.format("%s,%s,%s,%s,%s\n", id, taskType, name, status, description);
    }

    private String taskToString(SubTask task) { //id,type,name,status,description,epic
        int id = task.getId();
        TaskType taskType = TaskType.SUBTASK;
        String name = task.getName();
        Status status = task.getStatus();
        String description = task.getDescription();
        String epic = Integer.toString(task.getEpicId());

        return String.format("%s,%s,%s,%s,%s,%s\n", id, taskType, name, status, description, epic);
    }

    private static Task taskFromString(String value) {
        String[] taskFields = value.split(","); //id,type,name,status,description,epic
        Task taskFromString = null;
        int id = Integer.parseInt(taskFields[0]);
        String name = taskFields[2];
        Status status = null;
        String description = taskFields[4];
        int idOfEpic;

        switch (taskFields[3]) { //определение статуса
            case "NEW":
                status = Status.NEW;
                break;
            case "IN_PROGRESS":
                status = Status.IN_PROGRESS;
                break;
            case "DONE":
                status = Status.DONE;
                break;
        }

        switch (taskFields[1]) { //определение типа задачи
            case "TASK":
                taskFromString = new Task(name, description, status);
                break;
            case "EPIC":
                taskFromString = new Epic(name, description, status);
                break;
            case "SUBTASK":
                idOfEpic = Integer.parseInt(taskFields[5]);
                taskFromString = new SubTask(name, description, idOfEpic, status);
                break;
        }
        taskFromString.setId(id);

        return taskFromString;
    }

    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            String backup = Files.readString(Path.of(file.getPath()));

            if (backup.isEmpty()) {
                throw new ManagerSaveException("Файл бекапа пуст.");
            }

            String[] lines = backup.split("\n");
            for (int i = 0; i < lines.length - 1; i++) { //последняя строка - история
                if (!lines[i].isEmpty()) {
                    Task task = taskFromString(lines[i]);

                    if (task.getClass() == Task.class) {
                        fileBackedTasksManager.tasks.put(task.getId(), task);
                    } else if (task.getClass() == Epic.class) {
                        fileBackedTasksManager.epicTasks.put(task.getId(), ((Epic) task));
                    } else if (task.getClass() == SubTask.class) {
                        fileBackedTasksManager.subTasks.put(task.getId(), ((SubTask) task));
                        fileBackedTasksManager.epicTasks.get(((SubTask) task).getEpicId()).addSubTaskId(task.getId());
                    }
                }
            }
            List<Integer> history = historyFromString(lines[lines.length - 1]); //последняя строка - история
            for (Integer id : history) {
                Task task;
                if (fileBackedTasksManager.tasks.containsKey(id)) {
                    task = fileBackedTasksManager.tasks.get(id);
                } else if (fileBackedTasksManager.epicTasks.containsKey(id)) {
                    task = fileBackedTasksManager.epicTasks.get(id);
                } else task = fileBackedTasksManager.subTasks.get(id);

                fileBackedTasksManager.inMemoryHistoryManager.add(task);
                }
        } catch (ManagerSaveException e) {
            e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTasksManager;
    }

    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder historyToString = new StringBuilder();
        for (Task task : history) {
            historyToString.append(task.getId()).append(",");
        }
        return historyToString.toString();
    }

    static List<Integer> historyFromString(String value) {
        String[] values = value.split(",");
        List<Integer> history = new ArrayList<>();
        for (String element : values) {
            history.add(Integer.parseInt(element));
        }
        return history;
    }

    private enum TaskType {
        TASK,
        EPIC,
        SUBTASK
    }
}
