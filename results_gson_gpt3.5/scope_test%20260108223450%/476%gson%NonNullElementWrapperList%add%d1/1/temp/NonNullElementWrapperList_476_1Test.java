package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_476_1Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void addShouldAddNonNullElementAtIndex() {
    list.add(0, "element");
    verify(delegate).add(0, "element");
    assertEquals(1, list.size());
    assertEquals("element", list.get(0));
  }

  @Test
    @Timeout(8000)
  void addShouldThrowNullPointerExceptionWhenElementIsNull() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      // directly test nonNull to ensure null triggers exception
      nonNullMethod.invoke(list, (Object) null);
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof NullPointerException);
    assertEquals("Element must be non-null", cause.getMessage());
  }
}