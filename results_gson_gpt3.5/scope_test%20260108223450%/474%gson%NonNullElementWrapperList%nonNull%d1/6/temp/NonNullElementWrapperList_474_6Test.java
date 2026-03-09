package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_474_6Test {

  private NonNullElementWrapperList<String> list;
  private Method nonNullMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    list = new NonNullElementWrapperList<>(new ArrayList<>());
    nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void nonNull_withNonNullElement_returnsElement() throws InvocationTargetException, IllegalAccessException {
    String element = "test";
    Object result = nonNullMethod.invoke(list, element);
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  void nonNull_withNullElement_throwsNullPointerException() {
    Exception exception = assertThrows(InvocationTargetException.class, () -> {
      nonNullMethod.invoke(list, new Object[] { null });
    });
    Throwable cause = exception.getCause();
    assertTrue(cause instanceof NullPointerException);
    assertEquals("Element must be non-null", cause.getMessage());
  }
}