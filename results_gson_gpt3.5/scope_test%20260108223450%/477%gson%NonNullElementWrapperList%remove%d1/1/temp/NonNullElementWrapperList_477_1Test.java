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

class NonNullElementWrapperList_477_1Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void remove_shouldCallDelegateRemoveAndReturnValue() {
    when(delegate.remove(1)).thenReturn("removedElement");
    String result = list.remove(1);
    verify(delegate).remove(1);
    assertEquals("removedElement", result);
  }

  @Test
    @Timeout(8000)
  void remove_shouldThrowIndexOutOfBoundsExceptionWhenDelegateThrows() {
    when(delegate.remove(5)).thenThrow(new IndexOutOfBoundsException());
    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5));
    verify(delegate).remove(5);
  }

  @Test
    @Timeout(8000)
  void removePrivateMethod_nonNull_shouldReturnSameElement() throws Exception {
    // Using reflection to access private method nonNull
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String element = "testElement";
    String result = (String) nonNullMethod.invoke(list, element);
    assertEquals(element, result);
  }
}