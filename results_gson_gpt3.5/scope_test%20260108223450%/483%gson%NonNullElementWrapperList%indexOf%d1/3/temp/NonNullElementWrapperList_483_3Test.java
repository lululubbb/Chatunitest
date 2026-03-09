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

class NonNullElementWrapperList_483_3Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void indexOf_existingElement_returnsIndex() {
    when(delegate.indexOf("test")).thenReturn(3);
    int index = list.indexOf("test");
    assertEquals(3, index);
    verify(delegate).indexOf("test");
  }

  @Test
    @Timeout(8000)
  void indexOf_nonExistingElement_returnsMinusOne() {
    when(delegate.indexOf("absent")).thenReturn(-1);
    int index = list.indexOf("absent");
    assertEquals(-1, index);
    verify(delegate).indexOf("absent");
  }

  @Test
    @Timeout(8000)
  void indexOf_nullElement() {
    when(delegate.indexOf(null)).thenReturn(-1);
    int index = list.indexOf(null);
    assertEquals(-1, index);
    verify(delegate).indexOf(null);
  }

  @Test
    @Timeout(8000)
  void testNonNullPrivateMethod() throws Exception {
    // Using reflection to test private method nonNull(E element)
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String input = "hello";
    Object result = nonNullMethod.invoke(list, input);
    assertEquals(input, result);

    Exception exception = assertThrows(Exception.class, () -> {
      nonNullMethod.invoke(list, (Object) null);
    });
    assertTrue(exception.getCause() instanceof NullPointerException);
  }
}