package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class LinkedTreeMap_626_6Test {

  @Test
    @Timeout(8000)
  void testReadObject_throwsInvalidObjectException() throws Exception {
    LinkedTreeMap<Object, Object> map = new LinkedTreeMap<>();

    ObjectInputStream mockIn = org.mockito.Mockito.mock(ObjectInputStream.class);

    Method readObjectMethod = LinkedTreeMap.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObjectMethod.setAccessible(true);

    assertThrows(InvalidObjectException.class, () -> {
      try {
        readObjectMethod.invoke(map, mockIn);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }
}