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
    // Create instance allowing null values
    mapAllowNull = new LinkedTreeMap<>(null, true);
    // Create instance disallowing null values
    mapDisallowNull = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  public void put_nullKey_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> mapAllowNull.put(null, "value"));
    assertEquals("key == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void put_nullValueWhenNotAllowed_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> mapDisallowNull.put("key", null));
    assertEquals("value == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void put_nullValueWhenAllowed_insertsValue() {
    String previous = mapAllowNull.put("key", null);
    assertNull(previous);
    assertNull(mapAllowNull.get("key"));
  }

  @Test
    @Timeout(8000)
  public void put_newKey_insertsValue() {
    String previous = mapAllowNull.put("key1", "value1");
    assertNull(previous);
    assertEquals("value1", mapAllowNull.get("key1"));
  }

  @Test
    @Timeout(8000)
  public void put_existingKey_updatesValue() {
    mapAllowNull.put("key1", "value1");
    String previous = mapAllowNull.put("key1", "value2");
    assertEquals("value1", previous);
    assertEquals("value2", mapAllowNull.get("key1"));
  }

  @Test
    @Timeout(8000)
  public void put_invokesFindWithCreateTrue() throws Exception {
    // Spy on LinkedTreeMap to verify find called with create=true
    LinkedTreeMap<String, String> spyMap = spy(new LinkedTreeMap<>(null, true));

    // Use reflection to access private find method
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // Stub find to call real method but track invocation
    doAnswer(invocation -> {
      Object key = invocation.getArgument(0);
      boolean create = invocation.getArgument(1);
      return findMethod.invoke(invocation.getMock(), key, create);
    }).when(spyMap).find(any(), anyBoolean());

    // Call put
    spyMap.put("key", "value");

    // Verify find called with create=true
    verify(spyMap).find(eq("key"), eq(true));
  }
}