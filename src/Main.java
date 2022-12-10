import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task firstTask = manager.createTask("Выбросить мусор", "по пути в магазин взять мусор " +
                "и выкинуть");
        manager.addNewTask(firstTask);

        Task secondTask = manager.createTask("Позвонить маме", "по пути в магазин набрать маму");
        manager.addNewTask(secondTask);

        EpicTask firstEpic = manager.createEpicTask("Написать проект по ТЗ 3", "написать код по ТЗ");
        manager.addNewTask(firstEpic);
        SubTask firstSubOfFirstEpic = manager.createSubTask("изучить ТЗ", "внимательно прочитать ТЗ", 3);
        manager.addNewTask(firstSubOfFirstEpic);
        SubTask secondSubOfFirstEpic = manager.createSubTask("написать код по ТЗ", "написать и протестировать " +
                "код по ТЗ", 3);
        manager.addNewTask(secondSubOfFirstEpic);

        EpicTask secondEpic = manager.createEpicTask("Прибраться дома", "навести дома порядок");
        manager.addNewTask(secondEpic);
        SubTask firstSubOfSecondEpic = manager.createSubTask("Пропылесосить", "взять пылесос и " +
                "пропылесосить квартиру", 6);
        manager.addNewTask(firstSubOfSecondEpic);

        System.out.println("______________________");
        System.out.println("Печатаем все задачи");
        System.out.println(manager.getListOfTasks(1));
        System.out.println("______________________");
        System.out.println("Печатаем все эпики");
        System.out.println(manager.getListOfTasks(2));
        System.out.println("______________________");
        System.out.println("Печатаем все сабы");
        System.out.println(manager.getListOfTasks(3));
        System.out.println("______________________");

        System.out.println("Меняем статусы.");
        System.out.println("... ... ...");
        manager.updateTask(manager.createUpdatedTask("status", "DONE", 1));
        manager.updateTask(manager.createUpdatedTask("status", "DONE", 4));
        manager.updateTask(manager.createUpdatedTask("status", "DONE", 7));
        System.out.println("Печатаем все задачи");
        System.out.println(manager.getListOfTasks(1));
        System.out.println("______________________");
        System.out.println("Печатаем все эпики");
        System.out.println(manager.getListOfTasks(2));
        System.out.println("______________________");
        System.out.println("Печатаем все сабы");
        System.out.println(manager.getListOfTasks(3));
        System.out.println("______________________");

        System.out.println("Удаляем задачу");
        System.out.println("... ... ...");
        manager.deleteById(1);
        System.out.println("Печатаем все задачи");
        System.out.println(manager.getListOfTasks(1));
        System.out.println("______________________");

        System.out.println("Удаляем эпик");
        System.out.println("... ... ...");
        manager.deleteById(3);
        System.out.println("Печатаем все эпики");
        System.out.println(manager.getListOfTasks(2));
        System.out.println("______________________");
        System.out.println("Печатаем все сабы");
        System.out.println(manager.getListOfTasks(3));
        System.out.println("______________________");

    }
}
