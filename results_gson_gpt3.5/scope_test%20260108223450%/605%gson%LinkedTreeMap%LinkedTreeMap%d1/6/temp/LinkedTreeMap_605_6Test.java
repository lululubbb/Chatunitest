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
import java.util.Set;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_605_6Test {

    LinkedTreeMap<String, String> mapDefault;
    LinkedTreeMap<String, String> mapAllowNull;
    LinkedTreeMap<String, String> mapCustomComparator;

    @BeforeEach
    void setUp() {
        // Using default constructor (assumes natural order comparator, allowNullValues = false)
        mapDefault = new LinkedTreeMap<>();

        // Using constructor with allowNullValues true
        try {
            Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
            ctor.setAccessible(true);
            mapAllowNull = ctor.newInstance(true);
        } catch (Exception e) {
            fail("Failed to instantiate LinkedTreeMap with allowNullValues constructor: " + e);
        }

        // Using constructor with custom comparator and allowNullValues false
        Comparator<String> reverseComparator = Comparator.reverseOrder();
        try {
            Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(Comparator.class, boolean.class);
            ctor.setAccessible(true);
            mapCustomComparator = ctor.newInstance(reverseComparator, false);
        } catch (Exception e) {
            fail("Failed to instantiate LinkedTreeMap with comparator constructor: " + e);
        }
    }

    @Test
    @Timeout(8000)
    void testConstructor_default() {
        assertNotNull(mapDefault);
        assertEquals(0, mapDefault.size());
    }

    @Test
    @Timeout(8000)
    void testConstructor_allowNullValuesTrue() {
        assertNotNull(mapAllowNull);
        assertEquals(0, mapAllowNull.size());
    }

    @Test
    @Timeout(8000)
    void testConstructor_customComparator() {
        assertNotNull(mapCustomComparator);
        assertEquals(0, mapCustomComparator.size());
    }

    @Test
    @Timeout(8000)
    void testPutAndGet() {
        mapDefault.put("a", "valueA");
        mapDefault.put("b", "valueB");
        assertEquals("valueA", mapDefault.get("a"));
        assertEquals("valueB", mapDefault.get("b"));
        assertNull(mapDefault.get("c"));
    }

    @Test
    @Timeout(8000)
    void testPutNullValue_allowed() {
        mapAllowNull.put("key", null);
        assertTrue(mapAllowNull.containsKey("key"));
        assertNull(mapAllowNull.get("key"));
    }

    @Test
    @Timeout(8000)
    void testPutNullValue_notAllowed() {
        assertThrows(NullPointerException.class, () -> mapDefault.put("key", null));
    }

    @Test
    @Timeout(8000)
    void testContainsKey() {
        mapDefault.put("hello", "world");
        assertTrue(mapDefault.containsKey("hello"));
        assertFalse(mapDefault.containsKey("missing"));
    }

    @Test
    @Timeout(8000)
    void testRemove() {
        mapDefault.put("key1", "val1");
        assertEquals("val1", mapDefault.remove("key1"));
        assertNull(mapDefault.remove("key1"));
        assertFalse(mapDefault.containsKey("key1"));
    }

    @Test
    @Timeout(8000)
    void testClear() {
        mapDefault.put("k1", "v1");
        mapDefault.put("k2", "v2");
        assertEquals(2, mapDefault.size());
        mapDefault.clear();
        assertEquals(0, mapDefault.size());
        assertFalse(mapDefault.containsKey("k1"));
    }

    @Test
    @Timeout(8000)
    void testSizeAndModCount() throws Exception {
        // size and modCount fields are package-private, access via reflection
        mapDefault.put("x", "1");
        mapDefault.put("y", "2");
        assertEquals(2, mapDefault.size());

        var sizeField = LinkedTreeMap.class.getDeclaredField("size");
        sizeField.setAccessible(true);
        int size = (int) sizeField.get(mapDefault);
        assertEquals(2, size);

        var modCountField = LinkedTreeMap.class.getDeclaredField("modCount");
        modCountField.setAccessible(true);
        int modCount = (int) modCountField.get(mapDefault);
        assertTrue(modCount >= 2);
    }

    @Test
    @Timeout(8000)
    void testFindAndFindByObject() throws Exception {
        // find is package-private; findByObject is package-private
        // Use reflection to invoke find and findByObject

        Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
        findMethod.setAccessible(true);

        // Insert key "key1"
        Object node1 = findMethod.invoke(mapDefault, "key1", true);
        assertNotNull(node1);

        Method findByObjectMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
        findByObjectMethod.setAccessible(true);

        Object node2 = findByObjectMethod.invoke(mapDefault, "key1");
        assertNotNull(node2);

        // For a key not present and create=false, should return null
        Object node3 = findMethod.invoke(mapDefault, "missing", false);
        assertNull(node3);
    }

    @Test
    @Timeout(8000)
    void testEqualMethod() throws Exception {
        Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
        equalMethod.setAccessible(true);

        assertTrue((Boolean) equalMethod.invoke(mapDefault, null, null));
        assertTrue((Boolean) equalMethod.invoke(mapDefault, "a", "a"));
        assertFalse((Boolean) equalMethod.invoke(mapDefault, "a", "b"));
        assertFalse((Boolean) equalMethod.invoke(mapDefault, null, "b"));
        assertFalse((Boolean) equalMethod.invoke(mapDefault, "a", null));
    }

    @Test
    @Timeout(8000)
    void testRemoveInternalAndReplaceInParent() throws Exception {
        Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
        findMethod.setAccessible(true);
        Object node = findMethod.invoke(mapDefault, "keyToRemove", true);
        assertNotNull(node);

        Method removeInternalMethod = LinkedTreeMap.class.getDeclaredMethod("removeInternal", node.getClass(), boolean.class);
        removeInternalMethod.setAccessible(true);

        // Remove node with unlink = true
        removeInternalMethod.invoke(mapDefault, node, true);

        // The node should no longer be found
        Method findByObjectMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
        findByObjectMethod.setAccessible(true);
        Object nodeAfter = findByObjectMethod.invoke(mapDefault, "keyToRemove");
        assertNull(nodeAfter);

        // Test replaceInParent with null replacement
        Method replaceInParentMethod = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", node.getClass(), node.getClass());
        replaceInParentMethod.setAccessible(true);

        // Reinsert node
        Object newNode = findMethod.invoke(mapDefault, "keyToRemove", true);
        assertNotNull(newNode);

        // Replace in parent with null (simulate removal)
        replaceInParentMethod.invoke(mapDefault, newNode, (Object) null);
    }

    @Test
    @Timeout(8000)
    void testRebalanceRotateLeftRotateRight() throws Exception {
        Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
        findMethod.setAccessible(true);

        // Insert multiple keys to create tree structure
        findMethod.invoke(mapDefault, "c", true);
        findMethod.invoke(mapDefault, "b", true);
        findMethod.invoke(mapDefault, "a", true);

        // Get one node for rebalancing (root)
        var rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Object rootNode = rootField.get(mapDefault);
        assertNotNull(rootNode);

        Method rebalanceMethod = LinkedTreeMap.class.getDeclaredMethod("rebalance", rootNode.getClass(), boolean.class);
        rebalanceMethod.setAccessible(true);
        rebalanceMethod.invoke(mapDefault, rootNode, true);

        Method rotateLeftMethod = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", rootNode.getClass());
        rotateLeftMethod.setAccessible(true);
        rotateLeftMethod.invoke(mapDefault, rootNode);

        Method rotateRightMethod = LinkedTreeMap.class.getDeclaredMethod("rotateRight", rootNode.getClass());
        rotateRightMethod.setAccessible(true);
        rotateRightMethod.invoke(mapDefault, rootNode);
    }

    @Test
    @Timeout(8000)
    void testEntrySetAndKeySet() {
        mapDefault.put("k1", "v1");
        mapDefault.put("k2", "v2");

        var entries = mapDefault.entrySet();
        assertNotNull(entries);
        assertEquals(2, entries.size());

        var keys = mapDefault.keySet();
        assertNotNull(keys);
        assertEquals(2, keys.size());
        assertTrue(keys.contains("k1"));
        assertTrue(keys.contains("k2"));
    }

    @Test
    @Timeout(8000)
    void testWriteReplaceAndReadObject() throws Exception {
        // writeReplace is private, returns Object
        Method writeReplaceMethod = LinkedTreeMap.class.getDeclaredMethod("writeReplace");
        writeReplaceMethod.setAccessible(true);
        Object replacement = writeReplaceMethod.invoke(mapDefault);
        assertNotNull(replacement);

        // readObject takes ObjectInputStream, private void
        Method readObjectMethod = LinkedTreeMap.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
        readObjectMethod.setAccessible(true);

        // Use mock ObjectInputStream for testing
        java.io.ObjectInputStream mockIn = mock(java.io.ObjectInputStream.class);
        // No exception expected
        readObjectMethod.invoke(mapDefault, mockIn);
    }
}