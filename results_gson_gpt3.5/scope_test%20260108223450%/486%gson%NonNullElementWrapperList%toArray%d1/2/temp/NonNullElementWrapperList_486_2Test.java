package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NonNullElementWrapperList_486_2Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testToArray_withEmptyArray_returnsEmptyArray() {
    String[] array = new String[0];
    String[] result = list.toArray(array);
    verify(delegate).toArray(array);
    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
    @Timeout(8000)
  void testToArray_withLargerArray_returnsFilledArray() {
    delegate.add("a");
    delegate.add("b");
    String[] array = new String[5];
    String[] result = list.toArray(array);
    verify(delegate).toArray(array);
    assertSame(array, result);
    assertEquals("a", result[0]);
    assertEquals("b", result[1]);
    assertNull(result[2]);
  }

  @Test
    @Timeout(8000)
  void testToArray_withSmallerArray_returnsNewArray() {
    delegate.add("x");
    delegate.add("y");
    String[] array = new String[1];
    String[] result = list.toArray(array);
    verify(delegate).toArray(array);
    assertNotSame(array, result);
    assertArrayEquals(new String[]{"x", "y"}, result);
  }

  @Test
    @Timeout(8000)
  void testToArray_reflectiveInvocation() throws Exception {
    String[] inputArray = new String[2];
    Method toArrayMethod = NonNullElementWrapperList.class.getDeclaredMethod("toArray", Object[].class);
    toArrayMethod.setAccessible(true);
    delegate.add("foo");
    Object result = toArrayMethod.invoke(list, (Object) inputArray);
    assertTrue(result instanceof String[]);
    String[] resultArray = (String[]) result;
    assertEquals("foo", resultArray[0]);
  }
}