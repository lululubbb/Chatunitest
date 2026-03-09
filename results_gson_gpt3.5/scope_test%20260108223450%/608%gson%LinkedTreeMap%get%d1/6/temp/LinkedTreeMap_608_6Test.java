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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedTreeMap_608_6Test {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    map = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  public void testGet_existingKey_returnsValue() {
    map.put("key1", "value1");
    String result = map.get("key1");
    assertEquals("value1", result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nonExistingKey_returnsNull() {
    String result = map.get("nonExistingKey");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_whenAllowed_returnsValue() {
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(null, false);
    // Use reflection to set allowNullValues field to true
    try {
      Field allowNullValuesField = LinkedTreeMap.class.getDeclaredField("allowNullValues");
      allowNullValuesField.setAccessible(true);
      allowNullValuesField.set(mapAllowNull, true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // Use reflection to set comparator to allow null keys (set to Comparator that allows null)
    try {
      Field comparatorField = LinkedTreeMap.class.getDeclaredField("comparator");
      comparatorField.setAccessible(true);
      comparatorField.set(mapAllowNull, (Comparator<String>) (a, b) -> {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    mapAllowNull.put(null, "nullValue");
    String result = mapAllowNull.get(null);
    assertEquals("nullValue", result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_whenNotAllowed_returnsNull() {
    String result = map.get(null);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_privateFindByObjectMethod_withExistingNode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    map.put("key2", "value2");
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    Object node = findByObject.invoke(map, "key2");
    assertNotNull(node);
    // The Node class is package-private, check field 'value' via reflection
    Field valueField = node.getClass().getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(node);
    assertEquals("value2", value);
  }

  @Test
    @Timeout(8000)
  public void testGet_privateFindByObjectMethod_withNonExistingNode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    Object node = findByObject.invoke(map, "nonExistingKey");
    assertNull(node);
  }

  @Test
    @Timeout(8000)
  public void testGet_privateFindByObjectMethod_withNullKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(null, false);
    // Use reflection to set allowNullValues field to true
    try {
      Field allowNullValuesField = LinkedTreeMap.class.getDeclaredField("allowNullValues");
      allowNullValuesField.setAccessible(true);
      allowNullValuesField.set(mapAllowNull, true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // Use reflection to set comparator to allow null keys (set to Comparator that allows null)
    try {
      Field comparatorField = LinkedTreeMap.class.getDeclaredField("comparator");
      comparatorField.setAccessible(true);
      comparatorField.set(mapAllowNull, (Comparator<String>) (a, b) -> {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    mapAllowNull.put(null, "nullValue");
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    Object node = findByObject.invoke(mapAllowNull, (Object) null);
    assertNotNull(node);
    Field valueField = node.getClass().getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(node);
    assertEquals("nullValue", value);
  }
}