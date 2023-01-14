package manager;

import tasks.Task;

import java.util.Objects;

public class Node<T extends Task> {
    Node<T> prev;
    Node<T> next;
    T task;

    public Node(Node<T> prev, T task, Node<T> next) {
        this.prev = prev;
        this.task = task;
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(prev, node.prev) && Objects.equals(next, node.next) && task.equals(node.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prev, next, task);
    }
}
