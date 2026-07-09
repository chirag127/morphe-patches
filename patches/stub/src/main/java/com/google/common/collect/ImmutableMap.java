package com.google.common.collect;

import java.util.Map;
import java.util.Set;

/**
 * Compile-time stub for com.google.common.collect.ImmutableMap.
 *
 * ImmutableMap is part of Guava and is present in the patched app's DEX.
 * SkipAdsPatch.java uses it as the parameter type for setAdPlaybackStates()
 * and constructs a new instance via ImmutableMap.Builder to return the
 * transformed ad state map.
 *
 * Only the surface used in SkipAdsPatch.java is stubbed:
 *   - entrySet() for iterating the existing map entries
 *   - builder() for constructing the replacement map
 *   - Builder.put() and Builder.build() for populating and finalising it
 *
 * DO NOT add implementation logic — this class is never shipped to the device.
 * The real Guava class in the patched APK's DEX is used at runtime.
 */
@SuppressWarnings("unused")
public class ImmutableMap<K, V> implements Map<K, V> {

    /**
     * Returns a new Builder for constructing an ImmutableMap.
     * Used in SkipAdsPatch.java to build the transformed ad state map.
     */
    public static <K, V> Builder<K, V> builder() {
        throw new UnsupportedOperationException("stub");
    }

    /**
     * Returns the set of entries in this map.
     * Used in SkipAdsPatch.java to iterate over period UID → AdPlaybackState pairs.
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("stub");
    }

    // ── Remaining Map<K,V> interface methods — stubs only, not used by the extension ──

    @Override public int size() { throw new UnsupportedOperationException("stub"); }
    @Override public boolean isEmpty() { throw new UnsupportedOperationException("stub"); }
    @Override public boolean containsKey(Object key) { throw new UnsupportedOperationException("stub"); }
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException("stub"); }
    @Override public V get(Object key) { throw new UnsupportedOperationException("stub"); }
    @Override public V put(K key, V value) { throw new UnsupportedOperationException("stub"); }
    @Override public V remove(Object key) { throw new UnsupportedOperationException("stub"); }
    @Override public void putAll(Map<? extends K, ? extends V> m) { throw new UnsupportedOperationException("stub"); }
    @Override public void clear() { throw new UnsupportedOperationException("stub"); }
    @Override public java.util.Collection<V> values() { throw new UnsupportedOperationException("stub"); }
    @Override public java.util.Set<K> keySet() { throw new UnsupportedOperationException("stub"); }

    /**
     * Builder for constructing a new ImmutableMap.
     * Used in SkipAdsPatch.java to accumulate the transformed entries
     * before calling build() to produce the final map.
     */
    public static final class Builder<K, V> {

        /**
         * Adds a key-value pair to the map being built.
         *
         * @param key   Period UID (Object).
         * @param value Transformed AdPlaybackState with all groups skipped.
         * @return this Builder, for chaining.
         */
        public Builder<K, V> put(K key, V value) {
            throw new UnsupportedOperationException("stub");
        }

        /**
         * Returns the completed ImmutableMap containing all entries added via put().
         *
         * @return A new ImmutableMap.
         */
        public ImmutableMap<K, V> build() {
            throw new UnsupportedOperationException("stub");
        }
    }
}
