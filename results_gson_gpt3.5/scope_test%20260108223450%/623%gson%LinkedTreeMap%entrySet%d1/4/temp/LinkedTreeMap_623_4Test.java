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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapEntrySetTest {

    LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void entrySet_shouldReturnSameInstanceOnMultipleCalls() {
        Set<Entry<String, String>> firstCall = map.entrySet();
        Set<Entry<String, String>> secondCall = map.entrySet();
        assertNotNull(firstCall);
        assertSame(firstCall, secondCall);
    }

    @Test
    @Timeout(8000)
    void entrySet_shouldContainEntriesAfterPut() {
        map.put("key1", "value1");
        map.put("key2", "value2");

        Set<Entry<String, String>> entries = map.entrySet();
        assertEquals(2, entries.size());

        boolean foundKey1 = false;
        boolean foundKey2 = false;
        for (Entry<String, String> entry : entries) {
            if ("key1".equals(entry.getKey()) && "value1".equals(entry.getValue())) {
                foundKey1 = true;
            }
            if ("key2".equals(entry.getKey()) && "value2".equals(entry.getValue())) {
                foundKey2 = true;
            }
        }
        assertTrue(foundKey1);
        assertTrue(foundKey2);
    }

    @Test
    @Timeout(8000)
    void entrySet_iteratorRemoveShouldRemoveEntry() {
        map.put("key1", "value1");
        Set<Entry<String, String>> entries = map.entrySet();
        var iterator = entries.iterator();

        assertTrue(iterator.hasNext());
        Entry<String, String> entry = iterator.next();
        assertEquals("key1", entry.getKey());
        iterator.remove();

        assertTrue(entries.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    @Timeout(8000)
    void entrySet_iteratorShouldThrowNoSuchElementException() {
        Set<Entry<String, String>> entries = map.entrySet();
        var iterator = entries.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(java.util.NoSuchElementException.class, iterator::next);
    }

    @Test
    @Timeout(8000)
    void entrySet_iteratorRemoveWithoutNextShouldThrowIllegalStateException() {
        map.put("key1", "value1");
        Set<Entry<String, String>> entries = map.entrySet();
        var iterator = entries.iterator();
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    @Timeout(8000)
    void entrySet_iteratorConcurrentModificationShouldThrow() {
        map.put("key1", "value1");
        Set<Entry<String, String>> entries = map.entrySet();
        var iterator = entries.iterator();

        // Use reflection to increment modCount and simulate concurrent modification
        try {
            var modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
            modCountField.setAccessible(true);
            int currentModCount = (int) modCountField.get(map);
            modCountField.set(map, currentModCount + 1);
        } catch (ReflectiveOperationException e) {
            fail("Failed to modify modCount via reflection: " + e);
        }

        assertThrows(java.util.ConcurrentModificationException.class, iterator::next);
    }
}