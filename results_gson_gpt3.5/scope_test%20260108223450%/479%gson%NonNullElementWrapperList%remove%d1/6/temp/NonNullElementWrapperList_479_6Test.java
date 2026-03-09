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

class NonNullElementWrapperList_479_6Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void remove_Object_presentInDelegate_shouldReturnTrue() {
    when(delegate.remove("element")).thenReturn(true);
    boolean result = list.remove("element");
    assertTrue(result);
    verify(delegate).remove("element");
  }

  @Test
    @Timeout(8000)
  void remove_Object_notPresentInDelegate_shouldReturnFalse() {
    when(delegate.remove("missing")).thenReturn(false);
    boolean result = list.remove("missing");
    assertFalse(result);
    verify(delegate).remove("missing");
  }

  @Test
    @Timeout(8000)
  void nonNull_privateMethod_shouldReturnSameElement() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);
    String input = "test";
    @SuppressWarnings("unchecked")
    String result = (String) nonNullMethod.invoke(list, input);
    assertEquals(input, result);
  }
}