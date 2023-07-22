package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.9;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 32;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 16;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int size = 0;
    private double resizingLoadFactorThreshold;
    private int chainInitialCapacity;

    private int getChainIndex(K key) {
        // calculate the chain index
        return Math.abs(key.hashCode()) % chains.length;
    }

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.chains = createArrayOfChains(initialChainCount);
        this.resizingLoadFactorThreshold = resizingLoadFactorThreshold;
        this.chainInitialCapacity = chainInitialCapacity;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    @Override
    public V get(Object key) {
        int chainIndex = getChainIndex((K) key);
        if (chains[chainIndex] != null) {
            AbstractIterableMap<K, V> chain = chains[chainIndex];
            return chain.get(key);
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (chains.length == 0) {
            chains = createArrayOfChains(DEFAULT_INITIAL_CHAIN_COUNT);
        } else if ((double) size / (double) chains.length >= resizingLoadFactorThreshold) {
            // create a new chain map, add all entries we have, swap
            // iterator would be very useful
            ChainedHashMap<K, V> newHashMap = new ChainedHashMap<K, V>(this.resizingLoadFactorThreshold,
                2 * chains.length,
                chainInitialCapacity);
            // Rehash all key-values
            Iterator<Map.Entry<K, V>> curIterator = iterator();
            while (curIterator.hasNext()) {
                Map.Entry<K, V> e = curIterator.next();
                newHashMap.put(e.getKey(), e.getValue());
            }
            // place the data members from the new map into the current one
            chains = newHashMap.chains;
        }
        int chainIndex = getChainIndex((K) key);
        if (chains[chainIndex] == null) {
            // need a new chain
            chains[chainIndex] = new ArrayMap<K, V>(chainInitialCapacity);
        }
        AbstractIterableMap<K, V> chain = chains[chainIndex];
        int oldChainSize = chain.size();
        V result = chain.put(key, value);
        // update our map size if the chain size changed
        this.size += chain.size() - oldChainSize;
        return result;
    }

    @Override
    public V remove(Object key) {
        int chainIndex = getChainIndex((K) key);
        if (chains[chainIndex] == null) {
            return null;
        }
        AbstractIterableMap<K, V> chain = chains[chainIndex];
        int oldChainSize = chain.size();
        V result = chain.remove(key);
        // update our map size if the chain size changed
        this.size += chain.size() - oldChainSize;
        return result;
    }

    @Override
    public void clear() {
        this.size = 0;
        // recreate the empty chains
        chains = createArrayOfChains(chains.length);
    }

    @Override
    public boolean containsKey(Object key) {
        int chainIndex = getChainIndex((K) key);
        if (chains[chainIndex] == null) {
            return false;
        }
        AbstractIterableMap<K, V> chain = chains[chainIndex];
        return chain.containsKey(key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        // You may add more fields and constructor parameters
        private Iterator<Map.Entry<K, V>> currentChainIterator;
        private int nextChainIndex = 0;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;
        }

        @Override
        public boolean hasNext() {
            if (currentChainIterator != null && currentChainIterator.hasNext()) {
                // current chain is not empty
                return true;
            }
            // find the next non-empty chain
            while (nextChainIndex < chains.length) {
                AbstractIterableMap<K, V> chain = chains[nextChainIndex++];
                if (chain != null && chain.size() > 0) {
                    currentChainIterator = chain.iterator();
                    return true;
                }
            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                return currentChainIterator.next();
            }
            throw new NoSuchElementException();
        }
    }
}
