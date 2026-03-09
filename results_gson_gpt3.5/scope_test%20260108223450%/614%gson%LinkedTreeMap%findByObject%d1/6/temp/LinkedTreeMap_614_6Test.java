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

class LinkedTreeMap_FindByObjectTest {

    private LinkedTreeMap<String, String> map;

    @BeforeEach
    public void setUp() {
        // Using default constructor (assumes natural ordering)
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    public void testFindByObject_withExistingKey_returnsNode() throws Exception {
        // Put a key-value pair
        map.put("key1", "value1");

        // Access private findByObject via reflection
        Method findByObjectMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
        findByObjectMethod.setAccessible(true);

        // Retrieve node for existing key
        @SuppressWarnings("unchecked")
        Node<String, String> node = (Node<String, String>) findByObjectMethod.invoke(map, "key1");

        assertNotNull(node);
        assertEquals("key1", node.key);
        assertEquals("value1", node.value);
    }

    @Test
    @Timeout(8000)
    public void testFindByObject_withNonExistingKey_returnsNull() throws Exception {
        Method findByObjectMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
        findByObjectMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Node<String, String> node = (Node<String, String>) findByObjectMethod.invoke(map, "nonExistingKey");

        assertNull(node);
    }

    @Test
    @Timeout(8000)
    public void testFindByObject_withNullKey_returnsNull() throws Exception {
        Method findByObjectMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
        findByObjectMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Node<String, String> node = (Node<String, String>) findByObjectMethod.invoke(map, (Object) null);

        assertNull(node);
    }

    @Test
    @Timeout(8000)
    public void testFindByObject_withIncompatibleKeyType_returnsNull() throws Exception {
        // Create a map with natural ordering (String keys)
        LinkedTreeMap<String, String> stringMap = new LinkedTreeMap<>();

        // Put a key-value pair to have some content
        stringMap.put("key1", "value1");

        Method findByObjectMethod = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
        findByObjectMethod.setAccessible(true);

        try {
            @SuppressWarnings("unchecked")
            Node<String, String> node = (Node<String, String>) findByObjectMethod.invoke(stringMap, 123);
            // Should return null if ClassCastException caught inside findByObject
            assertNull(node);
        } catch (Exception e) {
            // unwrap InvocationTargetException and fail if unexpected exception
            Throwable cause = e.getCause();
            if (cause instanceof ClassCastException) {
                fail("ClassCastException should be caught inside findByObject and return null");
            } else {
                throw e;
            }
        }
    }
}