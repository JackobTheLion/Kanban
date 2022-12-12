import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import Manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.createTask(new Task("Вынести мусор", "по пути в магазин вынести мусор"));
        taskManager.createTask(new Task("Помыть чашку", "Помыть чашку после кофе"));

        taskManager.createEpic(new Epic("Написать проект по ТЗ 3", "написать код по ТЗ"));
        taskManager.createSubTask(new SubTask("Внести исправления в соответствии с комментариями",
                "изучить комментарии и внести соответствующие исправления в код", 3));
        taskManager.createSubTask(new SubTask("Сдать работу по ТЗ 3",
                "написать работу и сдать", 3));

        taskManager.createEpic(new Epic("Прибраться дома", "навести порядок в квартире"));
        taskManager.createSubTask(new SubTask("Пропылесосить",
                "Пропылесосить", 6));

        System.out.println("Печатаем задачи");
        System.out.println(taskManager.getAllTasks());
        System.out.println("____________");

        System.out.println("Печатаем эпики");
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println("____________");

        System.out.println("Печатаем сабтаски");
        System.out.println(taskManager.getAllSubTasks());
        System.out.println("____________");

        System.out.println("Меняем статус задачи");
        System.out.println("... ... ...");
        taskManager.updateTask(taskManager.createUpdatedTask("status", "DONE", 1));
        System.out.println(taskManager.getAllTasks());
        System.out.println("____________");


        System.out.println("Меняем статус подзадачи");
        taskManager.updateSubtask(taskManager.createUpdatedSubTask("status", "DONE", 4));
        taskManager.updateSubtask(taskManager.createUpdatedSubTask("status", "DONE", 7));
        System.out.println("... ... ...");
        System.out.println("Печатаем эпики");
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println("____________");

        System.out.println("Удаляем задачу и эпик");
        taskManager.deleteTaskById(2);
        taskManager.deleteEpicById(6);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpicTasks());
    }
}
