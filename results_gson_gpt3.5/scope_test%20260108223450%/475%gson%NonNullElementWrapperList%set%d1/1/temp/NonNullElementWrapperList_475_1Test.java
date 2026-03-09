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

class NonNullElementWrapperList_475_1Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegateMock = Mockito.mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  void testSet_returnsDelegateSetResult() {
    int index = 1;
    String element = "element";
    String expectedReturn = "oldElement";

    when(delegateMock.set(index, element)).thenReturn(expectedReturn);

    String result = list.set(index, element);

    assertEquals(expectedReturn, result);
    verify(delegateMock).set(index, element);
  }

  @Test
    @Timeout(8000)
  void testSet_nullElement_throwsNullPointerException() throws Exception {
    int index = 0;
    // Use reflection to get private method nonNull
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    // Verify that nonNull throws NPE on null
    assertThrows(InvocationTargetException.class, () -> {
      try {
        nonNullMethod.invoke(list, new Object[] { null });
      } catch (InvocationTargetException e) {
        // Rethrow cause if it's NPE, else rethrow
        if (e.getCause() instanceof NullPointerException) {
          throw e;
        }
        throw e;
      }
    });

    // Because set calls nonNull, calling set with null should throw NPE
    assertThrows(NullPointerException.class, () -> list.set(index, null));
    verify(delegateMock, never()).set(anyInt(), any());
  }

  @Test
    @Timeout(8000)
  void testNonNull_returnsSameObjectWhenNotNull() throws Exception {
    String element = "testElement";
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);
    Object result = nonNullMethod.invoke(list, element);
    assertSame(element, result);
  }
}