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
import org.mockito.Mockito;

class NonNullElementWrapperList_475_2Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testSet_returnsDelegateSetValue() {
    String oldValue = "old";
    String newValue = "new";

    when(delegate.set(1, newValue)).thenReturn(oldValue);

    String result = list.set(1, newValue);

    verify(delegate).set(1, newValue);
    assertEquals(oldValue, result);
  }

  @Test
    @Timeout(8000)
  void testSet_callsNonNull_withNonNullElement() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    String element = "test";

    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    Object nonNullResult = nonNullMethod.invoke(list, element);
    assertEquals(element, nonNullResult);
  }

  @Test
    @Timeout(8000)
  void testSet_callsNonNull_withNullElement_throwsNPE() throws NoSuchMethodException {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    assertThrows(InvocationTargetException.class, () -> {
      try {
        nonNullMethod.invoke(list, (Object) null);
      } catch (InvocationTargetException e) {
        // The cause should be NullPointerException
        assertTrue(e.getCause() instanceof NullPointerException);
        throw e;
      }
    });
  }
}