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

class NonNullElementWrapperList_485_5Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testToArray_emptyDelegate_returnsEmptyArray() {
    Object[] array = list.toArray();
    assertNotNull(array);
    assertEquals(0, array.length);
  }

  @Test
    @Timeout(8000)
  void testToArray_nonEmptyDelegate_returnsArrayWithSameElements() {
    delegate.add("a");
    delegate.add("b");
    delegate.add("c");
    Object[] array = list.toArray();
    assertArrayEquals(new Object[]{"a", "b", "c"}, array);
  }

  @Test
    @Timeout(8000)
  void testToArray_delegateContainsNullElements() {
    delegate.add(null);
    delegate.add("x");
    Object[] array = list.toArray();
    assertEquals(2, array.length);
    assertNull(array[0]);
    assertEquals("x", array[1]);
  }

  @Test
    @Timeout(8000)
  void testToArray_calledViaReflection_returnsSameAsDirectCall() throws Exception {
    delegate.add("ref");
    Method toArrayMethod = NonNullElementWrapperList.class.getDeclaredMethod("toArray");
    toArrayMethod.setAccessible(true);
    Object[] result = (Object[]) toArrayMethod.invoke(list);
    assertArrayEquals(delegate.toArray(), result);
  }
}