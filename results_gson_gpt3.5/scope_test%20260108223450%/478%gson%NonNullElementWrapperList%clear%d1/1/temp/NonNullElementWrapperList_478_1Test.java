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

class NonNullElementWrapperList_478_1Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void clear_shouldClearDelegate() {
    delegate.add("one");
    delegate.add("two");
    assertFalse(delegate.isEmpty());

    list.clear();

    verify(delegate).clear();
    assertTrue(delegate.isEmpty());
  }

  @Test
    @Timeout(8000)
  void clear_onEmptyDelegate_shouldRemainEmpty() {
    assertTrue(delegate.isEmpty());
    list.clear();
    verify(delegate).clear();
    assertTrue(delegate.isEmpty());
  }

  @Test
    @Timeout(8000)
  void clear_privateMethod_nonNull_invocationViaReflection() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String input = "test";
    String output = (String) nonNullMethod.invoke(list, input);
    assertEquals(input, output);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> nonNullMethod.invoke(list, (Object) null));
    assertTrue(exception.getCause() instanceof NullPointerException);
  }
}