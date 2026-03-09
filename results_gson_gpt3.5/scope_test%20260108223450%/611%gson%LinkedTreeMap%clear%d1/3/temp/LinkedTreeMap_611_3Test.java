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

public class LinkedTreeMap_611_3Test {
    LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void testClear_onEmptyMap() throws Exception {
        // Initially empty map
        assertEquals(0, map.size());

        // Call clear
        map.clear();

        // Verify size is zero
        assertEquals(0, map.size());

        // Verify root is null
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        assertNull(rootField.get(map));

        // Verify modCount incremented (should be 1)
        Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        assertEquals(1, modCountField.getInt(map));

        // Verify header's next and prev point to itself
        Field headerField = LinkedTreeMap.class.getDeclaredField("header");
        headerField.setAccessible(true);
        Object header = headerField.get(map);

        Field nextField = header.getClass().getDeclaredField("next");
        Field prevField = header.getClass().getDeclaredField("prev");
        nextField.setAccessible(true);
        prevField.setAccessible(true);

        Object next = nextField.get(header);
        Object prev = prevField.get(header);

        assertSame(header, next);
        assertSame(header, prev);
    }

    @Test
    @Timeout(8000)
    void testClear_onPopulatedMap() throws Exception {
        // Put some entries
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");

        // Verify size before clear
        assertEquals(3, map.size());

        // Capture modCount before clear
        Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        int modCountBefore = modCountField.getInt(map);

        // Call clear
        map.clear();

        // Verify size is zero
        assertEquals(0, map.size());

        // Verify root is null
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        assertNull(rootField.get(map));

        // Verify modCount incremented by 1
        int modCountAfter = modCountField.getInt(map);
        assertEquals(modCountBefore + 1, modCountAfter);

        // Verify header's next and prev point to itself
        Field headerField = LinkedTreeMap.class.getDeclaredField("header");
        headerField.setAccessible(true);
        Object header = headerField.get(map);

        Field nextField = header.getClass().getDeclaredField("next");
        Field prevField = header.getClass().getDeclaredField("prev");
        nextField.setAccessible(true);
        prevField.setAccessible(true);

        Object next = nextField.get(header);
        Object prev = prevField.get(header);

        assertSame(header, next);
        assertSame(header, prev);
    }
}