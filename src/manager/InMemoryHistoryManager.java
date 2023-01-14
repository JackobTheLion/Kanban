package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int MAX_HISTORY_SIZE = 10;

    private final CustomLinkedList<Task> history = new CustomLinkedList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            System.out.println("Ошибка в addToHistory. Task == null");
            return;
        }

        history.add(task);

        if (history.getSize() > MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    private class CustomLinkedList<T extends Task> extends LinkedList<T> {
        private Node<T> head; //первый элемент в списке (самый старый в истории)
        private Node<T> tail; //последний элемент в списке (самый новый в истории)
        private int size = 0;
        private HashMap<Integer, Node<T>> idToNode = new HashMap<>(); //мапа ID -> нода

        public int getSize() {
            return size;
        }

        private void linkLast(T task) {
            Node<T> last = tail;
            Node<T> newNode = new Node<T>(last,task, null);
            tail = newNode;
            if (last == null) {
                head = newNode;
            } else {
                last.next = newNode;
            }

            size++;
            idToNode.put(task.getId(), newNode);
        }

        @Override
        public boolean add(T task) {
            int id = task.getId();
            if (idToNode.containsKey(id)) {
                removeNode(idToNode.get(id));
            }
            linkLast(task);
            return true;
        }

        @Override
        public T remove(int id) {
            Node<T> node = idToNode.get(id);
            T task = node.task;
            removeNode(node);
            return task;
        }

        private void removeNode(Node<T> node) {
            Node<T> prev = node.prev;
            Node<T> next = node.next;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
            }
            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
            }

            idToNode.remove(node.task.getId());
            size--;
        }

        public ArrayList<T> getTasks() {
            ArrayList<T> tasks = new ArrayList<>(size);
            if (size == 0) {
                System.out.println("История пуста");
                return tasks;
            }
            int i = 0;
            for (Node<T> node = tail; node != null; node = node.prev) {
                tasks.add(i, node.task);
                i++;
            }

            return tasks;
        }

        private void unlinkFirst(Node<T> node) {
            Node<T> next = node.next;
            head = next;
            if (next == null) {
                tail = null;
            } else {
                next.prev = null;
            }

            size--;
        }

        public T removeFirst() {
            T task = head.task;
            unlinkFirst(head);
            return task;
        }
    }

}
