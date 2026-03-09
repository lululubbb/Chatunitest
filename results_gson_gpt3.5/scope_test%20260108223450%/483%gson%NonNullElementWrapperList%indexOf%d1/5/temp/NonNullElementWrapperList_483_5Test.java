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

class NonNullElementWrapperList_483_5Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegateMock;

  @BeforeEach
  void setUp() {
    delegateMock = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void indexOf_delegatesToDelegateList() {
    String element = "testElement";
    when(delegateMock.indexOf(element)).thenReturn(3);

    int index = list.indexOf(element);

    assertEquals(3, index);
    verify(delegateMock).indexOf(element);
  }

  @Test
    @Timeout(8000)
  void indexOf_nullElement() {
    when(delegateMock.indexOf(null)).thenReturn(-1);

    int index = list.indexOf(null);

    assertEquals(-1, index);
    verify(delegateMock).indexOf(null);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementNotFound() {
    String element = "notFound";
    when(delegateMock.indexOf(element)).thenReturn(-1);

    int index = list.indexOf(element);

    assertEquals(-1, index);
    verify(delegateMock).indexOf(element);
  }
}