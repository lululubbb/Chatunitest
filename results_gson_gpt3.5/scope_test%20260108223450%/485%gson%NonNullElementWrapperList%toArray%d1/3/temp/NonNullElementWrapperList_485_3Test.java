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

class NonNullElementWrapperList_485_3Test {

  private ArrayList<String> mockDelegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    mockDelegate = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  void toArray_delegatesToDelegate() {
    Object[] expected = new Object[] {"a", "b"};
    when(mockDelegate.toArray()).thenReturn(expected);

    Object[] result = list.toArray();

    assertSame(expected, result);
    verify(mockDelegate).toArray();
  }

  @Test
    @Timeout(8000)
  void toArray_reflectionInvoke_privateToArray() throws Exception {
    // Use reflection to invoke the public toArray method (which is not private, but per request)
    Method toArrayMethod = NonNullElementWrapperList.class.getDeclaredMethod("toArray");
    toArrayMethod.setAccessible(true);

    Object[] expected = new Object[] {"x", "y"};
    when(mockDelegate.toArray()).thenReturn(expected);

    Object[] result = (Object[]) toArrayMethod.invoke(list);

    assertSame(expected, result);
    verify(mockDelegate).toArray();
  }
}