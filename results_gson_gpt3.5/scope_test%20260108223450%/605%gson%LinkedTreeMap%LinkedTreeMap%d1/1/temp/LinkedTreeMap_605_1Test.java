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
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class LinkedTreeMap_605_1Test {

  @Test
    @Timeout(8000)
  public void testConstructor_default() throws Exception {
    // Use reflection to invoke public LinkedTreeMap() constructor
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor();
    LinkedTreeMap<?, ?> map = constructor.newInstance();

    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_allowNullValuesTrue() throws Exception {
    // Use reflection to invoke public LinkedTreeMap(boolean) constructor
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    LinkedTreeMap<?, ?> map = constructor.newInstance(true);

    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_allowNullValuesFalse() throws Exception {
    // Use reflection to invoke public LinkedTreeMap(boolean) constructor
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    LinkedTreeMap<?, ?> map = constructor.newInstance(false);

    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_comparatorAndAllowNullValues() throws Exception {
    // Use reflection to invoke public LinkedTreeMap(Comparator<? super K>, boolean) constructor
    Comparator<String> comparator = String::compareTo;
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(Comparator.class, boolean.class);
    LinkedTreeMap<String, String> map = constructor.newInstance(comparator, true);

    assertNotNull(map);
    assertEquals(0, map.size());
  }
}