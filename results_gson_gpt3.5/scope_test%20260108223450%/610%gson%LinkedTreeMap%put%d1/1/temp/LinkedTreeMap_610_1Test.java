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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapPutTest {

  private LinkedTreeMap<String, String> mapAllowNull;
  private LinkedTreeMap<String, String> mapDisallowNull;

  @BeforeEach
  public void setUp() {
    // Using default constructor (assumes allowNullValues = false)
    mapDisallowNull = new LinkedTreeMap<>();
    // Using constructor with allowNullValues = true
    mapAllowNull = new LinkedTreeMap<>(null, true);
  }

  @Test
    @Timeout(8000)
  public void put_nullKey_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> {
      mapDisallowNull.put(null, "value");
    });
    assertEquals("key == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void put_nullValue_disallowNull_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> {
      mapDisallowNull.put("key", null);
    });
    assertEquals("value == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void put_nullValue_allowNull_acceptsNull() {
    String previous = mapAllowNull.put("key", null);
    assertNull(previous);
    assertNull(mapAllowNull.get("key"));
  }

  @Test
    @Timeout(8000)
  public void put_newKey_returnsNullAndInserts() {
    String previous = mapDisallowNull.put("key1", "value1");
    assertNull(previous);
    assertEquals("value1", mapDisallowNull.get("key1"));
  }

  @Test
    @Timeout(8000)
  public void put_existingKey_returnsOldValueAndUpdates() {
    mapDisallowNull.put("key1", "value1");
    String previous = mapDisallowNull.put("key1", "value2");
    assertEquals("value1", previous);
    assertEquals("value2", mapDisallowNull.get("key1"));
  }

  @Test
    @Timeout(8000)
  public void put_multipleEntries_sizeIncrements() {
    assertEquals(0, mapDisallowNull.size());
    mapDisallowNull.put("a", "1");
    assertEquals(1, mapDisallowNull.size());
    mapDisallowNull.put("b", "2");
    assertEquals(2, mapDisallowNull.size());
    mapDisallowNull.put("a", "3"); // update existing key
    assertEquals(2, mapDisallowNull.size());
  }

  @Test
    @Timeout(8000)
  public void put_invokesFindWithCreateTrue() throws Exception {
    LinkedTreeMap<String, String> spyMap = spy(new LinkedTreeMap<>());
    // Use reflection to access private find method
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // When find is called with create=true, call real method
    doCallRealMethod().when(spyMap).put(anyString(), anyString());

    // Call put
    spyMap.put("key", "value");

    // Verify find called with create=true
    verify(spyMap).find("key", true);
  }

  @Test
    @Timeout(8000)
  public void put_reflectionInvokePrivateFind() throws Exception {
    // Test coverage: invoke private find method via reflection with create=true and false
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // create=true, key not present, should create new node
    Object nodeCreated = findMethod.invoke(mapDisallowNull, "keyReflection", true);
    assertNotNull(nodeCreated);

    // create=false, key not present, should return null
    Object nodeNotFound = findMethod.invoke(mapDisallowNull, "absentKey", false);
    assertNull(nodeNotFound);
  }
}