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
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;

class LinkedTreeMap_604_1Test {

  @Test
    @Timeout(8000)
  void testDefaultConstructor() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor();
    ctor.setAccessible(true);
    LinkedTreeMap<?, ?> map = ctor.newInstance();
    assertNotNull(map);
    assertEquals(0, map.size());
    assertFalse(map.containsKey("anyKey"));
  }

  @Test
    @Timeout(8000)
  void testConstructorWithComparatorAndAllowNull() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(Comparator.class, boolean.class);
    ctor.setAccessible(true);
    Comparator<String> comp = String::compareTo;
    LinkedTreeMap<String, String> map = ctor.newInstance(comp, false);
    assertNotNull(map);
    assertEquals(0, map.size());
    assertFalse(map.containsKey("anyKey"));
  }

  @Test
    @Timeout(8000)
  void testConstructorWithAllowNullOnly() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    ctor.setAccessible(true);
    LinkedTreeMap<String, String> map = ctor.newInstance(true);
    assertNotNull(map);
    assertEquals(0, map.size());
    assertFalse(map.containsKey("anyKey"));
  }
}