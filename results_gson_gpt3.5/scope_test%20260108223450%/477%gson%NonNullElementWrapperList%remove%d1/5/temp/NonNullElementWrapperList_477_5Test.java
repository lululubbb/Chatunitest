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
import org.mockito.Mockito;

class NonNullElementWrapperList_477_5Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testRemoveByIndex_returnsElement() {
    when(delegate.remove(0)).thenReturn("removedElement");

    String result = list.remove(0);

    assertEquals("removedElement", result);
    verify(delegate).remove(0);
  }

  @Test
    @Timeout(8000)
  void testRemoveByIndex_delegateThrowsException() {
    when(delegate.remove(0)).thenThrow(new IndexOutOfBoundsException());

    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    verify(delegate).remove(0);
  }

  @Test
    @Timeout(8000)
  void testRemovePrivateNonNullMethod() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    // non-null element returns the same element
    Object result = nonNullMethod.invoke(list, "element");
    assertEquals("element", result);

    // null element throws NullPointerException
    Exception exception = assertThrows(Exception.class, () -> nonNullMethod.invoke(list, new Object[] {null}));
    assertTrue(exception.getCause() instanceof NullPointerException);
  }
}