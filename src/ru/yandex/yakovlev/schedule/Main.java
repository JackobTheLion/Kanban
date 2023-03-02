package ru.yandex.yakovlev.schedule;

import ru.yandex.yakovlev.schedule.HTTP.HttpTaskManager;
import ru.yandex.yakovlev.schedule.HTTP.HttpTaskServer;
import ru.yandex.yakovlev.schedule.HTTP.KVServer;
import ru.yandex.yakovlev.schedule.tasks.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        HttpTaskManager taskManager = new HttpTaskManager("http://localhost:8078");
        taskManager.kvClient.setAPI_TOKEN_DEBUG();

        System.out.println("Создадим задачи...");
        Task task1 = new Task("задача 1","описание 1", Status.NEW);
        task1.setStartTime(LocalDateTime.of(2023,3,15,8, 0));
        task1.setDuration(60);
        taskManager.createTask(task1);

        Task task2 = new Task("задача 2","описание 2", Status.NEW);
        task2.setStartTime(LocalDateTime.of(2023,3,15,9, 0));
        task2.setDuration(40);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("эпик 1","описание эпика 1", Status.NEW);
        taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Сабтаск 1","описание сабтаска 1",
                epic1.getId(), Status.NEW);
        subTask1.setStartTime(LocalDateTime.of(2023,3,15,12, 0));
        subTask1.setDuration(30);
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Сабтаск 2","описание сабтаска 2",
                epic1.getId(), Status.NEW);
        subTask2.setStartTime(LocalDateTime.of(2023,3,15,12, 30));
        subTask2.setDuration(40);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Сабтаск 3","описание сабтаска 3",
                epic1.getId(), Status.NEW);
        subTask3.setStartTime(LocalDateTime.of(2023,3,15,13, 30));
        subTask3.setDuration(30);
        taskManager.createSubTask(subTask3);

        Epic epic2 = new Epic("эпик 2","описание эпика 1", Status.NEW);
        taskManager.createEpic(epic2);

        SubTask subTask4 = new SubTask("Сабтаск 4","описание сабтаска 4",
                epic2.getId(), Status.NEW);
        taskManager.createSubTask(subTask4);

        System.out.println("Вызовем задачи и посмотрим историю");
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getSubTaskById(subTask2.getId());
        taskManager.getSubTaskById(subTask3.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubTaskById(subTask4.getId());

        System.out.println(taskManager.getHistory());
        System.out.println("____________");

        System.out.println("Вызовем задачи в другом порядке и удалим 1 эпик посмотрим историю");
        taskManager.getTaskById(task2.getId());
        taskManager.getSubTaskById(subTask2.getId());
        taskManager.getSubTaskById(subTask3.getId());
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubTaskById(subTask4.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getTaskById(task1.getId());
        System.out.println(taskManager.getHistory());

        HttpTaskManager taskManager2 = new HttpTaskManager("http://localhost:8078");
        taskManager2.kvClient.setAPI_TOKEN_DEBUG();
        taskManager2.load();

        System.out.println("____________");

        System.out.println("Проверим историю в fileBackedTasksManager.")   ;
        System.out.println("\nИстории одинаковые?");
        System.out.println(taskManager.getHistory().equals(taskManager2.getHistory()));
        System.out.println(taskManager.getHistory());
        System.out.println("______");
        System.out.println(taskManager2.getHistory());
        System.out.println("____________");

        System.out.println("Проверим в ru.yandex.yakovlev.schedule.tasks.")   ;
        System.out.println("ru.yandex.yakovlev.schedule.tasks одинаковые?");
        System.out.println(taskManager.getAllTasks().equals(taskManager2.getAllTasks()));
        System.out.println("____________");

        System.out.println("Проверим в epics.")   ;
        System.out.println("epics одинаковые?");
        System.out.println(taskManager.getAllEpicTasks().equals(taskManager2.getAllEpicTasks()));
        System.out.println("____________");

        System.out.println("Проверим в subTasks.")   ;
        System.out.println("subTasks одинаковые?");
        System.out.println(taskManager.getAllSubTasks().equals(taskManager2.getAllSubTasks()));
        System.out.println("____________");

    }
}