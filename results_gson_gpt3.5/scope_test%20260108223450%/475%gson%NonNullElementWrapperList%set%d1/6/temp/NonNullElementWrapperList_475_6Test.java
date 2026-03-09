package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NonNullElementWrapperList_475_6Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testSet_returnsDelegateSetResult() {
    String oldValue = "old";
    String newValue = "new";
    int index = 1;

    // Mock delegate.set to return oldValue
    when(delegate.set(index, newValue)).thenReturn(oldValue);

    // Call set on list
    String result = list.set(index, newValue);

    // Verify delegate.set called with index and nonNull(newValue) (which is newValue since nonNull returns element if not null)
    verify(delegate).set(index, newValue);

    // Assert returned value is oldValue
    assertEquals(oldValue, result);
  }

  @Test
    @Timeout(8000)
  void testSet_nullElement_throwsNullPointerException() {
    int index = 0;
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> list.set(index, null));
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void testNonNull_privateMethod_withNonNullElement() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String element = "element";
    Object result = nonNullMethod.invoke(list, element);

    assertEquals(element, result);
  }

  @Test
    @Timeout(8000)
  void testNonNull_privateMethod_withNullElement_throwsNPE() throws NoSuchMethodException {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    assertThrows(InvocationTargetException.class, () -> {
      try {
        nonNullMethod.invoke(list, new Object[] { null });
      } catch (InvocationTargetException e) {
        // The cause should be NullPointerException
        assertTrue(e.getCause() instanceof NullPointerException);
        throw e;
      }
    });
  }
}