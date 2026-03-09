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
import static org.mockito.Mockito.*;

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
    void keySet_shouldReturnNewKeySet_whenKeySetIsNull() throws Exception {
        Field keySetField = LinkedTreeMap.class.getDeclaredField("keySet");
        keySetField.setAccessible(true);
        keySetField.set(map, null);

        Set<String> result = map.keySet();

        assertNotNull(result, "keySet() should not return null");
        Object cachedKeySet = keySetField.get(map);
        assertSame(result, cachedKeySet, "keySet() should cache the created KeySet instance");
    }

    @Test
    @Timeout(8000)
    void keySet_shouldReturnCachedKeySet_whenKeySetIsNotNull() throws Exception {
        Field keySetField = LinkedTreeMap.class.getDeclaredField("keySet");
        keySetField.setAccessible(true);

        // Create a KeySet instance via calling keySet() once to get a real KeySet
        Set<String> realKeySet = map.keySet();

        keySetField.set(map, realKeySet);

        Set<String> result = map.keySet();

        assertSame(realKeySet, result, "keySet() should return the cached KeySet instance");
    }
}