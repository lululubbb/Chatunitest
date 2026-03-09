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

class NonNullElementWrapperList_477_4Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void remove_WithValidIndex_RemovesAndReturnsElement() {
    delegate.add("a");
    delegate.add("b");
    delegate.add("c");

    String removed = list.remove(1);

    assertEquals("b", removed);
    verify(delegate).remove(1);
    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("c", list.get(1));
  }

  @Test
    @Timeout(8000)
  void remove_WithIndexOutOfBounds_ThrowsException() {
    delegate.add("a");

    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5));
    // Remove the verify(delegate, never()).remove(5); line because delegate.remove(5) is called internally and throws
    // We cannot verify that it was never called since it is called and throws
  }

  @Test
    @Timeout(8000)
  void remove_PrivateMethod_nonNull_ReturnsElement() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String element = "test";
    Object result = nonNullMethod.invoke(list, element);
    assertEquals(element, result);

    assertThrows(Exception.class, () -> nonNullMethod.invoke(list, (Object) null));
  }
}