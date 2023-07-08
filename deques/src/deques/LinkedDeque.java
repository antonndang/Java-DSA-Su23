package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    private int size;
    // Feel free to add any additional fields you may need, though.

    public LinkedDeque() {
        size = 0;
        front = new Node<T>(null);
        back = new Node<T>(null, front, null);
        front.next = back;
    }

    public void addFirst(T item) {
        size += 1;
        Node<T> newNode = new Node<>(item, front, front.next);
        newNode.next.prev = newNode;
        newNode.prev.next = newNode;
    }

    public void addLast(T item) {
        size += 1;
        Node<T> newNode = new Node<>(item, back.prev, back);
        newNode.next.prev = newNode;
        newNode.prev.next = newNode;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T result = front.next.value;
        front.next = front.next.next;
        front.next.prev = front;
        return result;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T result = back.prev.value;
        back.prev = back.prev.prev;
        back.prev.next = back;
        return result;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> current = front.next;
        while (--index >= 0) {
            current = current.next;
        }
        return current.value;
    }

    public int size() {
        return size;
    }
}
