package priorityqueues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    private Map<T, Integer> itemMap; // maps the item to the index in the heap

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        for (int i = 0; i < START_INDEX; i++) {
            items.add(null);
        }
        itemMap = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */

    private void checkHeapIndex(int a) {
        if (a < START_INDEX || a >= START_INDEX + size()) {
            throw new NoSuchElementException();
        }
    }
    private void swap(int a, int b) {
        checkHeapIndex(a);
        checkHeapIndex(b);
        // swap in the heap and in the items map
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
        itemMap.put(items.get(a).getItem(), a);
        itemMap.put(items.get(b).getItem(), b);
    }

    private int getParentIndex(int a) {
        switch (START_INDEX) {
            case 0: return (a - 1) / 2;
            case 1: return a / 2;
            default: throw new IllegalStateException("Only 0/1 start index supported");
        }
    }

    private int getLeftChildIndex(int a) {
        switch (START_INDEX) {
            case 0: return a * 2 + 1;
            case 1: return a * 2;
            default: throw new IllegalStateException("Only 0/1 start index supported");
        }
    }

    private int getRightChildIndex(int a) {
        switch (START_INDEX) {
            case 0: return a * 2 + 2;
            case 1: return a * 2 + 1;
            default: throw new IllegalStateException("Only 0/1 start index supported");
        }
    }

    private int getFirstLeafIndex() {
        switch (START_INDEX) {
            case 0: return size() / 2;
            case 1: return size() / 2 + 1;
            default: throw new IllegalStateException("Only 0/1 start index supported");
        }
    }

    private void siftUp(int a) {
        while (a > START_INDEX) {
            int p = getParentIndex(a);
            if (items.get(a).getPriority() < items.get(p).getPriority()) {
                swap(a, p);
                a = p;
            } else {
                break; // no need to swap anymore invariant restored
            }
        }
    }

    private void siftDown(int a) {
        int firstLeaf = getFirstLeafIndex();
        while (a < firstLeaf) {
            // presume left child has the lowest priority
            int minChildIndex = getLeftChildIndex(a);
            // check the right child too
            int rightChildIndex = getRightChildIndex(a);
            if (rightChildIndex < START_INDEX + size() &&
                items.get(rightChildIndex).getPriority() < items.get(minChildIndex).getPriority()) {
                minChildIndex = rightChildIndex;
            }
            // check if we need to swap with "a" (parent)
            if (items.get(minChildIndex).getPriority() < items.get(a).getPriority()) {
                swap(a, minChildIndex);
                a = minChildIndex;
            } else {
                break; // no more swaps break
            }
        }
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        if (size() == START_INDEX + items.size()) {
            // need additional storage
            items.add(new PriorityNode<>(item, priority));
        } else {
            // set the last item
            items.set(START_INDEX + size(), new PriorityNode<>(item, priority));
        }
        // update the item map
        itemMap.put(item, START_INDEX + size());
        // restore the heap invariant for the last index
        siftUp(START_INDEX + size() - 1);
    }

    @Override
    public boolean contains(T item) {
        return itemMap.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        T result = peekMin();
        itemMap.remove(result); // reduced the size by 1
        items.set(START_INDEX, items.get(START_INDEX + itemMap.size())); // move the last item to START_INDEX
        siftDown(START_INDEX); // Restore the invariant
        return result;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int itemIndex = itemMap.get(item);
        PriorityNode<T> node = items.get(itemIndex);
        double oldPriority = node.getPriority();
        node.setPriority(priority);
        if (priority > oldPriority) {
            siftDown(itemIndex);
        } else if (priority < oldPriority) {
            siftUp(itemIndex);
        }
    }

    @Override
    public int size() {
        return itemMap.size();
    }
}
