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

class LinkedTreeMapPutTest {

  LinkedTreeMap<String, String> mapAllowNull;
  LinkedTreeMap<String, String> mapDisallowNull;

  @BeforeEach
  void setUp() {
    // allowNullValues = true
    mapAllowNull = new LinkedTreeMap<>(null, true);
    // allowNullValues = false
    mapDisallowNull = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  void put_nullKey_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> mapAllowNull.put(null, "value"));
    assertEquals("key == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void put_nullValueWhenDisallowNull_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> mapDisallowNull.put("key", null));
    assertEquals("value == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void put_nullValueWhenAllowNull_insertsSuccessfully() {
    String previous = mapAllowNull.put("key", null);
    assertNull(previous);
    assertNull(mapAllowNull.get("key"));
  }

  @Test
    @Timeout(8000)
  void put_newKey_returnsNullAndInsertsValue() {
    String previous = mapAllowNull.put("newKey", "newValue");
    assertNull(previous);
    assertEquals("newValue", mapAllowNull.get("newKey"));
  }

  @Test
    @Timeout(8000)
  void put_existingKey_replacesValueAndReturnsOld() {
    mapAllowNull.put("key", "oldValue");
    String previous = mapAllowNull.put("key", "newValue");
    assertEquals("oldValue", previous);
    assertEquals("newValue", mapAllowNull.get("key"));
  }

  @Test
    @Timeout(8000)
  void put_invokesFindWithCreateTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Use reflection to access private find method
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // Call find with create=true before put
    String key = "reflectionKey";
    String value = "reflectionValue";

    Object nodeObj = findMethod.invoke(mapAllowNull, key, true);
    assertNotNull(nodeObj);
    // Node is package-private, so use reflection to check fields
    Class<?> nodeClass = nodeObj.getClass();
    Method getValue = nodeClass.getDeclaredMethod("getValue");
    getValue.setAccessible(true);
    Object nodeValue = getValue.invoke(nodeObj);
    // Initially value is null
    assertNull(nodeValue);

    // Perform put
    String previous = mapAllowNull.put(key, value);
    assertNull(previous);

    // Call find with create=false after put
    Object nodeAfterPut = findMethod.invoke(mapAllowNull, key, false);
    assertNotNull(nodeAfterPut);
    Object updatedValue = getValue.invoke(nodeAfterPut);
    assertEquals(value, updatedValue);
  }
}