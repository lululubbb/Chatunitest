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

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class LinkedTreeMapRemoveInternalByKeyTest {

    private LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void testRemoveInternalByKey_NodeFound_RemovesNode() throws Exception {
        // Put a key-value pair so that the node exists
        map.put("key1", "value1");

        // Use reflection to access removeInternalByKey
        Method method = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
        method.setAccessible(true);

        // Call removeInternalByKey with existing key
        @SuppressWarnings("unchecked")
        Node<String, String> removedNode = (Node<String, String>) method.invoke(map, "key1");

        assertNotNull(removedNode);
        assertEquals("key1", removedNode.key);
        assertEquals("value1", removedNode.value);
        // After removal, size should be 0
        assertEquals(0, map.size());
        // The key should no longer be present
        assertFalse(map.containsKey("key1"));
    }

    @Test
    @Timeout(8000)
    void testRemoveInternalByKey_NodeNotFound_ReturnsNull() throws Exception {
        // Use reflection to access removeInternalByKey
        Method method = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
        method.setAccessible(true);

        // Call removeInternalByKey with a key that does not exist
        @SuppressWarnings("unchecked")
        Node<String, String> removedNode = (Node<String, String>) method.invoke(map, "nonexistent");

        assertNull(removedNode);
        // Size should remain 0
        assertEquals(0, map.size());
    }

    @Test
    @Timeout(8000)
    void testRemoveInternalByKey_NullKey() throws Exception {
        // Put a null key if allowed by default constructor (it is allowed)
        map.put(null, "nullValue");

        Method method = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Node<String, String> removedNode = (Node<String, String>) method.invoke(map, (Object) null);

        assertNotNull(removedNode);
        assertNull(removedNode.key);
        assertEquals("nullValue", removedNode.value);
        assertEquals(0, map.size());
        assertFalse(map.containsKey(null));
    }
}