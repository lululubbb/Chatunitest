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

class LinkedTreeMapClearTest {

    private LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void testClearOnEmptyMap() throws Exception {
        // Initially empty
        assertEquals(0, map.size());

        // Access header and modCount before clear
        Field headerField = LinkedTreeMap.class.getDeclaredField("header");
        headerField.setAccessible(true);
        Object header = headerField.get(map);
        Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        int modCountBefore = modCountField.getInt(map);

        // Access header.next and header.prev before clear
        Field nextField = header.getClass().getDeclaredField("next");
        nextField.setAccessible(true);
        Object nextBefore = nextField.get(header);
        Field prevField = header.getClass().getDeclaredField("prev");
        prevField.setAccessible(true);
        Object prevBefore = prevField.get(header);

        map.clear();

        // After clear size is zero
        assertEquals(0, map.size());

        // root is null
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        assertNull(rootField.get(map));

        // modCount incremented by 1
        int modCountAfter = modCountField.getInt(map);
        assertEquals(modCountBefore + 1, modCountAfter);

        // header.next and header.prev point to header itself
        Object nextAfter = nextField.get(header);
        Object prevAfter = prevField.get(header);
        assertSame(header, nextAfter);
        assertSame(header, prevAfter);

        // Before clear header.next and header.prev should not both be header (usually)
        // But since map is empty, they might already be header, so no assert here
    }

    @Test
    @Timeout(8000)
    void testClearOnNonEmptyMap() throws Exception {
        // Put some entries
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");

        assertEquals(3, map.size());

        // Access header and modCount before clear
        Field headerField = LinkedTreeMap.class.getDeclaredField("header");
        headerField.setAccessible(true);
        Object header = headerField.get(map);
        Field modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        int modCountBefore = modCountField.getInt(map);

        // Access header.next and header.prev before clear
        Field nextField = header.getClass().getDeclaredField("next");
        nextField.setAccessible(true);
        Object nextBefore = nextField.get(header);
        Field prevField = header.getClass().getDeclaredField("prev");
        prevField.setAccessible(true);
        Object prevBefore = prevField.get(header);

        // root not null before clear
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Object rootBefore = rootField.get(map);
        assertNotNull(rootBefore);

        map.clear();

        // After clear size is zero
        assertEquals(0, map.size());

        // root is null
        Object rootAfter = rootField.get(map);
        assertNull(rootAfter);

        // modCount incremented by 1
        int modCountAfter = modCountField.getInt(map);
        assertEquals(modCountBefore + 1, modCountAfter);

        // header.next and header.prev point to header itself
        Object nextAfter = nextField.get(header);
        Object prevAfter = prevField.get(header);
        assertSame(header, nextAfter);
        assertSame(header, prevAfter);
    }

}