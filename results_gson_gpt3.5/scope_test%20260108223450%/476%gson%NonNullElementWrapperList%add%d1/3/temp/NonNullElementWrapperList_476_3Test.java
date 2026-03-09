package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_476_3Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void add_insertsElementAtIndex() {
    list.add(0, "foo");
    verify(delegate).add(0, "foo");
  }

  @Test
    @Timeout(8000)
  void add_insertsElementAtIndex_multipleElements() {
    list.add(0, "foo");
    list.add(1, "bar");
    list.add(1, "baz");
    assertEquals("foo", delegate.get(0));
    assertEquals("baz", delegate.get(1));
    assertEquals("bar", delegate.get(2));
  }

  @Test
    @Timeout(8000)
  void add_nullElement_throwsNPE() {
    NullPointerException npe = assertThrows(NullPointerException.class, () -> list.add(0, null));
    assertEquals("Element must be non-null", npe.getMessage());
    verify(delegate, never()).add(anyInt(), any());
  }

  @Test
    @Timeout(8000)
  void nonNull_returnsElement_whenNotNull() throws Exception {
    Method nonNull = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNull.setAccessible(true);
    String element = "test";
    Object result = nonNull.invoke(list, element);
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  void nonNull_throwsNPE_whenNull() throws Exception {
    Method nonNull = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNull.setAccessible(true);
    NullPointerException npe = assertThrows(NullPointerException.class, () -> {
      try {
        nonNull.invoke(list, new Object[] {null});
      } catch (Exception e) {
        throw e.getCause();
      }
    });
    assertEquals("Element must be non-null", npe.getMessage());
  }
}