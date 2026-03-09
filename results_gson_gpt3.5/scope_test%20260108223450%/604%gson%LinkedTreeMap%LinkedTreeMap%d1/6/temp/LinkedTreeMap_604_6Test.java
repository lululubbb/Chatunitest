package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class LinkedTreeMap_604_6Test {

    LinkedTreeMap<String, Integer> map;

    @BeforeEach
    public void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NaturalOrder_DefaultAllowNullValues() throws Exception {
        LinkedTreeMap<String, Integer> m = new LinkedTreeMap<>();
        assertNotNull(m);
        assertEquals(0, m.size());
        // comparator field should be NATURAL_ORDER
        Field comparatorField = LinkedTreeMap.class.getDeclaredField("comparator");
        comparatorField.setAccessible(true);
        Comparator<? super String> comparator = (Comparator<? super String>) comparatorField.get(m);
        assertNotNull(comparator);
        assertEquals(0, m.size());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_CustomComparator_AllowNullFalse() throws Exception {
        Comparator<String> comp = (a, b) -> b.compareTo(a);
        LinkedTreeMap<String, Integer> m = new LinkedTreeMap<>(comp, false);
        assertNotNull(m);
        assertEquals(0, m.size());
        Field comparatorField = LinkedTreeMap.class.getDeclaredField("comparator");
        comparatorField.setAccessible(true);
        Comparator<? super String> comparator = (Comparator<? super String>) comparatorField.get(m);
        assertSame(comp, comparator);
        Field allowNullField = LinkedTreeMap.class.getDeclaredField("allowNullValues");
        allowNullField.setAccessible(true);
        assertFalse(allowNullField.getBoolean(m));
    }

    @Test
    @Timeout(8000)
    public void testPutAndGet() {
        assertNull(map.put("one", 1));
        assertEquals(1, map.size());
        assertEquals(1, map.get("one"));
        assertTrue(map.containsKey("one"));
        assertNull(map.get("two"));
    }

    @Test
    @Timeout(8000)
    public void testPutUpdateValue() {
        map.put("key", 10);
        Integer old = map.put("key", 20);
        assertEquals(10, old);
        assertEquals(20, map.get("key"));
        assertEquals(1, map.size());
    }

    @Test
    @Timeout(8000)
    public void testRemoveExistingAndNonExisting() {
        map.put("a", 100);
        map.put("b", 200);
        assertEquals(2, map.size());
        Integer removed = map.remove("a");
        assertEquals(100, removed);
        assertEquals(1, map.size());
        assertNull(map.remove("nonexistent"));
        assertEquals(1, map.size());
    }

    @Test
    @Timeout(8000)
    public void testClear() {
        map.put("x", 1);
        map.put("y", 2);
        assertEquals(2, map.size());
        map.clear();
        assertEquals(0, map.size());
        assertNull(map.get("x"));
    }

    @Test
    @Timeout(8000)
    public void testEntrySetAndKeySet() {
        map.put("k1", 11);
        map.put("k2", 22);
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        assertEquals(2, entries.size());
        Set<String> keys = map.keySet();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("k1"));
        assertTrue(keys.contains("k2"));
    }

    @Test
    @Timeout(8000)
    public void testFindCreateNode() throws Exception {
        Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
        findMethod.setAccessible(true);
        // create new node for key "foo"
        Node<String, Integer> node = (Node<String, Integer>) findMethod.invoke(map, "foo", true);
        assertNotNull(node);
        assertEquals("foo", node.key);
        // find existing node
        Node<String, Integer> node2 = (Node<String, Integer>) findMethod.invoke(map, "foo", false);
        assertSame(node, node2);
        // find non-existing node with create = false returns null
        Node<String, Integer> node3 = (Node<String, Integer>) findMethod.invoke(map, "bar", false);
        assertNull(node3);
    }

    @Test
    @Timeout(8000)
    public void testFindByObject() throws Exception {
        Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
        findByObject.setAccessible(true);
        map.put("a", 1);
        Object result = findByObject.invoke(map, "a");
        assertNotNull(result);
        Object result2 = findByObject.invoke(map, "notpresent");
        assertNull(result2);
        // test with null key if allowed
        LinkedTreeMap<String, Integer> map2 = new LinkedTreeMap<>(null, true);
        result = findByObject.invoke(map2, (Object) null);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRemoveInternalByKey() throws Exception {
        map.put("key", 123);
        Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
        removeInternalByKey.setAccessible(true);
        Object removed = removeInternalByKey.invoke(map, "key");
        assertNotNull(removed);
        assertEquals(0, map.size());
        Object removed2 = removeInternalByKey.invoke(map, "key");
        assertNull(removed2);
    }

    @Test
    @Timeout(8000)
    public void testRemoveInternal() throws Exception {
        map.put("k", 7);
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Node<String, Integer> root = (Node<String, Integer>) rootField.get(map);
        Method removeInternal = LinkedTreeMap.class.getDeclaredMethod("removeInternal", Node.class, boolean.class);
        removeInternal.setAccessible(true);
        removeInternal.invoke(map, root, true);
        assertEquals(0, map.size());
    }

    @Test
    @Timeout(8000)
    public void testReplaceInParent() throws Exception {
        map.put("x", 1);
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Node<String, Integer> root = (Node<String, Integer>) rootField.get(map);
        Method replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", Node.class, Node.class);
        replaceInParent.setAccessible(true);
        // Replace root with null (deletion)
        replaceInParent.invoke(map, root, (Object) null);
        assertNull(rootField.get(map));
        assertEquals(1, map.size()); // size unchanged, because this only replaces links
    }

    @Test
    @Timeout(8000)
    public void testRebalanceRotateLeftRight() throws Exception {
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        Field rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Node<String, Integer> root = (Node<String, Integer>) rootField.get(map);
        Method rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", Node.class, boolean.class);
        rebalance.setAccessible(true);
        Method rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", Node.class);
        rotateLeft.setAccessible(true);
        Method rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", Node.class);
        rotateRight.setAccessible(true);

        // Call rebalance with insert = true
        rebalance.invoke(map, root, true);
        // Call rotateLeft and rotateRight directly
        rotateLeft.invoke(map, root);
        rotateRight.invoke(map, root);
        assertNotNull(rootField.get(map));
    }

    @Test
    @Timeout(8000)
    public void testEqualMethod() throws Exception {
        Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
        equalMethod.setAccessible(true);
        Boolean res1 = (Boolean) equalMethod.invoke(map, "a", "a");
        assertTrue(res1);
        Boolean res2 = (Boolean) equalMethod.invoke(map, "a", "b");
        assertFalse(res2);
        Boolean res3 = (Boolean) equalMethod.invoke(map, null, null);
        assertTrue(res3);
        Boolean res4 = (Boolean) equalMethod.invoke(map, null, "b");
        assertFalse(res4);
    }

    @Test
    @Timeout(8000)
    public void testFindByEntry() throws Exception {
        map.put("key", 42);
        Method findByEntry = LinkedTreeMap.class.getDeclaredMethod("findByEntry", Map.Entry.class);
        findByEntry.setAccessible(true);
        Map.Entry<String, Integer> entry = Map.entry("key", 42);
        Object node = findByEntry.invoke(map, entry);
        assertNotNull(node);
        Map.Entry<String, Integer> entry2 = Map.entry("key", 999);
        Object node2 = findByEntry.invoke(map, entry2);
        assertNull(node2);
    }

    @Test
    @Timeout(8000)
    public void testWriteReplaceAndReadObject() throws Exception {
        Method writeReplace = LinkedTreeMap.class.getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(true);
        Object replacement = writeReplace.invoke(map);
        assertNotNull(replacement);

        Method readObject = LinkedTreeMap.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
        readObject.setAccessible(true);
        // mock ObjectInputStream to simulate deserialization
        java.io.ObjectInputStream mockIn = mock(java.io.ObjectInputStream.class);
        // readObject returns void, just verify no exceptions
        readObject.invoke(map, mockIn);
    }
}