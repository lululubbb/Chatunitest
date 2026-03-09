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
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_605_5Test {

    LinkedTreeMap<String, String> mapAllowNull;
    LinkedTreeMap<String, String> mapDisallowNull;
    Comparator<String> reverseComparator = (a, b) -> b.compareTo(a);

    @BeforeEach
    void setUp() {
        mapAllowNull = new LinkedTreeMap<>(true);
        mapDisallowNull = new LinkedTreeMap<>(reverseComparator, false);
    }

    @Test
    @Timeout(8000)
    void testConstructor_defaultAllowNull() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withComparatorAndAllowNull() {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>(reverseComparator, true);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    @Timeout(8000)
    void testPutAndGet() {
        assertNull(mapAllowNull.put("key1", "value1"));
        assertEquals("value1", mapAllowNull.get("key1"));
        assertEquals(1, mapAllowNull.size());

        // Replace existing
        assertEquals("value1", mapAllowNull.put("key1", "value2"));
        assertEquals("value2", mapAllowNull.get("key1"));
        assertEquals(1, mapAllowNull.size());

        // Add more entries
        assertNull(mapAllowNull.put("key2", "value3"));
        assertEquals(2, mapAllowNull.size());
        assertEquals("value3", mapAllowNull.get("key2"));
    }

    @Test
    @Timeout(8000)
    void testPutNullValueAllowed() {
        assertNull(mapAllowNull.put("keyNull", null));
        assertTrue(mapAllowNull.containsKey("keyNull"));
        assertNull(mapAllowNull.get("keyNull"));
    }

    @Test
    @Timeout(8000)
    void testPutNullValueNotAllowed() {
        assertThrows(NullPointerException.class, () -> mapDisallowNull.put("keyNull", null));
    }

    @Test
    @Timeout(8000)
    void testContainsKey() {
        mapAllowNull.put("a", "1");
        assertTrue(mapAllowNull.containsKey("a"));
        assertFalse(mapAllowNull.containsKey("b"));
    }

    @Test
    @Timeout(8000)
    void testRemove() {
        mapAllowNull.put("removeKey", "toRemove");
        assertEquals("toRemove", mapAllowNull.remove("removeKey"));
        assertNull(mapAllowNull.get("removeKey"));
        assertFalse(mapAllowNull.containsKey("removeKey"));
        assertEquals(0, mapAllowNull.size());

        // Remove non-existing key returns null
        assertNull(mapAllowNull.remove("nonExisting"));
    }

    @Test
    @Timeout(8000)
    void testClear() {
        mapAllowNull.put("k1", "v1");
        mapAllowNull.put("k2", "v2");
        assertEquals(2, mapAllowNull.size());
        mapAllowNull.clear();
        assertEquals(0, mapAllowNull.size());
        assertFalse(mapAllowNull.containsKey("k1"));
        assertFalse(mapAllowNull.containsKey("k2"));
    }

    @Test
    @Timeout(8000)
    void testSize() {
        assertEquals(0, mapAllowNull.size());
        mapAllowNull.put("x", "y");
        assertEquals(1, mapAllowNull.size());
        mapAllowNull.remove("x");
        assertEquals(0, mapAllowNull.size());
    }

    @Test
    @Timeout(8000)
    void testEntrySetAndKeySet() {
        mapAllowNull.put("key1", "val1");
        mapAllowNull.put("key2", "val2");

        assertEquals(2, mapAllowNull.entrySet().size());
        assertEquals(2, mapAllowNull.keySet().size());
        assertTrue(mapAllowNull.keySet().contains("key1"));
        assertTrue(mapAllowNull.keySet().contains("key2"));
    }

    @Test
    @Timeout(8000)
    void testFindPrivateMethodViaReflection() throws Exception {
        // Use reflection to invoke private find(K key, boolean create)
        var method = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
        method.setAccessible(true);

        // Should return null when map empty and create == false
        Object result = method.invoke(mapAllowNull, "anyKey", false);
        assertNull(result);

        // Should create node when create == true
        Object node = method.invoke(mapAllowNull, "createdKey", true);
        assertNotNull(node);
        assertEquals(1, mapAllowNull.size());

        // Find existing node with create false returns non-null
        Object found = method.invoke(mapAllowNull, "createdKey", false);
        assertNotNull(found);
        assertEquals(node, found);
    }

    @Test
    @Timeout(8000)
    void testEqualPrivateMethod() throws Exception {
        var method = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
        method.setAccessible(true);

        assertTrue((Boolean) method.invoke(mapAllowNull, null, null));
        assertFalse((Boolean) method.invoke(mapAllowNull, null, "a"));
        assertFalse((Boolean) method.invoke(mapAllowNull, "a", null));
        assertTrue((Boolean) method.invoke(mapAllowNull, "abc", "abc"));
        assertFalse((Boolean) method.invoke(mapAllowNull, "abc", "def"));
    }

    @Test
    @Timeout(8000)
    void testRemoveInternalByKey() throws Exception {
        mapAllowNull.put("a", "1");
        var method = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
        method.setAccessible(true);

        Object removedNode = method.invoke(mapAllowNull, "a");
        assertNotNull(removedNode);
        assertEquals(0, mapAllowNull.size());

        // Removing non-existing key returns null
        Object nullNode = method.invoke(mapAllowNull, "nonExistent");
        assertNull(nullNode);
    }

    @Test
    @Timeout(8000)
    void testReplaceInParentAndRebalanceRotate() throws Exception {
        // Insert multiple keys to cause tree to have nodes
        mapAllowNull.put("c", "3");
        mapAllowNull.put("a", "1");
        mapAllowNull.put("b", "2");
        mapAllowNull.put("d", "4");
        mapAllowNull.put("e", "5");

        // Access private methods replaceInParent, rebalance, rotateLeft, rotateRight
        var replaceInParent = LinkedTreeMap.class.getDeclaredMethod("replaceInParent", LinkedTreeMap.Node.class, LinkedTreeMap.Node.class);
        replaceInParent.setAccessible(true);

        var rebalance = LinkedTreeMap.class.getDeclaredMethod("rebalance", LinkedTreeMap.Node.class, boolean.class);
        rebalance.setAccessible(true);

        var rotateLeft = LinkedTreeMap.class.getDeclaredMethod("rotateLeft", LinkedTreeMap.Node.class);
        rotateLeft.setAccessible(true);

        var rotateRight = LinkedTreeMap.class.getDeclaredMethod("rotateRight", LinkedTreeMap.Node.class);
        rotateRight.setAccessible(true);

        // Get root node field
        var rootField = LinkedTreeMap.class.getDeclaredField("root");
        rootField.setAccessible(true);
        Object root = rootField.get(mapAllowNull);

        // Call rebalance with insert true
        rebalance.invoke(mapAllowNull, root, true);

        // Call rotateLeft and rotateRight on root
        rotateLeft.invoke(mapAllowNull, root);
        rotateRight.invoke(mapAllowNull, root);

        // Call replaceInParent with root and null replacement
        replaceInParent.invoke(mapAllowNull, root, null);
    }

    @Test
    @Timeout(8000)
    void testConstructorReflection() throws Exception {
        // Use reflection to create instance with Comparator and allowNullValues false
        Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(Comparator.class, boolean.class);
        constructor.setAccessible(true);
        LinkedTreeMap<String, String> map = constructor.newInstance(reverseComparator, false);
        assertNotNull(map);
        assertEquals(0, map.size());
    }
}