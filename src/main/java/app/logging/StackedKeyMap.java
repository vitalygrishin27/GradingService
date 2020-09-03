package app.logging;

import java.io.Serializable;
import java.util.*;

public class StackedKeyMap<K, V> extends HashMap<K, V> implements Cloneable, Serializable {
    private Map<Long, K> stackedMap = new HashMap<>();
    private Map<K, V> map = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public V put(K key, V value) {
        if (!stackedMap.containsValue(key)) {
            stackedMap.put(counter++, key);
        }
        return map.put(key, value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public V remove(Object key) {
        Optional<Long> keyFromStackedMap = stackedMap.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), key))
                .map(Entry::getKey)
                .findFirst();
        keyFromStackedMap.ifPresent(stackedMap::remove);
        return map.remove(key);
    }

    public List<K> getValuesInStackOrder() {
        List<K> sortedValues = new ArrayList<>();
        List<Long> keys = new ArrayList<>(stackedMap.keySet());
        keys.sort(Long::compareTo);
        for (Long timing : keys) {
            sortedValues.add(stackedMap.get(timing));
        }
        return sortedValues;
    }

}
