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

class LinkedTreeMap_605_3Test {

  @Test
    @Timeout(8000)
  void testLinkedTreeMap_defaultConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    LinkedTreeMap<?, ?> map = constructor.newInstance();

    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testLinkedTreeMap_allowNullValuesConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    constructor.setAccessible(true);
    LinkedTreeMap<?, ?> mapTrue = constructor.newInstance(true);
    LinkedTreeMap<?, ?> mapFalse = constructor.newInstance(false);

    assertNotNull(mapTrue);
    assertNotNull(mapFalse);
    assertEquals(0, mapTrue.size());
    assertEquals(0, mapFalse.size());
  }

  @Test
    @Timeout(8000)
  void testLinkedTreeMap_comparatorAllowNullConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    @SuppressWarnings("unchecked")
    Comparator<String> comparator = mock(Comparator.class);
    Constructor<LinkedTreeMap> constructor = LinkedTreeMap.class.getDeclaredConstructor(Comparator.class, boolean.class);
    constructor.setAccessible(true);
    LinkedTreeMap<String, String> map = constructor.newInstance(comparator, true);

    assertNotNull(map);
    assertEquals(0, map.size());
  }
}