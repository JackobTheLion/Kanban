package ru.yandex.yakovlev.schedule.manager;

import ru.yandex.yakovlev.schedule.manager.exceptions.ManagerSaveException;
import ru.yandex.yakovlev.schedule.tasks.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.yandex.yakovlev.schedule.manager.CSVTaskFormat.*;


public class FileBackedTasksManager extends InMemoryTaskManager {

    protected String path = "";

    public FileBackedTasksManager(String path) {
        this.path = path;
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
        if (task == null) {
            //System.out.println("Ошибка. epic == null");
            return -1;
        }

        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        if (epic == null) {
            //System.out.println("Ошибка. epic == null");
            return -1;
        }

        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (subTask == null) {
            //System.out.println("Ошибка. subTask == null");
            return -1;
        }

        int id = super.createSubTask(subTask);
        save();
        return id;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager taskManager = new FileBackedTasksManager(file.getPath()); //создаем менеджер из файла
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
        switch (task.getTaskType()) { //проверяем какой тип задачи
            case SUBTASK:
                subTasks.put(id, (SubTask) task); //при необходимости приводим к нужному типу
                prioritizedTasks.add(task);
                scheduleManager.bookSchedule(task);
                break;
            case EPIC:
                epicTasks.put(id, (Epic) task); //при необходимости приводим к нужному типу
                break;
            case TASK:
                tasks.put(id, task); //и кладем в нужную мапу
                scheduleManager.bookSchedule(task);
                prioritizedTasks.add(task);
                break;
        }
    }

    protected Task findTask(Integer id) { //поиск нужной задачи по ее ID
        final Task task = tasks.get(id); //пробуем достать из ru.yandex.yakovlev.schedule.tasks
        if (task != null) { //если достали не null
            return task; // возвращаем то, что достали
        }
        final SubTask subtask = subTasks.get(id); //пробуем достать из subTasks
        if (subtask != null) { //если достали не null
            return subtask; //возвращаем то, что достали
        }
        return epicTasks.get(id); //если выше были null, значит ID принадлежит сабтаску, достаем и возвращаем
    }

    protected void save() {
        File file = new File(path);
        StringBuilder saved = new StringBuilder();
        saved.append(getHeader()).append(System.lineSeparator());
        for (Task task : tasks.values()) {
            saved.append(taskToString(task)).append(System.lineSeparator());
        }
        for (Epic epic : epicTasks.values()) {
            saved.append(taskToString(epic)).append(System.lineSeparator());
        }
        for (SubTask subTask : subTasks.values()) {
            saved.append(taskToString(subTask)).append(System.lineSeparator());
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