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
    void keySet_returnsNewKeySet_whenNull() throws Exception {
        // Use reflection to set keySet field to null explicitly before test
        setKeySetField(map, null);
        assertNull(getKeySetField(map));

        Set<String> keys = map.keySet();

        assertNotNull(keys);
        // keySet field is now set
        assertSame(keys, getKeySetField(map));
    }

    @Test
    @Timeout(8000)
    void keySet_returnsCachedKeySet_whenNotNull() throws Exception {
        // Call keySet once to initialize keySet field
        Set<String> firstCall = map.keySet();
        assertNotNull(firstCall);

        // Call keySet again, should return same instance
        Set<String> secondCall = map.keySet();
        assertSame(firstCall, secondCall);
    }

    @SuppressWarnings("unchecked")
    private Set<String> getKeySetField(LinkedTreeMap<String, String> map) throws Exception {
        Field field = LinkedTreeMap.class.getDeclaredField("keySet");
        field.setAccessible(true);
        return (Set<String>) field.get(map);
    }

    private void setKeySetField(LinkedTreeMap<String, String> map, Set<String> value) throws Exception {
        Field field = LinkedTreeMap.class.getDeclaredField("keySet");
        field.setAccessible(true);
        field.set(map, value);
    }
}