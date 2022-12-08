import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task = manager.createTask("Первая задача", "Очень важная задача");
        manager.addNewTask(task);
        Task task1 = manager.createTask("Вторая задача", "Тоже важная задача");
        manager.addNewTask(task1);

        EpicTask epicTask = manager.createEpicTask("БОЛЬШАЯ задача", "УберМегаБольшая и сложная задача");
        manager.addNewTask(epicTask);

        SubTask subTask1 = manager.createSubTask("первый сабтаск", "маленький такой", 3);
        manager.addNewTask(subTask1);
        SubTask subTask2 = manager.createSubTask("второй сабтаск", "маленький такой", 3);
        manager.addNewTask(subTask2);
        SubTask subTask3 = manager.createSubTask("третий сабтаск", "маленький такой", 3);
        manager.addNewTask(subTask3);

        System.out.println(manager.getListOfTasks(1));
        System.out.println(manager.getListOfTasks(2));
        System.out.println(manager.getListOfTasks(3));
        System.out.println("_____");

        manager.setStatus(task, 1);
        System.out.println(manager.getListOfTasks(1));
        System.out.println("_____");


        manager.setStatus(subTask1, 2);
        System.out.println(manager.getListOfTasks(2));
        System.out.println("_____");


        manager.setStatus(subTask2, 3);
        System.out.println(manager.getListOfTasks(2));
        System.out.println("_____");

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(6));
        System.out.println(manager.getTaskById(100));


    }
}
