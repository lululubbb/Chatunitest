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
import java.util.Set;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class LinkedTreeMapClearTest {

    private LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void testClearOnEmptyMap() throws Exception {
        // Initially empty map
        assertEquals(0, map.size());

        // Access private fields before clear
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
        sizeField.setAccessible(true);
        Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        Field headerField = LinkedTreeMap.class.getDeclaredField("header");
        headerField.setAccessible(true);

        Object rootBefore = rootField.get(map);
        int sizeBefore = sizeField.getInt(map);
        int modCountBefore = modCountField.getInt(map);
        Object header = headerField.get(map);

        // header is Node<K,V> with next and prev fields
        Class<?> nodeClass = header.getClass();
        Field nextField = nodeClass.getDeclaredField("next");
        Field prevField = nodeClass.getDeclaredField("prev");
        nextField.setAccessible(true);
        prevField.setAccessible(true);

        Object nextBefore = nextField.get(header);
        Object prevBefore = prevField.get(header);

        // Call clear
        map.clear();

        // After clear, root should be null
        assertNull(rootField.get(map));
        // size should be 0
        assertEquals(0, sizeField.getInt(map));
        // modCount should be incremented by 1
        assertEquals(modCountBefore + 1, modCountField.getInt(map));
        // header's next and prev should point to itself
        Object nextAfter = nextField.get(header);
        Object prevAfter = prevField.get(header);

        assertSame(header, nextAfter);
        assertSame(header, prevAfter);
    }

    @Test
    @Timeout(8000)
    void testClearOnNonEmptyMap() throws Exception {
        // Put some entries
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");

        assertEquals(3, map.size());

        // Access private fields before clear
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
        sizeField.setAccessible(true);
        Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        Field headerField = LinkedTreeMap.class.getDeclaredField("header");
        headerField.setAccessible(true);

        Object rootBefore = rootField.get(map);
        int sizeBefore = sizeField.getInt(map);
        int modCountBefore = modCountField.getInt(map);
        Object header = headerField.get(map);

        Class<?> nodeClass = header.getClass();
        Field nextField = nodeClass.getDeclaredField("next");
        Field prevField = nodeClass.getDeclaredField("prev");
        nextField.setAccessible(true);
        prevField.setAccessible(true);

        Object nextBefore = nextField.get(header);
        Object prevBefore = prevField.get(header);

        // Call clear
        map.clear();

        // After clear, root should be null
        assertNull(rootField.get(map));
        // size should be 0
        assertEquals(0, sizeField.getInt(map));
        // modCount should be incremented by 1
        assertEquals(modCountBefore + 1, modCountField.getInt(map));
        // header's next and prev should point to itself
        Object nextAfter = nextField.get(header);
        Object prevAfter = prevField.get(header);

        assertSame(header, nextAfter);
        assertSame(header, prevAfter);

        // Map should be empty now
        assertEquals(0, map.size());
        assertFalse(map.containsKey("one"));
        assertFalse(map.containsKey("two"));
        assertFalse(map.containsKey("three"));
    }
}