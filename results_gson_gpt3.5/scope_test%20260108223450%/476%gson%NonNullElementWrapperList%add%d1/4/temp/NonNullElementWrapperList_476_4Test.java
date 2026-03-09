package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_476_4Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void add_insertsElementAtIndex() {
    list.add(0, "test");
    assertEquals(1, list.size());
    assertEquals("test", list.get(0));
    verify(delegate).add(0, "test");
  }

  @Test
    @Timeout(8000)
  void add_nullElement_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> list.add(0, null));
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void nonNull_privateMethod_returnsElementIfNotNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String element = "element";
    Object result = nonNullMethod.invoke(list, element);
    assertEquals(element, result);
  }

  @Test
    @Timeout(8000)
  void nonNull_privateMethod_throwsIfNull() throws NoSuchMethodException {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    assertThrows(InvocationTargetException.class, () -> {
      try {
        nonNullMethod.invoke(list, new Object[] { null });
      } catch (InvocationTargetException e) {
        // The cause should be NullPointerException
        assertTrue(e.getCause() instanceof NullPointerException);
        throw e;
      }
    });
  }
}