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

public class LinkedTreeMap_608_2Test {

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
    map.put("key1", "value1");
    String result = map.get("key2");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKeyWhenAllowed_returnsValue() throws Exception {
    // Access private static final Comparator<Comparable> NATURAL_ORDER via reflection
    Field naturalOrderField = LinkedTreeMap.class.getDeclaredField("NATURAL_ORDER");
    naturalOrderField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Comparator<? super String> naturalOrder = (Comparator<? super String>) naturalOrderField.get(null);

    // Use constructor with comparator and allowNullValues = true
    LinkedTreeMap<String, String> mapAllowNull = new LinkedTreeMap<>(naturalOrder, true);
    mapAllowNull.put(null, "nullValue");
    String result = mapAllowNull.get(null);
    assertEquals("nullValue", result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKeyWhenNotAllowed_returnsNull() {
    String result = map.get(null);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_privateFindByObjectMethod_invokedViaReflection_existingKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    map.put("key1", "value1");
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) findByObject.invoke(map, "key1");
    assertNotNull(node);
    assertEquals("value1", node.value);
  }

  @Test
    @Timeout(8000)
  public void testGet_privateFindByObjectMethod_invokedViaReflection_nonExistingKey() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap.Node<String, String> node = (LinkedTreeMap.Node<String, String>) findByObject.invoke(map, "nonexistent");
    assertNull(node);
  }
}