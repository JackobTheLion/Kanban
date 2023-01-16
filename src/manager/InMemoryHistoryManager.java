package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first; //первый элемент в списке (самый старый в истории)
    private Node last; //последний элемент в списке (самый новый в истории)
    private final Map<Integer, Node> history = new HashMap<>(); //мапа ID -> нода


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            System.out.println("Ошибка в InMemoryHistoryManager.add. Task == null");
            return;
        }

        final int id = task.getId();
        removeNode(id);
        linkLast(task);
        history.put(id, last);
    }

    @Override
    public void remove(int id) {
        Node node = history.get(id);
        removeNode(id);
    }

    private void linkLast(Task task) {
        final Node node = new Node(last, task, null); //создаем ноду под пришедший таск
        if (first == null) { //если первая нода null, то история пуста, наша нода становится первой
            first = node;
        } else { //иначе добавляем ссылку последней ноде на новую ноду
            last.next = node;
        }
        last = node; //новая нода становится последней
    }

    private void removeNode(int id) {
        final Node node = history.remove(id); //достаем ноду и одновременно удаляем ее из мапы истории
        if (node == null) { //если она null, завершаем метод, такого таска нет в истории, нечего удалять
            return;
        }
        if (node.prev != null) { //если предыдущая нода не равна нулю (т.е. наша нода не первая)
            node.prev.next = node.next; //то значением next предыдущей становится значение next удаляемой
            if (node.next == null) { //если next удаляемой null (удаляемая - последняя)
                last = node.prev; //last истории становится предыдущая от удаляемой
            } else { //если next удаляемой не null (удаляемая не последняя)
                node.next.prev = node.prev; //предыдущая следующей становится предыдущей удаляемой
            }
        } else {  // если наша нода первая
            first = node.next; //то следующая от удаляемой становится первой
            if (first == null) { //если она оказалась null (мы удалили единственную запись)
                last = null; //То и последняя становится null. История пуста.
            } else { //если она не null, то prev у первой становится null (т.к. она становится первой)
                first.prev = null;
            }
        }
    }

    private List<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }
        return tasks;
    }

    private static class Node {
        Node prev;
        Node next;
        Task task;

        private Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}
