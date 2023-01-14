import manager.*;
import tasks.*;

public class Main {

    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();

 /*       inMemoryTaskManager.createTask(new Task("Вынести мусор", "по пути в магазин вынести мусор"));
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
        System.out.println("____________");*/

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
        inMemoryTaskManager.createTask(new Task("задача 3", "описание 1", Status.NEW));
        inMemoryTaskManager.createTask(new Task("задача 4", "описание 1", Status.NEW));
        inMemoryTaskManager.createTask(new Task("задача 5", "описание 1", Status.NEW));
        inMemoryTaskManager.createTask(new Task("задача 6", "описание 1", Status.NEW));

        System.out.println("Вызовем задачи и посмотрим историю");
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getSubTaskById(4);
        inMemoryTaskManager.getSubTaskById(5);
        inMemoryTaskManager.getSubTaskById(6);
        inMemoryTaskManager.getEpicById(7);
        inMemoryTaskManager.getTaskById(8);
        inMemoryTaskManager.getTaskById(9);
        inMemoryTaskManager.getTaskById(10);

        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("____________");

        System.out.println("Вызовем задачи в другом порядке и по    смотрим историю");
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

        System.out.println("Удалим эпик с ID 3 и проверим историю");
        inMemoryTaskManager.deleteEpicById(3);
        System.out.println(inMemoryTaskManager.getHistory());







    }
}