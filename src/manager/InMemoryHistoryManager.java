package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first; //первый элемент в списке (самый старый в истории)
    private Node last; //последний элемент в списке (самый новый в истории)
    private int size = 0; //размер истории
    private final HashMap<Integer, Node> idToNode = new HashMap<>(); //мапа ID -> нода


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

        int id = task.getId();
        if (idToNode.containsKey(id)) {
            removeNode(idToNode.get(id));
        }

        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = idToNode.get(id);
        removeNode(node);
    }

    private void linkLast(Task task) {
        Node last = this.last;
        Node newNode = new Node(last,task, null);
        this.last = newNode;
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }

        size++;
        idToNode.put(task.getId(), newNode);
    }

    private void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
        }

        idToNode.remove(node.task.getId());
        size--;
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
