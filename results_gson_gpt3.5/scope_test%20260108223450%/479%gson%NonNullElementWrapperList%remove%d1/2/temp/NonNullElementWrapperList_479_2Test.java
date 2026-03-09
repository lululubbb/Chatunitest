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

class NonNullElementWrapperList_479_2Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() {
    delegateMock = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void testRemove_Object_DelegatesToDelegate() {
    String element = "element";
    when(delegateMock.remove(element)).thenReturn(true);

    boolean result = list.remove(element);

    assertTrue(result);
    verify(delegateMock).remove(element);
  }

  @Test
    @Timeout(8000)
  void testRemove_Object_ReturnsFalseWhenDelegateReturnsFalse() {
    String element = "absent";
    when(delegateMock.remove(element)).thenReturn(false);

    boolean result = list.remove(element);

    assertFalse(result);
    verify(delegateMock).remove(element);
  }

  @Test
    @Timeout(8000)
  void testRemove_Object_WithNull() {
    when(delegateMock.remove(null)).thenReturn(false);

    boolean result = list.remove(null);

    assertFalse(result);
    verify(delegateMock).remove(null);
  }

  @Test
    @Timeout(8000)
  void testRemove_Object_ReflectionInvoke() throws Exception {
    // Using reflection to invoke private field and ensure remove calls delegate.remove
    Method removeMethod = NonNullElementWrapperList.class.getDeclaredMethod("remove", Object.class);
    removeMethod.setAccessible(true);

    String element = "reflection";
    when(delegateMock.remove(element)).thenReturn(true);

    boolean result = (boolean) removeMethod.invoke(list, element);

    assertTrue(result);
    verify(delegateMock).remove(element);
  }
}