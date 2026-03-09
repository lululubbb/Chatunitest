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

class NonNullElementWrapperList_485_1Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void toArray_shouldReturnDelegateToArrayResult() {
    delegate.add("one");
    delegate.add("two");

    Object[] array = list.toArray();

    assertArrayEquals(delegate.toArray(), array);
    verify(delegate, times(2)).toArray();
  }

  @Test
    @Timeout(8000)
  void toArray_emptyDelegate_returnsEmptyArray() {
    Object[] array = list.toArray();
    assertEquals(0, array.length);
  }

  @Test
    @Timeout(8000)
  void toArray_reflectiveInvocation_returnsSameResult() throws Exception {
    delegate.add("a");
    delegate.add("b");

    Method toArrayMethod = NonNullElementWrapperList.class.getDeclaredMethod("toArray");
    toArrayMethod.setAccessible(true);
    Object[] result = (Object[]) toArrayMethod.invoke(list);

    assertArrayEquals(delegate.toArray(), result);
  }
}