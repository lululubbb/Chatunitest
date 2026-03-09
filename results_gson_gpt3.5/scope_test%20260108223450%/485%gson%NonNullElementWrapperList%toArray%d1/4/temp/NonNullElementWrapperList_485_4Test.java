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

class NonNullElementWrapperList_485_4Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void toArray_returnsDelegateToArray() {
    delegate.add("a");
    delegate.add("b");
    Object[] array = list.toArray();
    assertArrayEquals(new Object[] { "a", "b" }, array);
  }

  @Test
    @Timeout(8000)
  void toArray_emptyDelegate_returnsEmptyArray() {
    Object[] array = list.toArray();
    assertEquals(0, array.length);
  }

  @Test
    @Timeout(8000)
  void toArray_reflectionInvoke() throws Exception {
    Method toArrayMethod = NonNullElementWrapperList.class.getDeclaredMethod("toArray");
    toArrayMethod.setAccessible(true);
    delegate.add("x");
    Object[] result = (Object[]) toArrayMethod.invoke(list);
    assertArrayEquals(new Object[] { "x" }, result);
  }
}