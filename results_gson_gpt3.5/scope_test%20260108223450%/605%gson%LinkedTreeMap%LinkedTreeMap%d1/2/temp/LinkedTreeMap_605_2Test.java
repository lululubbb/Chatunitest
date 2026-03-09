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
import java.lang.reflect.InvocationTargetException;

class LinkedTreeMap_605_2Test {

  @Test
    @Timeout(8000)
  void testConstructor_allowNullValuesFalse() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    LinkedTreeMap<?, ?> map = ctor.newInstance(false);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

  @Test
    @Timeout(8000)
  void testConstructor_allowNullValuesTrue() throws Exception {
    Constructor<LinkedTreeMap> ctor = LinkedTreeMap.class.getDeclaredConstructor(boolean.class);
    LinkedTreeMap<?, ?> map = ctor.newInstance(true);
    assertNotNull(map);
    assertEquals(0, map.size());
  }

}