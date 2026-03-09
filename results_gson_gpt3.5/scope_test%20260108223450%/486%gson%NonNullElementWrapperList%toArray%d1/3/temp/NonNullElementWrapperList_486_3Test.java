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

public class NonNullElementWrapperList_486_3Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  public void setUp() {
    delegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  public void testToArray_withExactSizeArray() {
    delegate.add("a");
    delegate.add("b");
    String[] arr = new String[2];
    String[] result = list.toArray(arr);
    assertSame(arr, result);
    assertArrayEquals(new String[]{"a", "b"}, result);
    verify(delegate).toArray(arr);
  }

  @Test
    @Timeout(8000)
  public void testToArray_withSmallerArray() {
    delegate.add("x");
    delegate.add("y");
    String[] arr = new String[1];
    String[] result = list.toArray(arr);
    assertNotSame(arr, result);
    assertArrayEquals(new String[]{"x", "y"}, result);
    verify(delegate).toArray(arr);
  }

  @Test
    @Timeout(8000)
  public void testToArray_withLargerArray() {
    delegate.add("foo");
    String[] arr = new String[3];
    String[] result = list.toArray(arr);
    assertSame(arr, result);
    assertEquals("foo", result[0]);
    assertNull(result[1]);
    assertNull(result[2]);
    verify(delegate).toArray(arr);
  }

  @Test
    @Timeout(8000)
  public void testToArray_withEmptyList() {
    String[] arr = new String[0];
    String[] result = list.toArray(arr);
    assertNotNull(result);
    assertEquals(0, result.length);
    verify(delegate).toArray(arr);
  }

  @Test
    @Timeout(8000)
  public void testPrivateNonNullMethod() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    // Should return the element if non-null
    String element = "test";
    Object returned = nonNullMethod.invoke(list, element);
    assertEquals(element, returned);

    // Should throw NullPointerException if null
    assertThrows(
        java.lang.reflect.InvocationTargetException.class,
        () -> nonNullMethod.invoke(list, new Object[] {null}));
  }
}