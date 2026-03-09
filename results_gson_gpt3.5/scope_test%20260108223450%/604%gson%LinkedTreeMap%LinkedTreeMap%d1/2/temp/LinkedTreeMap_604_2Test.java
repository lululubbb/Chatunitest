package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ObjectInputStream;
import java.io.InvalidObjectException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_604_2Test {

    LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void testConstructor_Default() {
        LinkedTreeMap<String, String> defaultMap = new LinkedTreeMap<>();
        assertNotNull(defaultMap);
        assertEquals(0, defaultMap.size());
    }

    @Test
    @Timeout(8000)
    void testConstructor_WithComparatorAndAllowNull() {
        Comparator<String> comp = Comparator.reverseOrder();
        LinkedTreeMap<String, String> customMap = new LinkedTreeMap<>(comp, true);
        assertNotNull(customMap);
        assertEquals(0, customMap.size());
    }

    @Test
    @Timeout(8000)
    void testPutAndGet() {
        assertNull(map.put("key1", "value1"));
        assertEquals("value1", map.get("key1"));
        assertEquals(1, map.size());
        // Overwrite value
        assertEquals("value1", map.put("key1", "value2"));
        assertEquals("value2", map.get("key1"));
        assertEquals(1, map.size());
    }

    @Test
    @Timeout(8000)
    void testContainsKey() {
        assertFalse(map.containsKey("key1"));
        map.put("key1", "value1");
        assertTrue(map.containsKey("key1"));
        assertFalse(map.containsKey("key2"));
    }

    @Test
    @Timeout(8000)
    void testRemove() {
        map.put("key1", "value1");
        assertEquals("value1", map.remove("key1"));
        assertNull(map.get("key1"));
        assertNull(map.remove("key1"));
        assertEquals(0, map.size());
    }

    @Test
    @Timeout(8000)
    void testClear() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        assertEquals(2, map.size());
        map.clear();
        assertEquals(0, map.size());
        assertNull(map.get("key1"));
    }

    @Test
    @Timeout(8000)
    void testEntrySetAndKeySet() {
        map.put("key1", "value1");
        map.put("key2", "value2");
        Set<Entry<String, String>> entries = map.entrySet();
        Set<String> keys = map.keySet();
        assertEquals(2, entries.size());
        assertEquals(2, keys.size());
        assertTrue(keys.contains("key1"));
        assertTrue(keys.contains("key2"));
        boolean foundKey1 = false, foundKey2 = false;
        for (Entry<String, String> e : entries) {
            if ("key1".equals(e.getKey()) && "value1".equals(e.getValue())) foundKey1 = true;
            if ("key2".equals(e.getKey()) && "value2".equals(e.getValue())) foundKey2 = true;
        }
        assertTrue(foundKey1);
        assertTrue(foundKey2);
    }

    @Test
    @Timeout(8000)
    void testFindAndFindByObject() throws Exception {
        // Use reflection to access private find(K, boolean)
        Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
        findMethod.setAccessible(true);
        // Initially empty, create=true creates node
        Object node = findMethod.invoke(map, "key1", true);
        assertNotNull(node);
        // create=false finds existing
        Object node2 = findMethod.invoke(map, "key1", false);
        assertSame(node, node2);
        // create=false for non-existing returns null
        Object node3 = findMethod.invoke(map, "key2", false);
        assertNull(node3);

        // findByObject(Object)
        Method findByObjectMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
        findByObjectMethod.setAccessible(true);
        Object foundNode = findByObjectMethod.invoke(map, "key1");
        assertNotNull(foundNode);
        Object notFoundNode = findByObjectMethod.invoke(map, "key2");
        assertNull(notFoundNode);
    }

    @Test
    @Timeout(8000)
    void testFindByEntry() throws Exception {
        map.put("a", "1");
        // Create a Map.Entry to test
        Entry<String, String> entry = new SimpleEntry<>("a", "1");
        Method findByEntryMethod = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Entry.class);
        findByEntryMethod.setAccessible(true);
        Object node = findByEntryMethod.invoke(map, entry);
        assertNotNull(node);
        // Entry not in map
        Entry<String, String> entry2 = new SimpleEntry<>("a", "2");
        Object node2 = findByEntryMethod.invoke(map, entry2);
        assertNull(node2);
    }

    @Test
    @Timeout(8000)
    void testEqualMethod() throws Exception {
        Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
        equalMethod.setAccessible(true);
        assertTrue((Boolean) equalMethod.invoke(map, null, null));
        assertFalse((Boolean) equalMethod.invoke(map, "a", null));
        assertTrue((Boolean) equalMethod.invoke(map, "a", "a"));
        assertFalse((Boolean) equalMethod.invoke(map, "a", "b"));
    }

    @Test
    @Timeout(8000)
    void testRemoveInternalByKey() throws Exception {
        map.put("key1", "value1");
        Method removeInternalByKeyMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
        removeInternalByKeyMethod.setAccessible(true);
        Object node = removeInternalByKeyMethod.invoke(map, "key1");
        assertNotNull(node);
        Object node2 = removeInternalByKeyMethod.invoke(map, "key2");
        assertNull(node2);
    }

    @Test
    @Timeout(8000)
    void testRemoveInternalAndReplaceInParentRebalanceRotate() throws Exception {
        map.put("key1", "value1");
        // Access root node
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Object rootNode = rootField.get(map);
        assertNotNull(rootNode);

        // removeInternal(Node, boolean) is private and has signature removeInternal(Node<K,V>, boolean)
        // But it is package-private in Gson 2.8.9 (and later), so try getDeclaredMethod with parameter types Node and boolean.
        // The Node class is a private static inner class of LinkedTreeMap, so get it via reflection:
        Class<?> nodeClass = null;
        for (Class<?> inner : LinkedTreeMap.class.getDeclaredClasses()) {
            if ("Node".equals(inner.getSimpleName())) {
                nodeClass = inner;
                break;
            }
        }
        assertNotNull(nodeClass);

        Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal", nodeClass, boolean.class);
        removeInternalMethod.setAccessible(true);
        removeInternalMethod.invoke(map, rootNode, true);

        Method replaceInParentMethod = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", nodeClass, nodeClass);
        replaceInParentMethod.setAccessible(true);
        replaceInParentMethod.invoke(map, rootNode, null);

        Method rebalanceMethod = LinkedTreeMap.class.getDeclaredMethod("rebalance", nodeClass, boolean.class);
        rebalanceMethod.setAccessible(true);
        rebalanceMethod.invoke(map, rootNode, true);
        rebalanceMethod.invoke(map, rootNode, false);

        Method rotateLeftMethod = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", nodeClass);
        rotateLeftMethod.setAccessible(true);
        rotateLeftMethod.invoke(map, rootNode);

        Method rotateRightMethod = LinkedTreeMap.class.getDeclaredMethod("rotateRight", nodeClass);
        rotateRightMethod.setAccessible(true);
        rotateRightMethod.invoke(map, rootNode);
    }

    @Test
    @Timeout(8000)
    void testWriteReplace() throws Exception {
        Method writeReplaceMethod = LinkedTreeMap.class.getDeclaredMethod("writeReplace");
        writeReplaceMethod.setAccessible(true);
        Object replacement = writeReplaceMethod.invoke(map);
        assertNotNull(replacement);
    }

    @Test
    @Timeout(8000)
    void testReadObject() throws Exception {
        ObjectInputStream mockIn = mock(ObjectInputStream.class);
        Method readObjectMethod = LinkedTreeMap.class.getDeclaredMethod("readObject", ObjectInputStream.class);
        readObjectMethod.setAccessible(true);
        try {
            readObjectMethod.invoke(map, mockIn);
            fail("Expected InvalidObjectException");
        } catch (Exception e) {
            Throwable cause = e.getCause();
            assertTrue(cause instanceof InvalidObjectException);
            assertEquals("Deserialization is unsupported", cause.getMessage());
        }
    }
}