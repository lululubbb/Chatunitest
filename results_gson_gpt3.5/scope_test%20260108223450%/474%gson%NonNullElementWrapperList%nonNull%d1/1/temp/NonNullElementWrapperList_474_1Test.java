package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class NonNullElementWrapperList_474_1Test {

  private NonNullElementWrapperList<String> list;
  private Method nonNullMethod;

  @BeforeEach
  void setUp() throws Exception {
    list = new NonNullElementWrapperList<>(new ArrayList<>());
    nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testNonNull_withNonNullElement_returnsElement() throws Exception {
    String element = "test";
    Object result = nonNullMethod.invoke(list, element);
    assertEquals(element, result);
  }

  @Test
    @Timeout(8000)
  void testNonNull_withNullElement_throwsNullPointerException() throws Exception {
    try {
      nonNullMethod.invoke(list, new Object[] { null });
      fail("Expected NullPointerException");
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      assertNotNull(cause);
      assertTrue(cause instanceof NullPointerException);
      assertEquals("Element must be non-null", cause.getMessage());
    }
  }
}