package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NonNullElementWrapperList_484_5Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  public void setUp() {
    delegateMock = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  public void testLastIndexOf_ObjectPresent() {
    String element = "test";
    when(delegateMock.lastIndexOf(element)).thenReturn(2);

    int result = list.lastIndexOf(element);

    assertEquals(2, result);
  }

  @Test
    @Timeout(8000)
  public void testLastIndexOf_ObjectAbsent() {
    String element = "absent";
    when(delegateMock.lastIndexOf(element)).thenReturn(-1);

    int result = list.lastIndexOf(element);

    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testLastIndexOf_NullObject() {
    when(delegateMock.lastIndexOf(null)).thenReturn(-1);

    int result = list.lastIndexOf(null);

    assertEquals(-1, result);
  }
}