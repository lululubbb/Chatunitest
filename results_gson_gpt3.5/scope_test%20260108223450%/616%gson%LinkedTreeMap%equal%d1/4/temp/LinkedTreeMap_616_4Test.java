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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class LinkedTreeMapEqualTest {

  private LinkedTreeMap<Object, Object> map;
  private Method equalMethod;

  @BeforeEach
  void setUp() throws Exception {
    map = new LinkedTreeMap<>();
    equalMethod = LinkedTreeMap.class.getDeclaredMethod("equal", Object.class, Object.class);
    equalMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void equal_bothNull_returnsTrue() throws Exception {
    assertTrue((Boolean) equalMethod.invoke(map, (Object) null, (Object) null));
  }

  @Test
    @Timeout(8000)
  void equal_firstNullSecondNonNull_returnsFalse() throws Exception {
    assertFalse((Boolean) equalMethod.invoke(map, (Object) null, "test"));
  }

  @Test
    @Timeout(8000)
  void equal_firstNonNullSecondNull_returnsFalse() throws Exception {
    assertFalse((Boolean) equalMethod.invoke(map, "test", (Object) null));
  }

  @Test
    @Timeout(8000)
  void equal_sameObject_returnsTrue() throws Exception {
    Object obj = new Object();
    assertTrue((Boolean) equalMethod.invoke(map, obj, obj));
  }

  @Test
    @Timeout(8000)
  void equal_equalObjects_returnsTrue() throws Exception {
    String a = new String("abc");
    String b = new String("abc");
    assertTrue((Boolean) equalMethod.invoke(map, a, b));
  }

  @Test
    @Timeout(8000)
  void equal_nonEqualObjects_returnsFalse() throws Exception {
    String a = "abc";
    String b = "def";
    assertFalse((Boolean) equalMethod.invoke(map, a, b));
  }

  @Test
    @Timeout(8000)
  void equal_differentTypesSameToString_returnsFalse() throws Exception {
    Integer a = 123;
    String b = "123";
    assertFalse((Boolean) equalMethod.invoke(map, a, b));
  }
}