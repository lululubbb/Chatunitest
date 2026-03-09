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

class NonNullElementWrapperList_483_2Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void indexOf_delegatesToDelegate() {
    when(delegate.indexOf("test")).thenReturn(3);
    int result = list.indexOf("test");
    assertEquals(3, result);
    verify(delegate).indexOf("test");
  }

  @Test
    @Timeout(8000)
  void indexOf_nullElement() {
    when(delegate.indexOf(null)).thenReturn(-1);
    int result = list.indexOf(null);
    assertEquals(-1, result);
    verify(delegate).indexOf(null);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementNotFound() {
    when(delegate.indexOf("absent")).thenReturn(-1);
    int result = list.indexOf("absent");
    assertEquals(-1, result);
    verify(delegate).indexOf("absent");
  }

  @Test
    @Timeout(8000)
  void testNonNullMethodViaReflection_nonNullElement() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);
    String element = "element";
    @SuppressWarnings("unchecked")
    String result = (String) nonNullMethod.invoke(list, element);
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  void testNonNullMethodViaReflection_nullElementThrows() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);
    Exception exception = assertThrows(Exception.class, () -> {
      nonNullMethod.invoke(list, (Object) null);
    });
    // InvocationTargetException is thrown by reflection, check cause is NPE or IllegalArgumentException
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof NullPointerException || cause instanceof IllegalArgumentException);
  }
}