import manager.*;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        Managers managers = new Managers();
        TaskManager inMemoryTaskManager = managers.getDefault();
        HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

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
        System.out.println(inMemoryHistoryManager.getHistory());
        System.out.println("____________");
        System.out.println("Создадим задачи и вызовем 10 задач по id");
        inMemoryTaskManager.createTask(new Task("задача 1", "описание 1"));
        inMemoryTaskManager.createTask(new Task("задача 2", "описание 2"));
        inMemoryTaskManager.createTask(new Task("задача 3", "описание 3"));
        inMemoryTaskManager.createTask(new Task("задача 4", "описание 4"));
        inMemoryTaskManager.createTask(new Task("задача 5", "описание 5"));
        inMemoryTaskManager.createTask(new Task("задача 6", "описание 6"));
        inMemoryTaskManager.createEpic(new Epic("эпик 1", "описание эпика 1"));
        inMemoryTaskManager.createEpic(new Epic("эпик 2", "описание эпика 2"));
        inMemoryTaskManager.createEpic(new Epic("эпик 3", "описание эпика 3"));
        inMemoryTaskManager.createEpic(new Epic("эпик 4", "описание эпика 4"));
        inMemoryTaskManager.createSubTask(new SubTask("Сабтаск 1","описание сабтаска 1", 7));
        inMemoryTaskManager.createSubTask(new SubTask("Сабтаск 2","описание сабтаска 2", 7));
        inMemoryTaskManager.createSubTask(new SubTask("Сабтаск 3","описание сабтаска 3", 7));
        inMemoryTaskManager.createSubTask(new SubTask("Сабтаск 4","описание сабтаска 4", 7));

        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getTaskById(3);
        inMemoryTaskManager.getTaskById(4);
        inMemoryTaskManager.getTaskById(5);
        inMemoryTaskManager.getTaskById(6);
        inMemoryTaskManager.getSubTaskById(11);
        inMemoryTaskManager.getSubTaskById(12);
        inMemoryTaskManager.getSubTaskById(13);
        inMemoryTaskManager.getSubTaskById(14);

        System.out.println(inMemoryHistoryManager.getHistory());
        System.out.println("____________");

        System.out.println("вызовем еще 2 задачи и снова посмотрим историю");

        inMemoryTaskManager.getEpicById(7);
        inMemoryTaskManager.getEpicById(8);

        System.out.println(inMemoryHistoryManager.getHistory());

    }
}
