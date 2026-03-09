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

class LinkedTreeMap_keySet_Test {

    LinkedTreeMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new LinkedTreeMap<>();
    }

    @Test
    @Timeout(8000)
    void keySet_whenKeySetIsNull_createsAndReturnsKeySet() throws Exception {
        // Use reflection to set the private keySet field to null
        Field keySetField = LinkedTreeMap.class.getDeclaredField("keySet");
        keySetField.setAccessible(true);
        keySetField.set(map, null);

        Set<String> result = map.keySet();

        assertNotNull(result);

        Object keySetFieldValue = keySetField.get(map);
        assertSame(result, keySetFieldValue);
    }

    @Test
    @Timeout(8000)
    void keySet_whenKeySetIsNotNull_returnsExistingKeySet() throws Exception {
        Field keySetField = LinkedTreeMap.class.getDeclaredField("keySet");
        keySetField.setAccessible(true);

        // Initialize keySet by calling keySet() once
        Set<String> firstCall = map.keySet();

        // Explicitly set the keySet field to the firstCall instance
        keySetField.set(map, firstCall);

        Set<String> secondCall = map.keySet();

        assertSame(firstCall, secondCall);
    }
}