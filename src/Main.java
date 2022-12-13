import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import manager.TaskManager;

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
        Task updatedTask = new Task("Вынести мусор", "по пути в магазин вынести мусор");
        updatedTask.setId(1);
        updatedTask.setStatus("DONE");
        taskManager.updateTask(updatedTask);
        System.out.println(taskManager.getAllTasks());
        System.out.println("____________");


        System.out.println("Меняем статус подзадачи");
        SubTask updatedSubTask = new SubTask("Внести исправления в соответствии с комментариями",
                "изучить комментарии и внести соответствующие исправления в код", 3);
        updatedSubTask.setId(4);
        updatedSubTask.setStatus("DONE");
        taskManager.updateSubtask(updatedSubTask);

        updatedSubTask = new SubTask("Пропылесосить", "Пропылесосить", 6);
        updatedSubTask.setId(7);
        updatedSubTask.setStatus("DONE");
        taskManager.updateSubtask(updatedSubTask);

        System.out.println("... ... ...");
        System.out.println("Печатаем эпики");
        System.out.println(taskManager.getAllEpicTasks());
        System.out.println("____________");

        System.out.println("Удаляем задачу и эпик и печатаем все эпики и задачи");
        taskManager.deleteTaskById(2);
        taskManager.deleteEpicById(6);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpicTasks());
    }
}
