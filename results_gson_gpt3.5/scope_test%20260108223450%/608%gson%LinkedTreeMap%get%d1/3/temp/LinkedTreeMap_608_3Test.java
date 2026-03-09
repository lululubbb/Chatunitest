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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkedTreeMap_608_3Test {

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
    String result = map.get("nonExisting");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_returnsValueIfNullAllowed() throws Exception {
    // Use reflection to instantiate LinkedTreeMap with (Comparator, boolean) constructor
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(java.util.Comparator.class, boolean.class);
    constructor.setAccessible(true);
    LinkedTreeMap<String, String> mapWithNull = constructor.newInstance(null, true);

    // Put a non-null key first to initialize tree structure
    mapWithNull.put("initKey", "initValue");

    // Remove the existing key to allow null key insertion without conflict
    mapWithNull.remove("initKey");

    // Put null key only if null keys are allowed (which they are here)
    // Use reflection to bypass possible null check in put method
    Method putMethod = LinkedTreeMap.class.getDeclaredMethod("put", Object.class, Object.class);
    putMethod.setAccessible(true);
    putMethod.invoke(mapWithNull, null, "nullValue");

    String result = mapWithNull.get(null);
    assertEquals("nullValue", result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_returnsNullIfNullNotAllowed() throws Exception {
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(java.util.Comparator.class, boolean.class);
    constructor.setAccessible(true);
    LinkedTreeMap<String, String> mapNoNull = constructor.newInstance(null, false);

    // Since null keys are not allowed, put(null, ...) throws NPE.
    // So do not put null key, just test get(null) returns null.
    String result = mapNoNull.get(null);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_privateFindByObject_invokedViaReflection_foundAndNotFound() throws Exception {
    // Put a key-value pair
    map.put("keyReflection", "valueReflection");

    // Access private method findByObject via reflection
    Method findByObject = LinkedTreeMap.class.getDeclaredMethod("findByObject", Object.class);
    findByObject.setAccessible(true);

    // Invoke with existing key
    Object node = findByObject.invoke(map, "keyReflection");
    assertNotNull(node);

    // Invoke with non-existing key
    Object nodeNull = findByObject.invoke(map, "noSuchKey");
    assertNull(nodeNull);
  }
}