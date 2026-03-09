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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapKeySetTest {

    LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void keySet_whenKeySetIsNull_initializesAndReturnsKeySet() throws Exception {
        // Call keySet once to initialize the field
        map.keySet();

        // Using reflection to set keySet field to null to test initialization
        Field keySetField = LinkedTreeMap.class.getDeclaredField("keySet");
        keySetField.setAccessible(true);
        keySetField.set(map, null);

        Set<String> result = map.keySet();

        assertNotNull(result, "keySet() should not return null");
        // keySet field should now be assigned
        Object keySetFieldValue = keySetField.get(map);
        assertSame(result, keySetFieldValue, "Returned keySet should be assigned to field");
    }

    @Test
    @Timeout(8000)
    void keySet_whenKeySetIsAlreadySet_returnsExistingKeySet() throws Exception {
        // Prepare a KeySet instance by calling keySet() once
        Set<String> firstCall = map.keySet();

        // Using reflection to get keySet field
        Field keySetField = LinkedTreeMap.class.getDeclaredField("keySet");
        keySetField.setAccessible(true);
        Object keySetFieldValue = keySetField.get(map);

        // Call keySet() again and check that it returns the same instance
        Set<String> secondCall = map.keySet();

        assertSame(keySetFieldValue, secondCall, "keySet() should return the cached KeySet instance");
        assertSame(firstCall, secondCall, "Subsequent calls to keySet() should return the same instance");
    }
}