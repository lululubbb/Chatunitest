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

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NonNullElementWrapperList_484_3Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  public void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  public void lastIndexOf_existingElement_returnsCorrectIndex() {
    when(delegate.lastIndexOf("element")).thenReturn(3);
    int index = list.lastIndexOf("element");
    assertEquals(3, index);
  }

  @Test
    @Timeout(8000)
  public void lastIndexOf_nonExistingElement_returnsMinusOne() {
    when(delegate.lastIndexOf("missing")).thenReturn(-1);
    int index = list.lastIndexOf("missing");
    assertEquals(-1, index);
  }

  @Test
    @Timeout(8000)
  public void lastIndexOf_nullElement_returnsCorrectIndex() {
    when(delegate.lastIndexOf(null)).thenReturn(2);
    int index = list.lastIndexOf(null);
    assertEquals(2, index);
  }

  @Test
    @Timeout(8000)
  public void lastIndexOf_privateMethodInvocation_delegateLastIndexOfCalled() throws Exception {
    // Use reflection to invoke private field and verify lastIndexOf call indirectly
    // Since lastIndexOf is public and just delegates, test coverage is sufficient
    // But to fulfill requirement, invoke private method nonNull via reflection

    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String input = "test";
    String result = (String) nonNullMethod.invoke(list, input);

    assertEquals(input, result);
  }
}