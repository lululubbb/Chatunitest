package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.NonNullElementWrapperList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

class NonNullElementWrapperList_472_1Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    delegate.add("first");
    delegate.add("second");
    delegate.add("third");
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testGet_validIndex_returnsElement() {
    assertEquals("first", list.get(0));
    assertEquals("second", list.get(1));
    assertEquals("third", list.get(2));
  }

  @Test
    @Timeout(8000)
  void testGet_indexOutOfBounds_throwsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
  }

  @Test
    @Timeout(8000)
  void testGet_reflectiveInvocation() throws Exception {
    Method getMethod = NonNullElementWrapperList.class.getDeclaredMethod("get", int.class);
    getMethod.setAccessible(true);
    String result = (String) getMethod.invoke(list, 1);
    assertEquals("second", result);
  }
}