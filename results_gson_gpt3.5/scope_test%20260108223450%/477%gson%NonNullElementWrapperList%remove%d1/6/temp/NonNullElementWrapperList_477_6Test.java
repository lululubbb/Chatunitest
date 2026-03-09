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

class NonNullElementWrapperList_477_6Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegateMock = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void remove_shouldDelegateCallAndReturnElement() {
    String removedElement = "removed";
    when(delegateMock.remove(0)).thenReturn(removedElement);

    String result = list.remove(0);

    verify(delegateMock).remove(0);
    assertEquals(removedElement, result);
  }

  @Test
    @Timeout(8000)
  void remove_shouldThrowIndexOutOfBoundsException_whenDelegateThrows() {
    when(delegateMock.remove(5)).thenThrow(new IndexOutOfBoundsException());

    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(5));
    verify(delegateMock).remove(5);
  }

  @Test
    @Timeout(8000)
  void remove_privateMethod_nonNull_invocation() throws Exception {
    // Use reflection to access private method nonNull(E element)
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String input = "test";
    String result = (String) nonNullMethod.invoke(list, input);
    assertEquals(input, result);

    // Test null input throws NullPointerException
    assertThrows(
        java.lang.reflect.InvocationTargetException.class,
        () -> nonNullMethod.invoke(list, new Object[] {null}));
  }
}