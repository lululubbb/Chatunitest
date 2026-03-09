package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NonNullElementWrapperList_482_1Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() {
    delegateMock = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void contains_delegatesToDelegate() {
    String element = "testElement";

    when(delegateMock.contains(element)).thenReturn(true);
    assertTrue(list.contains(element));
    verify(delegateMock).contains(element);

    when(delegateMock.contains(element)).thenReturn(false);
    assertFalse(list.contains(element));
    verify(delegateMock, times(2)).contains(element);
  }

  @Test
    @Timeout(8000)
  void contains_nullElement() {
    when(delegateMock.contains(null)).thenReturn(true);
    assertTrue(list.contains(null));
    verify(delegateMock).contains(null);
  }

  @Test
    @Timeout(8000)
  void contains_privateDelegateFieldIsUsed() throws Exception {
    // Use reflection to check the private delegate field is the same as delegateMock
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    Object delegateFieldValue = delegateField.get(list);
    assertSame(delegateMock, delegateFieldValue);
  }
}