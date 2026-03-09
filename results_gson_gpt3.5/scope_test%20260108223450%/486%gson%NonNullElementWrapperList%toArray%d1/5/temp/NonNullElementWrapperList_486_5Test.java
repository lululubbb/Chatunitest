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

class NonNullElementWrapperList_486_5Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testToArray_withEmptyArray_returnsEmptyArray() {
    String[] input = new String[0];
    // Cast to Object[] to avoid ambiguity between toArray methods
    @SuppressWarnings("unchecked")
    String[] result = (String[]) list.toArray((Object[]) input);
    assertNotNull(result);
    assertEquals(0, result.length);
    verify(delegate).toArray(input);
  }

  @Test
    @Timeout(8000)
  void testToArray_withLargerArray_returnsArrayWithElementsAndNulls() {
    delegate.add("a");
    delegate.add("b");
    String[] input = new String[5];
    @SuppressWarnings("unchecked")
    String[] result = (String[]) list.toArray((Object[]) input);
    assertSame(input, result);
    assertEquals("a", result[0]);
    assertEquals("b", result[1]);
    assertNull(result[2]);
    assertNull(result[3]);
    assertNull(result[4]);
    verify(delegate).toArray(input);
  }

  @Test
    @Timeout(8000)
  void testToArray_withSmallerArray_returnsNewArray() {
    delegate.add("x");
    delegate.add("y");
    String[] input = new String[1];
    @SuppressWarnings("unchecked")
    String[] result = (String[]) list.toArray((Object[]) input);
    assertNotSame(input, result);
    assertEquals(2, result.length);
    assertArrayEquals(new String[] {"x", "y"}, result);
    verify(delegate).toArray(input);
  }

  @Test
    @Timeout(8000)
  void testToArray_withNullArray_throwsNullPointerException() {
    // According to ArrayList.toArray(T[] a) spec, passing null throws NPE
    assertThrows(NullPointerException.class, () -> list.toArray((Object[]) null));
  }

  @Test
    @Timeout(8000)
  void testPrivateNonNullMethod_withNonNullElement() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String input = "test";
    Object result = nonNullMethod.invoke(list, input);
    assertEquals(input, result);
  }

  @Test
    @Timeout(8000)
  void testPrivateNonNullMethod_withNullElement_throwsNullPointerException() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    assertThrows(Exception.class, () -> {
      try {
        nonNullMethod.invoke(list, new Object[] { null });
      } catch (Exception e) {
        // InvocationTargetException wraps the actual exception
        Throwable cause = e.getCause();
        if (cause instanceof NullPointerException) {
          throw cause;
        }
        throw e;
      }
    });
  }
}