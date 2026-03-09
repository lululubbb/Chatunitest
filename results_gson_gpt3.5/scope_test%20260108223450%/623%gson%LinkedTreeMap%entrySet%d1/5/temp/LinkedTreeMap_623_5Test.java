package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_entrySet_Test {

    LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void entrySet_returnsSameInstanceOnMultipleCalls() throws Exception {
        // First call should create new EntrySet instance and assign to entrySet field
        Set<Entry<String, String>> firstCall = map.entrySet();
        assertNotNull(firstCall);

        // Access private field entrySet via reflection to assert it is assigned
        Field entrySetField = LinkedTreeMap.class.getDeclaredField("entrySet");
        entrySetField.setAccessible(true);
        Object entrySetValue = entrySetField.get(map);
        assertSame(firstCall, entrySetValue);

        // Second call should return the same instance without creating a new one
        Set<Entry<String, String>> secondCall = map.entrySet();
        assertSame(firstCall, secondCall);
    }

    @Test
    @Timeout(8000)
    void entrySet_emptyMap_isEmpty() {
        Set<Entry<String, String>> entries = map.entrySet();
        assertTrue(entries.isEmpty());
        assertEquals(0, entries.size());
    }

    @Test
    @Timeout(8000)
    void entrySet_afterPut_containsEntry() {
        map.put("key1", "value1");
        Set<Entry<String, String>> entries = map.entrySet();
        assertEquals(1, entries.size());
        Entry<String, String> entry = entries.iterator().next();
        assertEquals("key1", entry.getKey());
        assertEquals("value1", entry.getValue());
    }

    @Test
    @Timeout(8000)
    void entrySet_iteratorSupportsRemove() {
        map.put("key1", "value1");
        Set<Entry<String, String>> entries = map.entrySet();
        Iterator<Entry<String, String>> it = entries.iterator();
        assertTrue(it.hasNext());
        it.next();
        it.remove();
        assertFalse(map.containsKey("key1"));
        assertEquals(0, map.size());
        assertFalse(it.hasNext());
    }

    @Test
    @Timeout(8000)
    void entrySet_iteratorThrowsNoSuchElementException() {
        Set<Entry<String, String>> entries = map.entrySet();
        Iterator<Entry<String, String>> it = entries.iterator();
        assertFalse(it.hasNext());
        assertThrows(java.util.NoSuchElementException.class, it::next);
    }
}