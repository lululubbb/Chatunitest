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

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        // Capture modCount before clear
        int modCountBefore = getModCount(map);

        // Clear map
        map.clear();

        // After clear, root should be null
        assertNull(getRoot(map));

        // size should be zero
        assertEquals(0, map.size());

        // modCount should be incremented by 1
        int modCountAfter = getModCount(map);
        assertEquals(modCountBefore + 1, modCountAfter);

        // header's next and prev should point to header itself
        Object header = getHeader(map);
        Object next = getField(header, "next");
        Object prev = getField(header, "prev");
        assertSame(header, next);
        assertSame(header, prev);
    }

    @Test
    @Timeout(8000)
    void testClearOnNonEmptyMap() throws Exception {
        // Put some entries
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");

        assertEquals(3, map.size());
        assertNotNull(getRoot(map));

        int modCountBefore = getModCount(map);

        // Clear map
        map.clear();

        // root should be null
        assertNull(getRoot(map));

        // size should be zero
        assertEquals(0, map.size());

        // modCount incremented by 1
        int modCountAfter = getModCount(map);
        assertEquals(modCountBefore + 1, modCountAfter);

        // header's next and prev should point to header itself
        Object header = getHeader(map);
        Object next = getField(header, "next");
        Object prev = getField(header, "prev");
        assertSame(header, next);
        assertSame(header, prev);

        // map should not contain any keys
        assertFalse(map.containsKey("one"));
        assertFalse(map.containsKey("two"));
        assertFalse(map.containsKey("three"));
    }

    // Helper methods to access private fields via reflection

    private Object getRoot(LinkedTreeMap<?, ?> map) throws Exception {
        return getField(map, "root");
    }

    private int getModCount(LinkedTreeMap<?, ?> map) throws Exception {
        return (int) getField(map, "modCount");
    }

    private Object getHeader(LinkedTreeMap<?, ?> map) throws Exception {
        // header is a final field of type Node<K,V>
        return getField(map, "header");
    }

    private Object getField(Object instance, String fieldName) throws Exception {
        Class<?> clazz = instance.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(instance);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy of " + instance.getClass());
    }
}