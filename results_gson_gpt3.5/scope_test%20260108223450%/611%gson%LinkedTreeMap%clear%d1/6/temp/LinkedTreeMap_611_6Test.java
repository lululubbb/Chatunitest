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
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;

public class LinkedTreeMap_611_6Test {

    LinkedTreeMap<String, String> map;

    @BeforeEach
    public void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    public void testClear_onEmptyMap() throws Exception {
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
        Node<?, ?> header = (Node<?, ?>) headerField.get(map);

        Node<?, ?> headerNextBefore = header.next;
        Node<?, ?> headerPrevBefore = header.prev;

        // Call clear
        map.clear();

        Object rootAfter = rootField.get(map);
        int sizeAfter = sizeField.getInt(map);
        int modCountAfter = modCountField.getInt(map);
        Node<?, ?> headerNextAfter = header.next;
        Node<?, ?> headerPrevAfter = header.prev;

        // root should be null
        assertNull(rootAfter);
        // size should be 0
        assertEquals(0, sizeAfter);
        // modCount should be incremented by 1
        assertEquals(modCountBefore + 1, modCountAfter);
        // header.next and header.prev should point to header itself
        assertSame(header, headerNextAfter);
        assertSame(header, headerPrevAfter);
        // Before clear, header.next and header.prev should also be header (empty)
        assertSame(header, headerNextBefore);
        assertSame(header, headerPrevBefore);
    }

    @Test
    @Timeout(8000)
    public void testClear_onNonEmptyMap() throws Exception {
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
        Node<?, ?> header = (Node<?, ?>) headerField.get(map);

        Node<?, ?> headerNextBefore = header.next;
        Node<?, ?> headerPrevBefore = header.prev;

        // The root should not be null before clear
        assertNotNull(rootBefore);
        // The header's next and prev should not be header itself (since map has entries)
        assertNotSame(header, headerNextBefore);
        assertNotSame(header, headerPrevBefore);

        // Call clear
        map.clear();

        Object rootAfter = rootField.get(map);
        int sizeAfter = sizeField.getInt(map);
        int modCountAfter = modCountField.getInt(map);
        Node<?, ?> headerNextAfter = header.next;
        Node<?, ?> headerPrevAfter = header.prev;

        // root should be null after clear
        assertNull(rootAfter);
        // size should be 0 after clear
        assertEquals(0, sizeAfter);
        // modCount should be incremented by 1
        assertEquals(modCountBefore + 1, modCountAfter);
        // header.next and header.prev should point to header itself after clear
        assertSame(header, headerNextAfter);
        assertSame(header, headerPrevAfter);

        // The map size should be 0
        assertEquals(0, map.size());

        // Getting any key should return null
        assertNull(map.get("one"));
        assertNull(map.get("two"));
        assertNull(map.get("three"));
    }
}