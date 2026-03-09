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

public class LinkedTreeMapClearTest {

    LinkedTreeMap<String, String> map;

    @BeforeEach
    public void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    public void testClear_onNonEmptyMap_resetsState() throws Exception {
        // Use put to add entries
        map.put("key1", "value1");
        map.put("key2", "value2");

        // Confirm map is not empty before clear
        assertEquals(2, map.size());

        // Access private fields before clear
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Object rootBefore = rootField.get(map);

        Field sizeField = LinkedTreeMap.class.getDeclaredField("size");
        sizeField.setAccessible(true);
        int sizeBefore = sizeField.getInt(map);

        Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        int modCountBefore = modCountField.getInt(map);

        Field headerField = LinkedTreeMap.class.getDeclaredField("header");
        headerField.setAccessible(true);
        Object header = headerField.get(map);

        Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");

        Field nextField = nodeClass.getDeclaredField("next");
        nextField.setAccessible(true);
        Field prevField = nodeClass.getDeclaredField("prev");
        prevField.setAccessible(true);

        Object nextBefore = nextField.get(header);
        Object prevBefore = prevField.get(header);

        assertNotNull(rootBefore);
        assertTrue(sizeBefore > 0);
        assertTrue(modCountBefore >= 0);
        assertNotNull(header);
        assertNotNull(nextBefore);
        assertNotNull(prevBefore);

        // Call clear
        map.clear();

        // After clear, root should be null
        Object rootAfter = rootField.get(map);
        assertNull(rootAfter);

        // size should be 0
        int sizeAfter = sizeField.getInt(map);
        assertEquals(0, sizeAfter);

        // modCount should be incremented by 1
        int modCountAfter = modCountField.getInt(map);
        assertEquals(modCountBefore + 1, modCountAfter);

        // header's next and prev should point to header itself
        Object nextAfter = nextField.get(header);
        Object prevAfter = prevField.get(header);

        assertSame(header, nextAfter);
        assertSame(header, prevAfter);

        // size() method should return 0
        assertEquals(0, map.size());
    }

    @Test
    @Timeout(8000)
    public void testClear_onEmptyMap_keepsConsistentState() throws Exception {
        // Initially empty map
        assertEquals(0, map.size());

        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Object rootBefore = rootField.get(map);
        assertNull(rootBefore);

        Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        int modCountBefore = modCountField.getInt(map);

        Field headerField = LinkedTreeMap.class.getDeclaredField("header");
        headerField.setAccessible(true);
        Object header = headerField.get(map);

        Class<?> nodeClass = Class.forName("com.google.gson.internal.LinkedTreeMap$Node");

        Field nextField = nodeClass.getDeclaredField("next");
        nextField.setAccessible(true);
        Field prevField = nodeClass.getDeclaredField("prev");
        prevField.setAccessible(true);

        Object nextBefore = nextField.get(header);
        Object prevBefore = prevField.get(header);

        // next and prev should point to header itself initially
        assertSame(header, nextBefore);
        assertSame(header, prevBefore);

        // Call clear on empty map
        map.clear();

        // root remains null
        Object rootAfter = rootField.get(map);
        assertNull(rootAfter);

        // modCount increments by 1
        int modCountAfter = modCountField.getInt(map);
        assertEquals(modCountBefore + 1, modCountAfter);

        // header's next and prev remain pointing to header
        Object nextAfter = nextField.get(header);
        Object prevAfter = prevField.get(header);
        assertSame(header, nextAfter);
        assertSame(header, prevAfter);

        // size remains 0
        assertEquals(0, map.size());
    }
}