package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 2;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!
    private int size = 0;
    private int freeSpaceStartIndex = 0;

    private int indexByKey(K key) {
        // locates the index on the key, returns -1 if not found
        for (int i = 0; i < freeSpaceStartIndex; i++) {
            if (this.entries[i] != null && this.entries[i].getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        int index = indexByKey((K) key);
        if (index != -1) {
            return entries[index].getValue();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int index = indexByKey((K) key);
        if (index != -1) {
            // key is present in the map
            V result = entries[index].getValue();
            entries[index].setValue(value);
            return result;
        } else {
            // key is absent in the map
            if (this.entries.length == 0) {
                // empty initial map
                entries = createArrayOfEntries(32);
            } else if (this.freeSpaceStartIndex == this.entries.length) {
                // need to increase the map capacity
                // reallocate the entries
                SimpleEntry<K, V>[] oldEntries = entries;
                // double the array size
                entries = createArrayOfEntries(oldEntries.length * 2);
                // copy the old entries
                for (int i = 0; i < this.size; i++) {
                    entries[i] = oldEntries[i];
                }
                // now all items moved to the begining of the array
                freeSpaceStartIndex = size;
            }
            this.entries[freeSpaceStartIndex++] = new SimpleEntry<K, V>(key, value);
            this.size++;
            return null;
        }
    }

    @Override
    public V remove(Object key) {
        int index = indexByKey((K) key);
        if (index != -1) {
            V result = this.entries[index].getValue();
            this.entries[index] = null;
            this.size--;
            return result;
        }
        return null;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.freeSpaceStartIndex = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return indexByKey((K) key) != -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(this.entries, this.size);
    }


    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        // You may add more fields and constructor parameters
        private int nextIndex = 0;
        private int currentSlot = 0;

        private int size;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                // skip the null slots
                while (entries[currentSlot] == null) {
                    currentSlot++;
                }
                nextIndex++;
                return entries[currentSlot++];
            }
            throw new NoSuchElementException();
        }
    }
}
