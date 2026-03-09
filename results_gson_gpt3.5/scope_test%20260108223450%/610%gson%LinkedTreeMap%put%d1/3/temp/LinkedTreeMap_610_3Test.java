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

import java.lang.reflect.Method;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinkedTreeMapPutTest {

  private LinkedTreeMap<String, String> mapAllowNull;
  private LinkedTreeMap<String, String> mapDisallowNull;

  @BeforeEach
  public void setUp() {
    // Create instances with allowNullValues true and false
    mapAllowNull = new LinkedTreeMap<>(null, true);
    mapDisallowNull = new LinkedTreeMap<>(null, false);
  }

  @Test
    @Timeout(8000)
  public void put_nullKey_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> {
      mapAllowNull.put(null, "value");
    });
    assertEquals("key == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void put_nullValue_allowedWhenAllowNullValuesTrue() {
    // Should not throw
    assertDoesNotThrow(() -> {
      String prev = mapAllowNull.put("key", null);
      assertNull(prev);
    });
  }

  @Test
    @Timeout(8000)
  public void put_nullValue_throwsWhenAllowNullValuesFalse() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> {
      mapDisallowNull.put("key", null);
    });
    assertEquals("value == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void put_newKey_returnsNullAndInsertsValue() {
    String prev = mapAllowNull.put("key1", "value1");
    assertNull(prev);
    assertEquals("value1", mapAllowNull.get("key1"));
  }

  @Test
    @Timeout(8000)
  public void put_existingKey_returnsOldValueAndReplaces() {
    mapAllowNull.put("key1", "value1");
    String prev = mapAllowNull.put("key1", "value2");
    assertEquals("value1", prev);
    assertEquals("value2", mapAllowNull.get("key1"));
  }

  @Test
    @Timeout(8000)
  public void put_withCustomComparator() {
    Comparator<String> reverseComparator = (a, b) -> b.compareTo(a);
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(reverseComparator, true);
    String prev = map.put("b", "valueB");
    assertNull(prev);
    prev = map.put("a", "valueA");
    assertNull(prev);

    // Confirm values inserted correctly
    assertEquals("valueB", map.get("b"));
    assertEquals("valueA", map.get("a"));
  }

  @Test
    @Timeout(8000)
  public void put_invokesFindWithCreateTrue() throws Exception {
    // Use reflection to get private find method
    Method findMethod = LinkedTreeMap.class.getDeclaredMethod("find", Object.class, boolean.class);
    findMethod.setAccessible(true);

    // Spy on mapAllowNull to verify find called with create=true
    LinkedTreeMap<String, String> spyMap = spy(mapAllowNull);

    // Call put
    spyMap.put("keySpy", "valueSpy");

    // Verify find called with create = true
    verify(spyMap).find("keySpy", true);

    // Also verify that find returns a Node whose value is replaced
    Object nodeObj = findMethod.invoke(spyMap, "keySpy", true);
    assertNotNull(nodeObj);
    // Node class is package private static nested class, access its value field by reflection
    Class<?> nodeClass = nodeObj.getClass();
    var valueField = nodeClass.getDeclaredField("value");
    valueField.setAccessible(true);
    Object val = valueField.get(nodeObj);
    assertEquals("valueSpy", val);
  }
}