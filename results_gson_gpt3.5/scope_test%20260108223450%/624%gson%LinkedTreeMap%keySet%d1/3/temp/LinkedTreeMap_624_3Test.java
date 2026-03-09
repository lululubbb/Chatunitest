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
  void keySet_whenKeySetIsNull_createsAndReturnsKeySet() throws Exception {
    // Ensure keySet field is null initially via reflection
    Field keySetField = LinkedTreeMap.class.getDeclaredField("keySet");
    keySetField.setAccessible(true);
    keySetField.set(map, null);

    Set<String> returnedKeySet = map.keySet();

    // The returned keySet should not be null
    assertNotNull(returnedKeySet);

    // The keySet field should now be set to the returned KeySet instance
    Object keySetFieldValue = keySetField.get(map);
    assertSame(returnedKeySet, keySetFieldValue);

    // Subsequent call returns the same keySet instance
    Set<String> secondCall = map.keySet();
    assertSame(returnedKeySet, secondCall);
  }

  @Test
    @Timeout(8000)
  void keySet_whenKeySetAlreadySet_returnsExistingKeySet() throws Exception {
    Field keySetField = LinkedTreeMap.class.getDeclaredField("keySet");
    keySetField.setAccessible(true);

    // Initialize keySet by calling keySet()
    Set<String> firstKeySet = map.keySet();

    // Set the keySet field forcibly to the same instance to simulate already set
    keySetField.set(map, firstKeySet);

    Set<String> returned = map.keySet();

    assertSame(firstKeySet, returned);
  }
}