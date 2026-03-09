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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Objects;

public class LinkedTreeMap_616_5Test {

  @Test
    @Timeout(8000)
  void testEqual_bothNull() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(map, null, null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_firstNullSecondNonNull() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(map, null, "abc");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_firstNonNullSecondNull() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(map, "abc", null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_bothNonNullEqualObjects() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    String a = new String("test");
    String b = new String("test");
    boolean result = (boolean) equalMethod.invoke(map, a, b);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_bothNonNullDifferentObjects() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    boolean result = (boolean) equalMethod.invoke(map, "test1", "test2");
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_withDifferentTypes() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Integer i = 123;
    String s = "123";
    boolean result = (boolean) equalMethod.invoke(map, i, s);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testEqual_withSameObject() throws Exception {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    Method equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
    Object obj = new Object();
    boolean result = (boolean) equalMethod.invoke(map, obj, obj);
    assertTrue(result);
  }
}