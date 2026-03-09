package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedTreeMap_608_1Test {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    map = new LinkedTreeMap<>();
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
    String result = map.get("missingKey");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_whenAllowed_returnsValue() {
    // Create the map with a comparator and allowNullValues = true
    LinkedTreeMap<String, String> nullAllowedMap = new LinkedTreeMap<>((Comparator<String>) (a, b) -> {
      if (a == null && b == null) return 0;
      if (a == null) return -1;
      if (b == null) return 1;
      return a.compareTo(b);
    }, true);
    // Use reflection to set allowNullValues field to true (redundant but safe)
    try {
      java.lang.reflect.Field allowNullValuesField = LinkedTreeMap.class.getDeclaredField("allowNullValues");
      allowNullValuesField.setAccessible(true);
      allowNullValuesField.setBoolean(nullAllowedMap, true);

      // Also set the comparator field to a comparator that supports null keys
      java.lang.reflect.Field comparatorField = LinkedTreeMap.class.getDeclaredField("comparator");
      comparatorField.setAccessible(true);
      comparatorField.set(nullAllowedMap, (Comparator<String>) (a, b) -> {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
      });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // Put a non-null key first to initialize internal structure
    nullAllowedMap.put("initKey", "initValue");
    nullAllowedMap.put(null, "nullValue");
    String result = nullAllowedMap.get(null);
    assertEquals("nullValue", result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_whenNotAllowed_returnsNull() {
    LinkedTreeMap<String, String> nullNotAllowedMap = new LinkedTreeMap<>(null, false);
    String result = nullNotAllowedMap.get(null);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_privateFindByObject_invokedViaReflection_existingKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    map.put("key2", "value2");
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) findByObject.invoke(map, "key2");
    assertNotNull(node);
    assertEquals("value2", node.value);
  }

  @Test
    @Timeout(8000)
  public void testGet_privateFindByObject_invokedViaReflection_nonExistingKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) findByObject.invoke(map, "nonexistent");
    assertNull(node);
  }

  @Test
    @Timeout(8000)
  public void testGet_withCustomComparator() {
    LinkedTreeMap<String, String> customCompMap = new LinkedTreeMap<>((a, b) -> b.compareTo(a), false);
    customCompMap.put("a", "valueA");
    customCompMap.put("b", "valueB");
    assertEquals("valueA", customCompMap.get("a"));
    assertEquals("valueB", customCompMap.get("b"));
    assertNull(customCompMap.get("c"));
  }
}