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

class NonNullElementWrapperList_486_4Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.spy(new ArrayList<>());
    delegate.add("one");
    delegate.add("two");
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void toArray_givenArray_returnsDelegateToArray() {
    String[] arr = new String[2];
    String[] result = list.toArray(arr);
    assertSame(delegate.toArray(arr), result);
  }

  @Test
    @Timeout(8000)
  void toArray_withSmallerArray_expandsArray() {
    String[] arr = new String[1];
    String[] result = list.toArray(arr);
    assertEquals(2, result.length);
    assertArrayEquals(new String[]{"one", "two"}, result);
  }

  @Test
    @Timeout(8000)
  void toArray_withLargerArray_returnsArrayWithNullPadding() {
    String[] arr = new String[5];
    String[] result = list.toArray(arr);
    assertSame(arr, result);
    assertEquals("one", result[0]);
    assertEquals("two", result[1]);
    for (int i = 2; i < result.length; i++) {
      assertNull(result[i]);
    }
  }

  @Test
    @Timeout(8000)
  void toArray_invokesDelegateToArray_withReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method toArrayMethod = NonNullElementWrapperList.class.getDeclaredMethod("toArray", Object[].class);
    toArrayMethod.setAccessible(true);
    String[] arr = new String[2];
    @SuppressWarnings("unchecked")
    String[] result = (String[]) toArrayMethod.invoke(list, (Object) arr);
    assertSame(delegate.toArray(arr), result);
  }
}