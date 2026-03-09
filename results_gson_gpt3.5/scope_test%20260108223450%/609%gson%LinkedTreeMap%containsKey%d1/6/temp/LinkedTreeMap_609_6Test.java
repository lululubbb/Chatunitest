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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapContainsKeyTest {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  void setUp() {
    map = new LinkedTreeMap<>();
  }

  @Test
    @Timeout(8000)
  void containsKey_whenKeyIsPresent_returnsTrue() {
    map.put("key1", "value1");
    assertTrue(map.containsKey("key1"));
  }

  @Test
    @Timeout(8000)
  void containsKey_whenKeyIsAbsent_returnsFalse() {
    map.put("key1", "value1");
    assertFalse(map.containsKey("key2"));
  }

  @Test
    @Timeout(8000)
  void containsKey_whenKeyIsNullAndNotPresent_returnsFalse() {
    assertFalse(map.containsKey(null));
  }

  @Test
    @Timeout(8000)
  void containsKey_whenKeyIsNullAndPresent_returnsTrue() {
    map.put(null, "nullValue");
    assertTrue(map.containsKey(null));
  }

  @Test
    @Timeout(8000)
  void containsKey_withDifferentKeyTypes() {
    LinkedTreeMap<Object, String> objMap = new LinkedTreeMap<>();
    objMap.put("stringKey", "value");
    objMap.put(42, "intValue");
    assertTrue(objMap.containsKey("stringKey"));
    assertTrue(objMap.containsKey(42));
    assertFalse(objMap.containsKey(43));
  }

  @Test
    @Timeout(8000)
  void containsKey_invokesFindByObjectWithReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    map.put("keyX", "valueX");
    Object node = findByObject.invoke(map, "keyX");
    assertNotNull(node);

    Object nodeNull = findByObject.invoke(map, "absentKey");
    assertNull(nodeNull);
  }
}