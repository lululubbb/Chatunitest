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
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapEntrySetTest {

    private LinkedTreeMap<String, Integer> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void entrySet_shouldReturnSameInstanceOnMultipleCalls() {
        Set<Entry<String, Integer>> firstCall = map.entrySet();
        Set<Entry<String, Integer>> secondCall = map.entrySet();
        assertNotNull(firstCall);
        assertSame(firstCall, secondCall);
    }

    @Test
    @Timeout(8000)
    void entrySet_shouldReflectPutEntries() {
        map.put("one", 1);
        map.put("two", 2);

        Set<Entry<String, Integer>> entries = map.entrySet();
        assertEquals(2, entries.size());

        boolean foundOne = false;
        boolean foundTwo = false;
        for (Entry<String, Integer> e : entries) {
            if ("one".equals(e.getKey()) && Integer.valueOf(1).equals(e.getValue())) {
                foundOne = true;
            }
            if ("two".equals(e.getKey()) && Integer.valueOf(2).equals(e.getValue())) {
                foundTwo = true;
            }
        }
        assertTrue(foundOne);
        assertTrue(foundTwo);
    }

    @Test
    @Timeout(8000)
    void entrySet_iteratorRemoveShouldDecreaseSize() {
        map.put("a", 10);
        map.put("b", 20);
        Set<Entry<String, Integer>> entries = map.entrySet();
        Iterator<Entry<String, Integer>> it = entries.iterator();
        assertTrue(it.hasNext());
        it.next();
        it.remove();
        assertEquals(1, map.size());
    }

    @Test
    @Timeout(8000)
    void entrySet_iteratorThrowsNoSuchElementException() {
        Set<Entry<String, Integer>> entries = map.entrySet();
        Iterator<Entry<String, Integer>> it = entries.iterator();
        assertFalse(it.hasNext());
        assertThrows(java.util.NoSuchElementException.class, it::next);
    }

    @Test
    @Timeout(8000)
    void entrySet_iteratorRemoveWithoutNextThrowsIllegalStateException() {
        map.put("x", 100);
        Set<Entry<String, Integer>> entries = map.entrySet();
        Iterator<Entry<String, Integer>> it = entries.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }
}