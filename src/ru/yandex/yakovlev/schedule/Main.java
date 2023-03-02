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

       /* TaskManager inMemoryTaskManager = Managers.getDefault();

        inMemoryTaskManager.createTask(new Task("Вынести мусор", "по пути в магазин вынести мусор"));
        inMemoryTaskManager.createTask(new Task("Помыть чашку", "Помыть чашку после кофе"));

        inMemoryTaskManager.createEpic(new Epic("Написать проект по ТЗ 3", "написать код по ТЗ"));
        inMemoryTaskManager.createSubTask(new SubTask("Внести исправления в соответствии с комментариями",
                "изучить комментарии и внести соответствующие исправления в код", 3));
        inMemoryTaskManager.createSubTask(new SubTask("Сдать работу по ТЗ 3",
                "написать работу и сдать", 3));

        inMemoryTaskManager.createEpic(new Epic("Прибраться дома", "навести порядок в квартире"));
        inMemoryTaskManager.createSubTask(new SubTask("Пропылесосить",
                "Пропылесосить", 6));

        System.out.println("Печатаем задачи");
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println("____________");

        System.out.println("Печатаем эпики");
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("____________");

        System.out.println("Печатаем сабтаски");
        System.out.println(inMemoryTaskManager.getAllSubTasks());
        System.out.println("____________");

        System.out.println("Меняем статус задачи");
        System.out.println("... ... ...");
        Task updatedTask = new Task("Вынести мусор", "по пути в магазин вынести мусор");
        updatedTask.setId(1);
        updatedTask.setStatus(Status.DONE);
        inMemoryTaskManager.updateTask(updatedTask);
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println("____________");

        System.out.println("Меняем статус подзадачи");
        SubTask updatedSubTask = new SubTask("Внести исправления в соответствии с комментариями",
                "изучить комментарии и внести соответствующие исправления в код", 3);
        updatedSubTask.setId(4);
        updatedSubTask.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(updatedSubTask);

        updatedSubTask = new SubTask("Пропылесосить", "Пропылесосить", 6);
        updatedSubTask.setId(7);
        updatedSubTask.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(updatedSubTask);

        System.out.println("... ... ...");
        System.out.println("Печатаем эпики");
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("____________");

        System.out.println("Удаляем задачу и эпик и печатаем все эпики и задачи");
        inMemoryTaskManager.deleteTaskById(2);
        inMemoryTaskManager.deleteEpicById(6);
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpicTasks());
        System.out.println("____________");

        System.out.println("Проверим историю. Должно быть пусто");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("____________");

        System.out.println("Создадим задачи...");
        inMemoryTaskManager.createTask(new Task("задача 1", "описание 1", Status.NEW));
        inMemoryTaskManager.createTask(new Task("задача 2", "описание 2", Status.NEW));
        inMemoryTaskManager.createEpic(new Epic("эпик 1", "описание эпика 1", Status.NEW));
        inMemoryTaskManager.createSubTask(new SubTask("Сабтаск 1",
                "описание сабтаска 1", 3, Status.NEW));
        inMemoryTaskManager.createSubTask(new SubTask("Сабтаск 2",
                "описание сабтаска 2", 3, Status.NEW));
        inMemoryTaskManager.createSubTask(new SubTask("Сабтаск 3",
                "описание сабтаска 3", 3, Status.NEW));
        inMemoryTaskManager.createEpic(new Epic("эпик 2", "описание эпика 1", Status.NEW));


        System.out.println("Вызовем задачи и посмотрим историю");
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getSubTaskById(4);
        inMemoryTaskManager.getSubTaskById(5);
        inMemoryTaskManager.getSubTaskById(6);
        inMemoryTaskManager.getEpicById(7);

        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("____________");

        System.out.println("Вызовем задачи в другом порядке и посмотрим историю");
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getSubTaskById(5);
        inMemoryTaskManager.getSubTaskById(6);
        inMemoryTaskManager.getSubTaskById(4);
        inMemoryTaskManager.getEpicById(7);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getTaskById(1);
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("____________");

        System.out.println("Удалим задачу с ID 2 и проверим историю");
        inMemoryTaskManager.deleteTaskById(2);
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("____________");

        System.out.println("Удалим эпик с ID 3 и проверим историю (должны удалиться в т.ч. его подзадачи)");
        inMemoryTaskManager.deleteEpicById(3);
        System.out.println(inMemoryTaskManager.getHistory());*/

        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

        HttpTaskManager taskManager = new HttpTaskManager("http://localhost:8078");

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
        //FileBackedTasksManager.deleteEpicById(epic2.getId());
        System.out.println(taskManager.getHistory());

        HttpTaskManager taskManager2 = new HttpTaskManager("http://localhost:8078");
        taskManager2.kvClient.API_TOKEN = taskManager.kvClient.API_TOKEN;
        taskManager2.load();
        System.out.println(taskManager2.kvClient.API_TOKEN);
        System.out.println(taskManager.kvClient.API_TOKEN);

        System.out.println("____________");

        System.out.println("Проверим историю в fileBackedTasksManager.")   ;
        System.out.println("\nИстории одинаковые?");
        System.out.println(taskManager.getHistory().equals(taskManager2.getHistory()));
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