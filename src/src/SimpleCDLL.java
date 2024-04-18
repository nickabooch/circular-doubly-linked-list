package src;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Impl of a circularly-linked, doubly-linked list with a dummy node.
 *  Implements a fail-fast iterator to handle concurrent modifications.
 *
 * The list uses a dummy node to simp handling of edge cases, particularly at the
 * boundaries of the list, making operations like add and remove more uniform by eliminating
 * special cases for head and tail operations.
 *
 * @author Rommin Adl
 * @see SimpleList
 * @see Node2
 */
public class SimpleCDLL<T> implements SimpleList<T> {
    private Node2<T> dummy; // This is the dummy node to act as both head and tail
    private int size;       // This is the num of elements in the list, excluding the dummy node
    private int modCount = 0; //this is the modification count for fail-fast behavior

    /**
     * This makes/constructs an empty circular doubly-linked list with a single dummy node.
     * The dummy node points to itself in both directions, simplifying boundary operations.
     */
    public SimpleCDLL() {
        dummy = new Node2<>(null);
        dummy.next = dummy;
        dummy.prev = dummy;
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIteratorImpl();
    }

    private class ListIteratorImpl implements ListIterator<T> {
        private Node2<T> lastReturned = dummy;
        private Node2<T> next = dummy.next;
        private int nextIndex = 0;
        private int expectedModCount = modCount;

        // Ensures that this iterator has not been compromised by concurrent modifications
        private void checkforcomod() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            checkforcomod();
            return nextIndex < size;
        }

        @Override
        public T next() {
            checkforcomod();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.value;
        }

        @Override
        public boolean hasPrevious() {
            checkforcomod();
            return nextIndex > 0;
        }

        @Override
        public T previous() {
            checkforcomod();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            next = next.prev;
            lastReturned = next;
            nextIndex--;
            return lastReturned.value;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void add(T e) {
            checkforcomod();
            Node2<T> newNode = new Node2<>(next.prev, e, next);
            next.prev.next = newNode;
            next.prev = newNode;
            lastReturned = dummy;  
            size++;
            nextIndex++;
            modCount++;
            expectedModCount = modCount; 
        }

        @Override
        public void remove() {
            checkforcomod();
            if (lastReturned == dummy) {
                throw new IllegalStateException("This is the illegal state for removal aka the iterator is at dummy node.");
            }
            lastReturned.remove();
            if (lastReturned == next) {
                next = next.next;
            } else {
                nextIndex--;
            }
            lastReturned = dummy;
            size--;
            modCount++;
            expectedModCount = modCount; 
        }

        @Override
        public void set(T e) {
            checkforcomod();
            if (lastReturned == dummy) {
                throw new IllegalStateException("This is the illegal state for set operation aka no element was returned by last call to next or prev.");
            }
            lastReturned.value = e;
        }
    }

    // Public interface methods to modify the list
    public void addFirst(T e) {
        addBetween(e, dummy, dummy.next);
    }

    public void addLast(T e) {
        addBetween(e, dummy.prev, dummy);
    }

    private void addBetween(T e, Node2<T> prevNode, Node2<T> nextNode) {
        Node2<T> newNode = new Node2<>(prevNode, e, nextNode);
        prevNode.next = newNode;
        nextNode.prev = newNode;
        size++;
        modCount++;
    }

    public T removeFirst() {
        if (size == 0) throw new NoSuchElementException("List is empty.");
        T element = dummy.next.value;
        removeNode(dummy.next);
        return element;
    }

    public T removeLast() {
        if (size == 0) throw new NoSuchElementException("List is empty.");
        T element = dummy.prev.value;
        removeNode(dummy.prev);
        return element;
    }

    private void removeNode(Node2<T> node) {
        if (node == dummy) {
            throw new NoSuchElementException("Cannot remove dummy node.");
        }
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
        modCount++;
    }
}
