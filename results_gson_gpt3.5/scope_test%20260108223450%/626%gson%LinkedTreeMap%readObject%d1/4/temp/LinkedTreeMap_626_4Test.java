package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
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

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LinkedTreeMap_ReadObjectTest {

  @Test
    @Timeout(8000)
  void readObject_shouldThrowInvalidObjectException() throws Exception {
    LinkedTreeMap<Object, Object> map = new LinkedTreeMap<>();

    Method readObjectMethod = LinkedTreeMap.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObjectMethod.setAccessible(true);

    ObjectInputStream mockInputStream = Mockito.mock(ObjectInputStream.class);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      readObjectMethod.invoke(map, mockInputStream);
    });

    Throwable cause = thrown.getCause();
    if (!(cause instanceof InvalidObjectException)) {
      throw new RuntimeException(cause);
    }
  }
}