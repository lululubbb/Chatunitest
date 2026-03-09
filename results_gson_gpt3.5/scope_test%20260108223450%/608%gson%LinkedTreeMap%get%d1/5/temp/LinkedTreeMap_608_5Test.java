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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedTreeMap_608_5Test {

  private LinkedTreeMap<String, String> map;

  @BeforeEach
  public void setUp() {
    // Using default constructor, allows null values false by default
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
    map.put("key1", "value1");
    String result = map.get("key2");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_whenAllowed_returnsValue() throws Exception {
    // Create map allowing null values and with a comparator that can handle null keys
    Comparator<String> nullSafeComparator = (k1, k2) -> {
      if (k1 == null && k2 == null) return 0;
      if (k1 == null) return -1;
      if (k2 == null) return 1;
      return ((Comparable<String>) k1).compareTo(k2);
    };
    LinkedTreeMap<String, String> nullAllowedMap = new LinkedTreeMap<>(nullSafeComparator, true);

    // Use reflection to forcibly set allowNullValues to true
    Field allowNullValuesField = LinkedTreeMap.class.getDeclaredField("allowNullValues");
    allowNullValuesField.setAccessible(true);
    allowNullValuesField.set(nullAllowedMap, true);

    // Use reflection to forcibly set comparator to the one that handles null keys
    Field comparatorField = LinkedTreeMap.class.getDeclaredField("comparator");
    comparatorField.setAccessible(true);
    comparatorField.set(nullAllowedMap, nullSafeComparator);

    // Put null key and value
    nullAllowedMap.put(null, "nullValue");
    String result = nullAllowedMap.get(null);
    assertEquals("nullValue", result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_whenNotAllowed_returnsNull() {
    // Default map does not allow null keys, get(null) should return null
    String result = map.get(null);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_invokesFindByObjectUsingReflection_existingKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    // Use reflection to invoke private findByObject method
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    map.put("key1", "value1");
    Object node = findByObject.invoke(map, "key1");
    assertNotNull(node);

    // The node returned has a 'value' field equal to "value1"
    Field valueField = node.getClass().getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(node);
    assertEquals("value1", value);
  }

  @Test
    @Timeout(8000)
  public void testGet_invokesFindByObjectUsingReflection_nonExistingKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    Object node = findByObject.invoke(map, "nonexistent");
    assertNull(node);
  }
}