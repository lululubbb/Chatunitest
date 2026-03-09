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

class NonNullElementWrapperList_486_1Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = Mockito.spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testToArrayWithExactSizeArray() {
    delegate.add("one");
    delegate.add("two");

    String[] input = new String[2];
    String[] result = list.toArray(input);

    assertSame(input, result);
    assertArrayEquals(new String[]{"one", "two"}, result);
    verify(delegate).toArray(input);
  }

  @Test
    @Timeout(8000)
  void testToArrayWithLargerArray() {
    delegate.add("one");
    delegate.add("two");

    String[] input = new String[5];
    String[] result = list.toArray(input);

    assertSame(input, result);
    assertEquals("one", result[0]);
    assertEquals("two", result[1]);
    assertNull(result[2]);
    verify(delegate).toArray(input);
  }

  @Test
    @Timeout(8000)
  void testToArrayWithSmallerArray() {
    delegate.add("one");
    delegate.add("two");

    String[] input = new String[1];
    String[] result = list.toArray(input);

    assertNotSame(input, result);
    assertArrayEquals(new String[]{"one", "two"}, result);
    verify(delegate).toArray(input);
  }

  @Test
    @Timeout(8000)
  void testToArrayEmptyDelegate() {
    String[] input = new String[0];
    String[] result = list.toArray(input);

    assertSame(input, result);
    verify(delegate).toArray(input);
  }

  @Test
    @Timeout(8000)
  void testInvokePrivateNonNullMethod() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String input = "test";
    String output = (String) nonNullMethod.invoke(list, input);
    assertEquals(input, output);

    Exception exception = assertThrows(Exception.class, () -> nonNullMethod.invoke(list, (Object) null));
    assertTrue(exception.getCause() instanceof NullPointerException);
  }
}