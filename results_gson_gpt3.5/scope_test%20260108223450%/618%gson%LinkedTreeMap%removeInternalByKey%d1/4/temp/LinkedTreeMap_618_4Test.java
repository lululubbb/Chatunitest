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
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.LinkedTreeMap.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

class LinkedTreeMap_removeInternalByKey_Test {

    LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        // Use the default constructor which assumes natural ordering and allowNullValues false
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void removeInternalByKey_keyNotFound_returnsNull() throws Exception {
        // key does not exist, so findByObject returns null
        Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
        removeInternalByKey.setAccessible(true);

        Object result = removeInternalByKey.invoke(map, "nonexistent");
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void removeInternalByKey_keyFound_removesNodeAndReturnsNode() throws Exception {
        // We need to add an entry first so that findByObject returns a node
        map.put("key1", "value1");

        // Spy on map to mock findByObject and removeInternal
        LinkedTreeMap<String, String> spyMap = Mockito.spy(map);

        // Obtain the node by calling findByObject normally
        Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
        findByObject.setAccessible(true);
        @SuppressWarnings("unchecked")
        Node<String, String> node = (Node<String, String>) findByObject.invoke(spyMap, "key1");

        // Mock findByObject to return the node only when called with "key1"
        doReturn(node).when(spyMap).findByObject(eq("key1"));

        // Mock removeInternal to do nothing when called with node and true
        doNothing().when(spyMap).removeInternal(eq(node), eq(true));

        // Invoke removeInternalByKey via reflection
        Method removeInternalByKey = LinkedTreeMap.class.getDeclaredMethod("removeInternalByKey", Object.class);
        removeInternalByKey.setAccessible(true);

        @SuppressWarnings("unchecked")
        Node<String, String> result = (Node<String, String>) removeInternalByKey.invoke(spyMap, "key1");

        // Verify removeInternal was called once with node and true
        verify(spyMap, times(1)).removeInternal(node, true);

        // Result should be the node
        assertNotNull(result);
        assertEquals(node, result);
    }
}