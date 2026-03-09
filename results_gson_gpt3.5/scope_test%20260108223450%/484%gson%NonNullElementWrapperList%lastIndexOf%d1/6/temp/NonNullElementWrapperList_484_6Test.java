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

public class NonNullElementWrapperList_484_6Test {

  private ArrayList<String> mockDelegate;
  private NonNullElementWrapperList<String> wrapperList;

  @BeforeEach
  public void setUp() {
    mockDelegate = mock(ArrayList.class);
    wrapperList = new NonNullElementWrapperList<>(mockDelegate);
  }

  @Test
    @Timeout(8000)
  public void testLastIndexOf_ObjectPresent() {
    String element = "testElement";
    when(mockDelegate.lastIndexOf(element)).thenReturn(2);

    int result = wrapperList.lastIndexOf(element);

    assertEquals(2, result);
  }

  @Test
    @Timeout(8000)
  public void testLastIndexOf_ObjectNotPresent() {
    String element = "absentElement";
    when(mockDelegate.lastIndexOf(element)).thenReturn(-1);

    int result = wrapperList.lastIndexOf(element);

    assertEquals(-1, result);
  }

  @Test
    @Timeout(8000)
  public void testLastIndexOf_NullObject() {
    when(mockDelegate.lastIndexOf(null)).thenReturn(-1);

    int result = wrapperList.lastIndexOf(null);

    assertEquals(-1, result);
  }
}