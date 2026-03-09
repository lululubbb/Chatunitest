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
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMap_entrySet_Test {

  LinkedTreeMap<String, Integer> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void entrySet_whenEntrySetIsNull_createsAndReturnsEntrySet() throws Exception {
    // entrySet field is initially null
    Field entrySetField = LinkedTreeMap.class.getDeclaredField("entrySet");
    entrySetField.setAccessible(true);
    assertNull(entrySetField.get(map));

    Set<Entry<String, Integer>> result = map.entrySet();

    assertNotNull(result);
    // The entrySet field should now be assigned the created EntrySet instance
    Object cachedEntrySet = entrySetField.get(map);
    assertSame(result, cachedEntrySet);
  }

  @Test
    @Timeout(8000)
  void entrySet_whenEntrySetNotNull_returnsCachedEntrySet() throws Exception {
    // Create a spy of EntrySet to set as cached entrySet
    // EntrySet is a package-private inner class, so we use reflection to instantiate it
    Class<?> entrySetClass = null;
    for (Class<?> innerClass : LinkedTreeMap.class.getDeclaredClasses()) {
      if ("EntrySet".equals(innerClass.getSimpleName())) {
        entrySetClass = innerClass;
        break;
      }
    }
    assertNotNull(entrySetClass, "EntrySet class should exist");

    // Use reflection to get the constructor and set it accessible
    var constructor = entrySetClass.getDeclaredConstructor(LinkedTreeMap.class);
    constructor.setAccessible(true);
    Object entrySetInstance = constructor.newInstance(map);

    Field entrySetField = LinkedTreeMap.class.getDeclaredField("entrySet");
    entrySetField.setAccessible(true);
    entrySetField.set(map, entrySetInstance);

    Set<Entry<String, Integer>> result = map.entrySet();

    assertSame(entrySetInstance, result);
  }
}