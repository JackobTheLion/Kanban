package manager;

import manager.exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static manager.CSVTaskFormat.*;


public class FileBackedTasksManager extends InMemoryTaskManager {

    public static void main(String[] args) {



        TaskManager FileBackedTasksManager = Managers.getDefault();
        System.out.println("Проверим историю. Должно быть пусто");
        System.out.println(FileBackedTasksManager.getHistory());
        System.out.println("____________");

        System.out.println("Создадим задачи...");
        int idTask1 = FileBackedTasksManager.createTask(new Task("задача 1",
                "описание 1", Status.NEW));
        int idTask2 = FileBackedTasksManager.createTask(new Task("задача 2",
                "описание 2", Status.NEW));
        int idEpic1 = FileBackedTasksManager.createEpic(new Epic("эпик 1",
                "описание эпика 1", Status.NEW));
        int subTask1 = FileBackedTasksManager.createSubTask(new SubTask("Сабтаск 1",
                "описание сабтаска 1", idEpic1, Status.NEW));
        int subTask2 = FileBackedTasksManager.createSubTask(new SubTask("Сабтаск 2",
                "описание сабтаска 2", idEpic1, Status.NEW));
        int subTask3 = FileBackedTasksManager.createSubTask(new SubTask("Сабтаск 3",
                "описание сабтаска 3", idEpic1, Status.NEW));
        int idEpic2 = FileBackedTasksManager.createEpic(new Epic("эпик 2",
                "описание эпика 1", Status.NEW));
        int subTask4 = FileBackedTasksManager.createSubTask(new SubTask("Сабтаск 3",
                "описание сабтаска 3", idEpic2, Status.NEW));



        System.out.println("Вызовем задачи и посмотрим историю");
        FileBackedTasksManager.getTaskById(idTask1);
        FileBackedTasksManager.getTaskById(idTask2);
        FileBackedTasksManager.getEpicById(idEpic1);
        FileBackedTasksManager.getSubTaskById(subTask1);
        FileBackedTasksManager.getSubTaskById(subTask2);
        FileBackedTasksManager.getSubTaskById(subTask3);
        FileBackedTasksManager.getEpicById(idEpic2);
        FileBackedTasksManager.getEpicById(subTask4);

        System.out.println(FileBackedTasksManager.getHistory());
        System.out.println("____________");

        System.out.println("Вызовем задачи в другом порядке и удалим 1 эпик посмотрим историю");
        FileBackedTasksManager.getTaskById(idTask2);
        FileBackedTasksManager.getSubTaskById(subTask2);
        FileBackedTasksManager.getSubTaskById(subTask3);
        FileBackedTasksManager.getSubTaskById(subTask1);
        FileBackedTasksManager.getEpicById(idEpic2);
        FileBackedTasksManager.getEpicById(subTask4);
        FileBackedTasksManager.getEpicById(idEpic1);
        FileBackedTasksManager.getTaskById(idTask1);
        FileBackedTasksManager.deleteEpicById(idEpic2);
        System.out.println(FileBackedTasksManager.getHistory());

        System.out.println("____________");
        System.out.println("Создадим fileBackedTasksManager из файла бекапа.");
        File file = new File("resources/backup.csv");
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(file);

        System.out.println("Проверим историю в fileBackedTasksManager.")   ;
        System.out.println(fileBackedTasksManager.getHistory());
        System.out.println("\nИстории одинаковые?");
        System.out.println(fileBackedTasksManager.getHistory().equals(FileBackedTasksManager.getHistory()));
        System.out.println("____________");

        System.out.println("Проверим в tasks.")   ;
        System.out.println("tasks одинаковые?");
        System.out.println(fileBackedTasksManager.getAllTasks().equals(FileBackedTasksManager.getAllTasks()));
        System.out.println("____________");

        System.out.println("Проверим в epics.")   ;
        System.out.println("epics одинаковые?");
        System.out.println(fileBackedTasksManager.getAllEpicTasks().equals(FileBackedTasksManager.getAllEpicTasks()));
        System.out.println("____________");

        System.out.println("Проверим в subTasks.")   ;
        System.out.println("subTasks одинаковые?");
        System.out.println(fileBackedTasksManager.getAllSubTasks().equals(FileBackedTasksManager.getAllSubTasks()));
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
    public int createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask.getId();
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager(file); //создаем менеджер из файла
        try { //пробуем
            final String csv = Files.readString(file.toPath()); //считываем стригу из файла
            final String[] lines = csv.split(System.lineSeparator()); //разделяем ее по разделителю строк
            //On UNIX systems, it returns "\n"; on Microsoft Windows systems it returns "\r\n".

            int generatorId = 0; //переменная для отслеживания последнего ID
            List<Integer> history = Collections.emptyList(); //создаем пустой список для истории
            for (int i = 1; i < lines.length; i++) { //пропускаем хедер, начинаем со 2 строки
                String line = lines[i];
                if (line.isEmpty()) { //если строка пустая, значит за ней идет строка с историей
                    history = CSVTaskFormat.historyFromString(lines[i + 1]); //восстанавливаем историю
                    break;
                }
                final Task task = CSVTaskFormat.taskFromString(line); //восстанавливаем таски
                final int id = task.getId(); //достаем ID таска
                if (id > generatorId) { //следим, на чем остановилось присвоение ID
                    generatorId = id;
                }
                taskManager.addAnyTask(task); //кладем таск в нужную мапу
            }
            //теперь нужно добавить сабтаски в эпики
            for (Map.Entry<Integer, SubTask> e : taskManager.subTasks.entrySet()) {//достаем сабтаск сетом <ID, Subtask>
            //почему не for (SubTask subTask : taskManager.subTasks.values()) { ?
                final SubTask subtask = e.getValue(); //достаем сабтаск
                final Epic epic = taskManager.epicTasks.get(subtask.getEpicId()); //достаем его эпик
                epic.addSubTaskId(subtask.getId()); //кладем в эпик ID сабтаска
            }
            for (Integer taskId : history) { //добавляем восстановленную историю в менеджер
                taskManager.inMemoryHistoryManager.add(taskManager.findTask(taskId));
            }
            taskManager.generatorId = generatorId; //передаем ID менеджеру, чтобы дальнейшее присвоение ID пошло верно
        } catch (IOException e) { //ловим исключение
            throw new ManagerSaveException("Can't read form file: " + file.getName(), e);
        }
        return taskManager;
    }

    protected void addAnyTask(Task task) { //метод для добавления таска в нужную мапу
        final int id = task.getId();
        switch (task.getType()) { //проверяем какой тип задачи
            case SUBTASK:
                subTasks.put(id, (SubTask) task); //при необходимости приводим к нужному типу
                break;
            case EPIC:
                epicTasks.put(id, (Epic) task); //при необходимости приводим к нужному типу
                break;
            case TASK:
                tasks.put(id, task); //и кладем в нужную мапу
                break;
        }
    }

    protected Task findTask(Integer id) { //поиск нужной задачи по ее ID
        final Task task = tasks.get(id); //пробуем достать из tasks
        if (task != null) { //если достали не null
            return task; // возвращаем то, что достали
        }
        final SubTask subtask = subTasks.get(id); //пробуем достать из subTasks
        if (subtask != null) { //если достали не null
            return subtask; //возвращаем то, что достали
        }
        return epicTasks.get(id); //если выше были null, значит ID принадлежит сабтаску, достаем и возвращаем
    }

    private void save() {
        StringBuilder saved = new StringBuilder();
        saved.append(getHeader()).append(System.lineSeparator());
        for (Task task : tasks.values()) {
            saved.append(taskToString(task) + System.lineSeparator());
        }
        for (Epic epic : epicTasks.values()) {
            saved.append(taskToString(epic) + System.lineSeparator());
        }
        for (SubTask subTask : subTasks.values()) {
            saved.append(taskToString(subTask) + System.lineSeparator());
        }

        saved.append(System.lineSeparator());

        saved.append(CSVTaskFormat.historyToString(inMemoryHistoryManager));

        try (OutputStreamWriter writer = new OutputStreamWriter
                (new FileOutputStream(file), StandardCharsets.UTF_8)) {
        writer.write(saved.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
        }
    }
}