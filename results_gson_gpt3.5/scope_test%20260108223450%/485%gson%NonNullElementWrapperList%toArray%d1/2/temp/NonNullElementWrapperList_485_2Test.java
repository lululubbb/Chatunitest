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

class NonNullElementWrapperList_485_2Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void toArray_delegatesToDelegateToArray() {
    Object[] expected = new Object[] {"a", "b"};
    when(delegate.toArray()).thenReturn(expected);

    Object[] actual = list.toArray();

    verify(delegate).toArray();
    assertSame(expected, actual);
  }

  @Test
    @Timeout(8000)
  void toArray_reflectiveInvocation() throws Exception {
    Object[] expected = new Object[] {"x", "y"};
    when(delegate.toArray()).thenReturn(expected);

    Method toArrayMethod = NonNullElementWrapperList.class.getDeclaredMethod("toArray");
    toArrayMethod.setAccessible(true);

    Object[] actual = (Object[]) toArrayMethod.invoke(list);

    verify(delegate).toArray();
    assertSame(expected, actual);
  }
}